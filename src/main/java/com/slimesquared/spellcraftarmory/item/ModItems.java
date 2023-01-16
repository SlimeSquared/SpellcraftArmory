package com.slimesquared.spellcraftarmory.item;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpellcraftArmory.MOD_ID);
    public static void register(IEventBus e) {
        ITEMS.register(e);
    }

    public static final RegistryObject<Item> PRIMARY_WAND = ITEMS.register("primary_wand", () ->
            new CastingItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.MOD_TAB), 0));
    public static final RegistryObject<Item> SECONDARY_WAND = ITEMS.register("secondary_wand", () ->
            new CastingItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.MOD_TAB), 1));
    public static final RegistryObject<Item> RUNE_WAND = ITEMS.register("rune_wand", () ->
            new CastingItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.MOD_TAB), 2));
    public static final RegistryObject<Item> RUNE_CHALK = ITEMS.register("rune_chalk", () ->
            new ChalkItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.MOD_TAB)));

    public static final RegistryObject<Item> SPELL_CLOTH = ITEMS.register("spell_cloth", () ->
            new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    //ToDo: Dark, Druid (water/plant/air/fire)
    // Legging passives: slippery (shift locks momentum, friction proportional to [speed - floor value]), inner tube
    // Boots: slime
    // generic ideas: AoE slowdown, smokescreen, defense increases/decreases on repeated damage (effect?)
    //    slow fall on sneak, fast ladders, wall climb, universal item allay

    //ToDo Spells: potions -> lingering, tunneling (tp to other side of block, keep momentum)
    // shield wave spell (push entities), dirt bomb/other terraforming, proj w/ linger pot (slow/curse),
    // vex summoner (attacks attacked), evoker jaw
    // freezing proj (place snow), remote ender chest/crafting table
    // make tele crown not anger endermen

    //ToDo Runes: tele cast to block, tele on top (new chalk?, not exactly rune)
    // summoning rune?, knockback (explode), redstone pulse,
    // Recolor empty runes w/ dyes? Nbt for texture number (randomized)
    // add particles: spherical shell around null rune

    //ToDo Models: horse, light particles

    //ToDo: mixins working when "./gradlew build" to jar, but not in runClient
    // make recipe work with JEI

    //Make sure to add recipe json, loot modifier json (+add to global loot modifier), item json+texture, and armor texture
    public static final RegistryObject<Item> PETRIFICATION_HAT = ITEMS.register("petrification_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.Petrify, "box_helmet", "petrification_hat"));
    public static final RegistryObject<Item> BLINDING_HAT = ITEMS.register("blinding_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.BlindRay, "witch_hat", "blinding_hat"));
    public static final RegistryObject<Item> POTION_HEAL_HAT = ITEMS.register("potion_heal_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.HealDrink, "witch_hat", "witch_hat"));
    public static final RegistryObject<Item> TELEPORT_HAT = ITEMS.register("teleport_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.Teleport, "box_hat", "teleport_hat"));
    public static final RegistryObject<Item> CRACKING_HAT = ITEMS.register("cracking_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.Crack, "witch_hat", "cracking_hat"));
    public static final RegistryObject<Item> LIGHT_BOLT_HAT = ITEMS.register("light_bolt_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.LightBolt, "box_hat", "light_bolt_hat"));
    public static final RegistryObject<Item> BLOODLUST_HAT = ITEMS.register("bloodlust_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.Bloodlust, "box_helmet", "bloodlust_hat"));
    public static final RegistryObject<Item> LIGHTNING_HAT = ITEMS.register("lightning_hat", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 0, Spells.SpellList.LightningBolt, "box_helmet", "lightning_hat"));

    public static final RegistryObject<Item> CURSE_ROBE = ITEMS.register("curse_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.CurseRay, "robe", "witch_robe"));
    public static final RegistryObject<Item> COOLDOWN_ROBE = ITEMS.register("cooldown_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.CooldownRay, "robe", "cooldown_robe"));
    public static final RegistryObject<Item> RECOIL_ROBE = ITEMS.register("recoil_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.Recoil, "robe", "recoil_robe"));
    public static final RegistryObject<Item> MOSSY_ROBE = ITEMS.register("mossy_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.Mossify, "robe", "mossy_robe"));
    public static final RegistryObject<Item> LIGHT_HEAL_ROBE = ITEMS.register("light_heal_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.Photosynthesis, "robe", "light_heal_robe"));
    public static final RegistryObject<Item> LIGHT_BLOCK_ROBE = ITEMS.register("light_block_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.LightBlock, "robe", "light_block_robe"));
    public static final RegistryObject<Item> BRIDGE_BOLT_ROBE = ITEMS.register("bridge_bolt_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.BridgeBolt, "robe", "bridge_bolt_robe"));
    public static final RegistryObject<Item> ANIMAL_HEAL_ROBE = ITEMS.register("animal_heal_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.PetRegen, "robe", "animal_heal_robe"));
    public static final RegistryObject<Item> GHOST_HORSE_ROBE = ITEMS.register("ghost_horse_robe", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 1, Spells.SpellList.HorseSummon, "robe", "ghost_horse_robe"));

    public static final RegistryObject<Item> CREEPER_RUNE_LEGGINGS = ITEMS.register("creeper_rune_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), RuneSpells.CreeperSound, "leggings", "creeper_leggings"));
    public static final RegistryObject<Item> EXPLOSION_RUNE_LEGGINGS = ITEMS.register("explosion_rune_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), RuneSpells.Explode, "leggings", "explosion_leggings"));
    public static final RegistryObject<Item> SILENCE_RUNE_LEGGINGS = ITEMS.register("silence_rune_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), RuneSpells.Silence, "leggings", "silence_leggings"));
    public static final RegistryObject<Item> TELEPORT_RUNE_LEGGINGS = ITEMS.register("teleport_rune_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), RuneSpells.Teleport, "leggings", "teleport_leggings"));
    public static final RegistryObject<Item> LAVA_CHARM_LEGGINGS = ITEMS.register("lava_charm_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.LavaCharm, "leggings", "lava_charm_leggings"));
    public static final RegistryObject<Item> SLIDE_LEGGINGS = ITEMS.register("slide_leggings", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.Slide, "leggings"));

    public static final RegistryObject<Item> JUMP_BOOTS = ITEMS.register("jump_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.PassiveJump, "box_boots", "jump_boots"));
    public static final RegistryObject<Item> ICE_BOOTS = ITEMS.register("ice_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.IceSlip, "box_boots", "ice_boots"));
    public static final RegistryObject<Item> KNOCKBACK_BOOTS = ITEMS.register("knockback_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.PassiveKnockback, "box_boots", "knockback_boots"));
    public static final RegistryObject<Item> UNSTOPPABLE_BOOTS = ITEMS.register("unstoppable_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.Unstoppable, "box_boots", "unstoppable_boots"));
    public static final RegistryObject<Item> MODERATE_FALL_BOOTS = ITEMS.register("moderate_fall_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.ModerateFall, "heels", "moderate_fall_boots"));
    public static final RegistryObject<Item> MINECART_BOOTS = ITEMS.register("minecart_boots", () ->
            new SpellArmorItem(ModArmorMaterials.SPELL_CLOTH_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB), 3, Spells.SpellList.MinecartSpeed, "box_boots", "minecart_boots"));

}
