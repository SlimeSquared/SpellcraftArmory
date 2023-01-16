package com.slimesquared.spellcraftarmory.particle.runeemitter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ExplodeRuneEmitter extends NoRenderParticle {
    private final Vec3 vec;
    private int life;

    protected ExplodeRuneEmitter(ClientLevel level, double xPos, double yPos, double zPos, Vec3 vec) {
        super(level, xPos, yPos, zPos);
        this.vec = vec;
    }

    @Override
    public void tick() {
        if (life == 0) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

        this.level.addParticle(ParticleTypes.FLAME, this.x, this.y, this.z,
                0.08 * (5 * this.vec.x + this.level.random.nextGaussian()),
                0.08 * (5 * this.vec.y + this.level.random.nextGaussian()),
                0.08 * (5 * this.vec.z + this.level.random.nextGaussian()));

        ++this.life;
        if (this.life >= 20) {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ExplodeRuneEmitter(level, x, y, z, new Vec3(dx, dy, dz));
        }
    }
}
