package com.slimesquared.spellcraftarmory.entity.custom;

import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.entity.ModEntityTypes;
import com.slimesquared.spellcraftarmory.util.Spells;
import com.slimesquared.spellcraftarmory.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.slimesquared.spellcraftarmory.util.Spells.SpellList.LightBolt;
import static com.slimesquared.spellcraftarmory.util.Spells.SpellList.None;

public class SpellProjectile extends Projectile {
    private static final EntityDataAccessor<String> SPELL = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SpellProjectile.class, EntityDataSerializers.INT);

    public SpellProjectile(EntityType<? extends SpellProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpellProjectile(Entity owner, Spells.SpellList spell) {
        super(ModEntityTypes.SPELL_PROJECTILE.get(), owner.level);
        this.setSpell(spell);
        this.setColor(defaultColor(spell));
        this.setOwner(owner);
    }

    public Spells.SpellList getSpell() {
        return Spells.SpellList.valueOf(this.entityData.get(SPELL));
    }
    public void setSpell(Spells.SpellList spell) {
        this.entityData.set(SPELL, spell.name());
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }
    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }
    public int defaultColor(Spells.SpellList spell) {
        int color;
        switch (spell) {
            default -> color = 0xfe02ffff;
            case BridgeBolt -> color = 0x288200ff;
            case LightBlock -> color = 0xffffffff;
            case LightBolt -> color = 0xffffffff;
        }
        return color;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SPELL, Spells.SpellList.None.name());
        this.entityData.define(COLOR, 0xfe02ffff);
    }

    public Vec3 randVec;
    public void initRotation() {
        randVec = new Vec3( this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        var pos = hitResult.getBlockPos();
        switch (this.getSpell()) {
            case BridgeBolt -> {
                if (level.isClientSide) {
                    return;
                }
                if (level.getBlockState(pos).isSolidRender(this.level, pos) || this.tickCount > 300) {
                    this.discard();
                }
                if (this.getOwner() != null) {
                    var owner = this.getOwner();
                    var startPos = owner.position().subtract(0, 1, 0);
                    var path = hitResult.getLocation().subtract(startPos);
                    var dL = path.normalize().scale(0.5);
                    var iterations = Math.round(path.length() / dL.length());
                    for (int i = 0; i <= iterations; i++) {
                        var offset = dL.scale(i);
                        var blockPos = new BlockPos(startPos.add(offset));
                        var blockState = level.getBlockState(blockPos);
                        if (blockState.getMaterial().isReplaceable() || blockState.is(ModBlocks.SPECTRAL_BLOCK.get())) {
                            level.setBlockAndUpdate(blockPos, ModBlocks.SPECTRAL_BLOCK.get().defaultBlockState());
                            level.scheduleTick(blockPos, ModBlocks.SPECTRAL_BLOCK.get(), level.getRandom().nextInt(400, 420));
                        }
                    }
                }
                this.discard();
            }
            case LightBlock -> {
                if (level.getBlockState(pos).isSolidRender(this.level, pos) || this.tickCount > 400) {
                    this.discard();
                }
                var placePos = pos.relative(hitResult.getDirection(), 1);
                for (int i = 0; i < 2; i++) {
                    if (level.getBlockState(placePos).getBlock() == Blocks.AIR) {
                        level.setBlockAndUpdate(placePos, Blocks.GLOWSTONE.defaultBlockState());
                        this.discard();
                    }
                }
                this.discard();
            }
            case LightBolt -> {
                if (level.getBlockState(pos).isSolidRender(this.level, pos)) {
                    this.discard();
                }
                else {
                    if (level.getBlockState(pos).getBlock() instanceof BeaconBeamBlock glass) {
                        var colorArray = glass.getColor().getTextureDiffuseColors();
                        int r = (int) (colorArray[0] * 255);
                        int g = (int) (colorArray[1] * 255);
                        int b = (int) (colorArray[2] * 255);
                        this.setColor(r << 24 | g << 16 | b << 8 | 0xFF);
                    }
                }
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        var spell = this.getSpell();
        if (spell == LightBolt && hitResult.getEntity() instanceof LivingEntity victim && this.getOwner() != null) {
            int maxDamage = 8;
            int minDamage = 4;
            float damage = ((victim.isInvertedHealAndHarm() ? 2 : 1) * (((maxDamage - minDamage) / 15F) * this.getOwner().level.getRawBrightness(this.getOwner().blockPosition(), 0) + minDamage));
            victim.hurt(DamageSource.indirectMagic(this, this.getOwner()), damage);
            Utils.applyEffect(victim, MobEffects.GLOWING, 120, 0, true);
            if (this.getOwner() instanceof LivingEntity) {
                ((LivingEntity) this.getOwner()).setLastHurtMob(victim);
            }
        }
    }

    public void tick() {
        float friction = 0.01F;
        float waterFriction = 0.8F;
        float gravity = 0.06F;

        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        if (this.level.isClientSide && this.getTrailParticle() != null) {
        this.level.addParticle(this.getTrailParticle(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        this.setDeltaMovement(vec3.scale(this.isInWater() ? waterFriction : (1 - friction)));
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -gravity, 0.0D));
        }

        this.setPos(d0, d1, d2);
    }

    public ParticleOptions getTrailParticle() {
        ParticleOptions particle;
        switch (this.getSpell()) {
            case BridgeBolt -> particle = null;
            case LightBlock -> particle = null;
            case LightBolt -> particle = ParticleTypes.END_ROD;
            default -> particle = ParticleTypes.HEART;
        }
        return particle;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        var spell = this.getSpell();
        var color = this.getColor();
        if (spell != None) {
            tag.putString("Spell", spell.name());
            tag.putInt("Color", color);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Spell")) {
            this.setSpell(Spells.SpellList.valueOf(tag.getString("Spell")));
        }
        if (tag.contains("Color")) {
            this.setColor(tag.getInt("Color"));
        }
    }
}
