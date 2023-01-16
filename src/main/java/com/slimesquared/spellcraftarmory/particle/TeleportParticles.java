package com.slimesquared.spellcraftarmory.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeleportParticles extends BaseAshSmokeParticle {
    private final SpriteSet sprites;
    protected TeleportParticles(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, float baseSize, SpriteSet spriteSet) {
        //level, xyz pos, xyz vel mult, xyz vel add, quadSize mult, spriteSet, random color multiplier, lifetime mult, gravity, hasPhysics
        super(level, x, y, z, 1F, 1F, 1F, xd, yd, zd, baseSize, spriteSet, 0F, 8, 0, true);

        this.sprites = spriteSet;
        this.rCol = 0.78431F;
        this.gCol = 0.00784F;
        this.bCol = 0.81176F;
        this.friction = 0.8F;
        this.xd = (this.xd + xd) / 2;
        this.yd = (this.yd + yd) / 2;
        this.zd = (this.zd + zd) / 2;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);

            if (this.y == this.yo || this.z == this.zo) {
                this.xd *= 1.15D;
            }
            if (this.x == this.xo || this.z == this.zo) {
                this.yd *= 1.15D;
            }
            if (this.x == this.xo || this.y == this.yo) {
                this.zd *= 1.15D;
            }

            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;
        }
        this.setSpriteFromAge(this.sprites);
    }


    public void move(double xd, double yd, double zd) {
        double xd0 = xd;
        double yd0 = yd;
        double zd0 = zd;
        if (this.hasPhysics && (xd != 0.0D || yd != 0.0D || zd != 0.0D) && xd*xd + yd*yd + zd*zd < Mth.square(100.0D)) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(xd, yd, zd), this.getBoundingBox(), this.level, List.of());
            xd = vec3.x;
            yd = vec3.y;
            zd = vec3.z;
        }

        if (xd != 0.0D || yd != 0.0D || zd != 0.0D) {
            this.setBoundingBox(this.getBoundingBox().move(xd, yd, zd));
            this.setLocationFromBoundingbox();
        }

        if (yd0 != yd) {
            this.yd = 0.0D;
        }

        if (xd0 != xd) {
            this.xd = 0.0D;
        }

        if (zd0 != zd) {
            this.zd = 0.0D;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new TeleportParticles(level, x, y, z, dx, dy, dz, 1, this.spriteSet);
        }
    }
}
