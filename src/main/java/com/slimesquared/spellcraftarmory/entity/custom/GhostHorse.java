package com.slimesquared.spellcraftarmory.entity.custom;

import com.slimesquared.spellcraftarmory.entity.ModEntityTypes;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class GhostHorse extends AbstractHorse {
    public GhostHorse(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    public GhostHorse(Entity owner) {
        super(ModEntityTypes.GHOST_HORSE.get(), owner.level);
        this.setOwnerUUID(owner.getUUID());
    }

    int ticksUnridden;

    //Averages of normal horse: 0.225 speed, 0.7 jump
    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.JUMP_STRENGTH, 0.75);
    }

    protected void randomizeAttributes(RandomSource randSource) {
        Objects.requireNonNull(this.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(this.generateRandomJumpStrength(randSource));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        var rider = super.getControllingPassenger();
        var flag = false;
        if (rider instanceof Player player) {
            for (var armor : player.getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor && spellArmor.getSpell(armor) == Spells.SpellList.HorseSummon) {
                    flag = true;
                    this.ticksUnridden = 0;
                }
            }
        }
        if (!flag) {
            this.ticksUnridden++;
            if (this.ticksUnridden > 600) {
                this.kill();
            }
        }

        return rider;
    }

    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    //removed super call to make horse not rear intermittently
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        super.getHurtSound(damageSource);
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        } else {
            if (!itemstack.isEmpty()) {
                if (itemstack.is(Items.SADDLE) && !this.isSaddled()) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }

                InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                if (interactionresult.consumesAction()) {
                    return interactionresult;
                }
            }

            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    protected void addBehaviourGoals() {
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }
}
