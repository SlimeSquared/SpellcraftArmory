package com.slimesquared.spellcraftarmory.mixin;

import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public abstract void remove(Entity.RemovalReason p_146834_);

    //ToDo: make petrify stop animations?
    @ModifyVariable(method = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Vec3 movement(Vec3 vel, MoverType moverType) {
        var entity = (Object) this;
        //This is fine, the mixin intercepts the function too early for intellij to tell it's working properly
        if ((moverType == MoverType.PLAYER || moverType == MoverType.SELF)
                && entity instanceof LivingEntity livingEntity
                && livingEntity.hasEffect(ModEffects.PETRIFY.get())) {
            if (livingEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue() < 0.5) {
                vel = vel.add(0, -0.5, 0);
            }
            vel = new Vec3(0, Math.min(vel.y, 0), 0);
        }
        return vel;
    }

    @Inject(method = "makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    public void stuckInBlock(BlockState state, Vec3 vec3, CallbackInfo ci) {
        var entity = (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            for (var armor : livingEntity.getAllSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor && spellArmor.getSpell(armor) == Spells.SpellList.Unstoppable) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "Lnet/minecraft/world/entity/Entity;getBlockSpeedFactor()F", at = @At("RETURN"), cancellable = true)
    public void speedFactor(CallbackInfoReturnable<Float> cir) {
        var entity = (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            for (var armor : livingEntity.getAllSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor && spellArmor.getSpell(armor) == Spells.SpellList.Unstoppable) {
                    cir.setReturnValue(1.0F);
                }
            }
        }
    }
}
