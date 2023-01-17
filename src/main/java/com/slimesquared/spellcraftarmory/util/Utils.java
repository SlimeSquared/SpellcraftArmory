package com.slimesquared.spellcraftarmory.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Utils {
    public static void applyEffect(LivingEntity entity, MobEffect effect, int duration, int level, boolean showParticles) {
        if (entity != null) {
            entity.addEffect(new MobEffectInstance(effect, duration, level, false, showParticles));
        }
        else {
            System.out.println("applying effect to null entity");
        }
    }

    public static LivingEntity raycastEntity(LivingEntity sourceEntity, double distance) {
        Vec3 start = sourceEntity.getEyePosition();
        Vec3 end = start.add(sourceEntity.getLookAngle().normalize().scale(distance));
        double nearEntityDist = distance + 10;
        LivingEntity pickedEntity = null;
        for (LivingEntity nearbyEntity : sourceEntity.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), sourceEntity, new AABB(start, end))) {
            if (nearbyEntity.distanceTo(sourceEntity) < nearEntityDist) {
                nearEntityDist = nearbyEntity.distanceTo(sourceEntity);
                pickedEntity = nearbyEntity;
            }
        }
        return pickedEntity;
    }

    public static BlockHitResult raycastBlock(LivingEntity sourceEntity, double distance, ClipContext.Fluid fluidInteraction) {
        Vec3 start = sourceEntity.getEyePosition();
        Vec3 end = start.add(sourceEntity.getLookAngle().normalize().scale(distance));
        return sourceEntity.level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, fluidInteraction, sourceEntity));
    }
}
