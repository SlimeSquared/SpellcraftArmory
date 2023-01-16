package com.slimesquared.spellcraftarmory.particle;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SpellcraftArmory.MOD_ID);
    public static void register(IEventBus e) {
        PARTICLE_TYPES.register(e);
    }

    //make sure to register in ModEvents as well
    public static final RegistryObject<SimpleParticleType> LIGHT_PARTICLES = PARTICLE_TYPES.register("light_particles", () ->
            new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> TELEPORT_PARTICLES = PARTICLE_TYPES.register("teleport_particles", () ->
            new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> TELE_RUNE_EMITTER = PARTICLE_TYPES.register("tele_rune_emitter", () ->
            new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> EXPLODE_RUNE_EMITTER = PARTICLE_TYPES.register("explode_rune_emitter", () ->
            new SimpleParticleType(true));
}
