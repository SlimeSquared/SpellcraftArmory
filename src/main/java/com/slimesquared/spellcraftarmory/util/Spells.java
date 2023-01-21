package com.slimesquared.spellcraftarmory.util;

import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.entity.custom.GhostHorse;
import com.slimesquared.spellcraftarmory.entity.custom.SpellProjectile;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Spells {

    public enum SpellList implements StringRepresentable {
        BlindRay("blind_ray"),
        Bloodlust("bloodlust"),
        BridgeBolt("bridge_bolt"),
        CooldownRay("cooldown_ray"),
        Crack("crack"),
        CurseRay("curse_ray"),
        HealDrink("heal_drink"),
        HorseSummon("horse_summon"),
        IceSlip("ice_slip"),
        LavaCharm("lava_charm"),
        LightBlock("light_block"),
        LightBolt("light_bolt"),
        LightningBolt("lightning_bolt"),
        MinecartSpeed("minecart_speed"),
        ModerateFall("moderate_fall"),
        Mossify("mossify"),
        PassiveJump("passive_jump"),
        PassiveKnockback("passive_knockback"),
        PetRegen("pet_regen"),
        Petrify("petrify"),
        Photosynthesis("photosynthesis"),
        Recoil("recoil"),
        Slide("slide"),
        Teleport("teleport"),
        Unstoppable("unstoppable"),
        None("none");

        private final String name;

        SpellList(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    //ToDo: add sound effects, particles
    public static int castSpell(SpellList spell, LivingEntity entity) {
        var sound = SoundEvents.NOTE_BLOCK_CHIME;
        float vol = 1;
        float pitch = 1;
        switch (spell) {
            case BlindRay -> {
                double distance = 4;
                var victim = Utils.raycastEntity(entity, distance);
                if (victim != null) {
                    Utils.applyEffect(victim, MobEffects.BLINDNESS, 100, 0, true);
                }
                else return 0;
            }
            case BridgeBolt -> {
                SpellProjectile bridgeProj = new SpellProjectile(entity, SpellList.BridgeBolt);
                bridgeProj.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0F, 3F, 0F);
                bridgeProj.setPos(entity.getEyePosition());
                entity.level.addFreshEntity(bridgeProj);
            }
            case CooldownRay -> {
                double distance = 12;
                var victim = Utils.raycastEntity(entity, distance);
                if (victim instanceof Player player) {
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), getCooldown(SpellList.CooldownRay) - 20);
                }
                else return 0;
            }
            case CurseRay -> {
                double distance = 6;
                var victim = Utils.raycastEntity(entity, distance);
                if (victim != null) {
                    Utils.applyEffect(victim, ModEffects.CURSE.get(), 600, 0, true);
                }
                else return 0;
            }
            case HorseSummon -> {
                GhostHorse horse = new GhostHorse(entity);
                horse.setPos(entity.position().add(entity.getViewVector(1).x * 1.5, 0, entity.getViewVector(1).z * 1.5));
                if (!entity.level.noCollision(horse)) {
                    horse.setPos(horse.position().add(0, 1, 0));
                }
                if (!entity.level.noCollision(horse)) {
                    horse.remove(Entity.RemovalReason.DISCARDED);
                    return 0;
                }
                horse.lookAt(EntityAnchorArgument.Anchor.EYES, entity.getEyePosition().add(entity.getViewVector(1).scale(10)));
                entity.level.addFreshEntity(horse);
            }
            case LightBlock -> {
                SpellProjectile lightProj = new SpellProjectile(entity, SpellList.LightBlock);
                //input entity, rotation, y direction additive (default 0), velocity (default 1.5), randomness (default 1)
                lightProj.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0F, 4F, 0F);
                lightProj.setPos(entity.getEyePosition());
                lightProj.setNoGravity(true);
                entity.level.addFreshEntity(lightProj);
            }
            case LightBolt -> {
                SpellProjectile lightBolt = new SpellProjectile(entity, SpellList.LightBolt);
                //input entity, rotation, y direction additive (default 0), velocity (default 1.5), randomness (default 1)
                lightBolt.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0F, 4.5F, 1F);
                lightBolt.setPos(entity.getEyePosition());
                entity.level.addFreshEntity(lightBolt);
            }
            case LightningBolt -> {
                double distance = 12;

                var start = entity.getEyePosition();
                var randRay = new Vec3(entity.level.random.nextFloat(), entity.level.random.nextFloat(), entity.level.random.nextFloat())
                        .subtract(0.5, 0.5, 0.5).scale(0.5);
                var end = start.add(entity.getLookAngle().add(randRay).normalize().scale(distance));

                double nearEntityDist = distance + 6;
                LivingEntity victim = null;
                for (LivingEntity nearbyEntity : entity.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), entity, new AABB(start, end))) {
                    if (nearbyEntity.distanceTo(entity) < nearEntityDist) {
                        nearEntityDist = nearbyEntity.distanceTo(entity);
                        victim = nearbyEntity;
                    }
                }

                var blockHit = entity.level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));

                Vec3 pos = null;
                if (blockHit.getType() != HitResult.Type.MISS) {
                    pos = blockHit.getLocation();
                }
                if (victim != null && (blockHit.getType() == HitResult.Type.MISS || victim.distanceTo(entity) < blockHit.distanceTo(entity))) {
                    pos = victim.position();
                }
                if(pos == null) {
                    return 0;
                }

                var bolt = EntityType.LIGHTNING_BOLT.create(entity.level);
                if (!entity.level.isClientSide && bolt != null) {
                    bolt.moveTo(pos);
                    entity.level.addFreshEntity(bolt);
                }
            }
            case PetRegen -> {
                var petList = entity.level.getNearbyEntities(TamableAnimal.class, TargetingConditions.forNonCombat().range(12), entity, entity.getBoundingBox().inflate(12));
                boolean flag = false;
                for (var pet : petList) {
                    if (pet.getOwner() == entity) {
                        Utils.applyEffect(pet, MobEffects.REGENERATION, 400, 0, true);
                        flag = true;
                    }
                }
                if (!flag) {
                    return 0;
                }
            }
            case Petrify -> {
                if (!entity.isInvisible()) {
                    var nearbyVictims = entity.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat().range(12), entity, entity.getBoundingBox().inflate(12));
                    for (LivingEntity victim : nearbyVictims) {
                        var vicView = victim.getViewVector(1).normalize();
                        var offsetVec = entity.getEyePosition().subtract(victim.getEyePosition()).normalize();
                        if (Math.acos(offsetVec.dot(vicView)) * 180 / Math.PI < 70) {
                            Utils.applyEffect(victim, ModEffects.PETRIFY.get(), victim instanceof Player ? 80 : 160, 0, true);
                            if (victim instanceof Player player) {
                                for (var item : player.getAllSlots()) {
                                    player.getCooldowns().addCooldown(item.getItem(), 80);
                                }
                            }
                        }
                    }
                }
            }
            case Recoil -> {
                double force = 1.5;
                if (entity.fallDistance > 2) {
                    force *= 0.5;
                }
                entity.setDeltaMovement(entity.getDeltaMovement().add(entity.getLookAngle().normalize().scale(-force)));
                if (entity.getLookAngle().y < 0) {
                    entity.resetFallDistance();
                }
            }
            case Teleport -> {
                var distance = 8;
                var hitResult = Utils.raycastBlock(entity, distance, ClipContext.Fluid.NONE);
                var pos = Vec3.atBottomCenterOf(hitResult.getBlockPos());
                if (hitResult.getType() != HitResult.Type.MISS) {
                    //if block above selected block does not block motion, teleport to its top face. Otherwise, back teleport 1 block
                    if (!entity.level.getBlockState(hitResult.getBlockPos().above(1)).getMaterial().blocksMotion()) {
                        pos = pos.add(0, entity.level.getBlockFloorHeight(hitResult.getBlockPos()), 0);
                    } else {
                        pos = pos.relative(hitResult.getDirection(), 1);
                    }
                } else {
                    var lowerTele = entity.level.clip(new ClipContext(pos, pos.add(0, -1 * distance, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
                    var lowerPos = Vec3.atBottomCenterOf(lowerTele.getBlockPos());
                    if (lowerTele.getType() != HitResult.Type.MISS) {
                        pos = lowerPos.add(0, entity.level.getBlockFloorHeight(lowerTele.getBlockPos()), 0);
                    }
                }
                entity.teleportTo(pos.x, pos.y, pos.z);
                entity.resetFallDistance();
            }
        }
        if (entity instanceof Player player && player.level.isClientSide) {
            player.level.playLocalSound(player.position().x, player.position().y, player.position().z, sound, SoundSource.PLAYERS, vol, pitch, false);
        }
        return getCooldown(spell);
    }

    public static int getCooldown(SpellList spell) {
        int cooldown = 0;
        switch (spell) {
            case BlindRay -> cooldown = 400;
            case BridgeBolt -> cooldown = 200;
            case CooldownRay -> cooldown = 160;
            case CurseRay -> cooldown = 400;
            case HorseSummon -> cooldown = 100;
            case LightBlock -> cooldown = 2;
            case LightBolt -> cooldown = 60;
            case LightningBolt -> cooldown = 400;
            case PetRegen -> cooldown = 200;
            case Petrify -> cooldown = 40;
            case Recoil -> cooldown = 25;
            case Teleport -> cooldown = 100;
        }
        return cooldown;
    }
}
