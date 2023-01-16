package com.slimesquared.spellcraftarmory.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LightParticles extends BaseAshSmokeParticle {
    protected LightParticles(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, float baseSize, SpriteSet spriteSet) {
        //level, xyz pos, xyz vel mult, xyz vel add, quadSize mult, spriteSet, random vel mult, lifetime mult, gravity, hasPhysics
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xd, yd, zd, baseSize, spriteSet, 0.2F, 16, 0, false);
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 1;
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
            return new LightParticles(level, x, y, z, dx, dy, dz, 1, this.spriteSet);
        }
    }
}
