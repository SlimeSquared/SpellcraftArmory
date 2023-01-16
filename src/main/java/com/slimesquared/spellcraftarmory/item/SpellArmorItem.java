package com.slimesquared.spellcraftarmory.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SpellArmorItem extends GeoArmorItem implements IAnimatable {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    private static final UUID NEGATION_MODIFIER = UUID.fromString("e418210c-5de5-4337-bad3-df008147e575");

    public int spellLvl;
    public Spells.SpellList spell;
    public RuneSpells runeSpell;
    public String model;
    public String textureLocation;
    public EquipmentSlot slot;

    public SpellArmorItem(ModArmorMaterials material, EquipmentSlot slot, Properties properties, int spellLvl, Spells.SpellList spell, RuneSpells runeSpell, String model, String textureName) {
        super(material, slot, properties);
        this.spellLvl = spellLvl;
        this.spell = spell;
        this.runeSpell = runeSpell;
        this.model = "geo/" + model + ".geo.json";
        this.textureLocation = "textures/models/armor/" + textureName + ".png";
        this.slot = slot;

        var defense = material.getDefenseForSlot(slot);
        var toughness = material.getToughness();
        var knockbackResistance = spell == Spells.SpellList.PassiveKnockback ? 1 : this.knockbackResistance;
        var attackKnockback = spell == Spells.SpellList.PassiveKnockback ? 1 : 0;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        if (attackKnockback > 0) {
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(uuid, "Armor attack knockback", attackKnockback, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
    }

    public SpellArmorItem(ModArmorMaterials material, EquipmentSlot slot, Properties properties, int spellLvl, Spells.SpellList spell, String model, String textureName) {
        this(material, slot, properties, spellLvl, spell, RuneSpells.None, model, textureName);
    }
    public SpellArmorItem(ModArmorMaterials material, EquipmentSlot slot, Properties properties, RuneSpells runeSpell, String model, String textureName) {
        this(material, slot, properties, 2, Spells.SpellList.None, runeSpell, model, textureName);
    }
    public SpellArmorItem(ModArmorMaterials material, EquipmentSlot slot, Properties properties, int spellLvl, Spells.SpellList spell, String model) {
        this(material, slot, properties, spellLvl, spell, model, "null");
    }

    public SpellArmorItem(ModArmorMaterials material, EquipmentSlot slot, Properties properties, RuneSpells runeSpell, String model) {
        this(material, slot, properties, runeSpell, model, "null");
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {

        String damageText = null;
        String buffText = null;
        String debuffText = null;
        switch (this.spell) {
            case BlindRay -> debuffText = "Blindness (0:05)";
            case Bloodlust -> buffText = "Strength (0:01) On Hit";
            case CurseRay -> debuffText = "Curse (0:30)";
            case HealDrink -> buffText = "Water to Instant Health";
            case LightBolt -> damageText = "4 - 8 Attack Damage";
            case PetRegen -> buffText = "Regeneration (0:20) to Pets";
            case Petrify -> debuffText = "Petrification (0:08)";
            case Photosynthesis -> buffText = "Regeneration";
        }

        if (damageText != null) {
            components.add(Component.literal(damageText).withStyle(ChatFormatting.DARK_GREEN));
        }
        if (buffText != null) {
            components.add(Component.literal(buffText).withStyle(ChatFormatting.BLUE));
        }
        if (debuffText != null) {
            components.add(Component.literal(debuffText).withStyle(ChatFormatting.RED));
        }

        if (Spells.getCooldown(this.spell) != 0) {
        components.add(Component.literal(Spells.getCooldown(this.spell) / 20F + "s Cooldown").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    public Spells.SpellList getSpell(ItemStack stack) {
        if (stack.getDamageValue() + 1 < stack.getMaxDamage()) {
            return this.spell;
        }
        else {
            return Spells.SpellList.None;
        }
    }

    public RuneSpells getRuneSpell(ItemStack stack) {
        if (stack.getDamageValue() + 1 < stack.getMaxDamage()) {
            return this.runeSpell;
        }
        else {
            return RuneSpells.None;
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getDamageValue() + amount >= stack.getMaxDamage()) {
            stack.setDamageValue(stack.getMaxDamage() - 1);
            return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (stack.getDamageValue() == stack.getMaxDamage() - 1) {
            return ImmutableMultimap.of();
        }
        else return super.getAttributeModifiers(slot, stack);
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> e) {
        e.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
