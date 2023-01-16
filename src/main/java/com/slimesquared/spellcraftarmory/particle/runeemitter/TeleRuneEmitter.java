package com.slimesquared.spellcraftarmory.particle.runeemitter;

import com.slimesquared.spellcraftarmory.particle.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class TeleRuneEmitter extends NoRenderParticle {
    private final Vec3 vec;
    protected TeleRuneEmitter(ClientLevel level, double xPos, double yPos, double zPos, Vec3 vec) {
        super(level, xPos, yPos, zPos);
        this.vec = vec;
    }

    @Override
    public void tick() {
        this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F, false);

        for (int i = 0; i < 16; i++) {
            var scaledTeleVec = this.vec.scale(this.level.random.nextFloat());
            this.level.addParticle(ModParticles.TELEPORT_PARTICLES.get(),
                    this.x + 0.4 * this.level.random.nextGaussian(),
                    this.y + 1.8 * this.level.random.nextFloat(),
                    this.z + 0.4 * this.level.random.nextGaussian(),
                    scaledTeleVec.x, scaledTeleVec.y, scaledTeleVec.z);
        }

        this.remove();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new TeleRuneEmitter(level, x, y, z, new Vec3(dx, dy, dz));
        }
    }
}
