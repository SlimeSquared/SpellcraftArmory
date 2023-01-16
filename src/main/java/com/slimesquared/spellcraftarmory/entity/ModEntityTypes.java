package com.slimesquared.spellcraftarmory.entity;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.entity.custom.GhostHorse;
import com.slimesquared.spellcraftarmory.entity.custom.SpellProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SpellcraftArmory.MOD_ID);
    public static void register(IEventBus e) {
        ENTITY_TYPES.register(e);
    }
    //make sure to set attributes in event bus

    public static final RegistryObject<EntityType<SpellProjectile>> SPELL_PROJECTILE = ENTITY_TYPES.register("spell_projectile", () ->
            EntityType.Builder.of((EntityType.EntityFactory<SpellProjectile>) SpellProjectile::new, MobCategory.MISC)
                    .sized(.5F, .5F).clientTrackingRange(4).updateInterval(20)
                    .build(new ResourceLocation(SpellcraftArmory.MOD_ID, "spell_projectile").toString()));
    public static final RegistryObject<EntityType<GhostHorse>> GHOST_HORSE = ENTITY_TYPES.register("ghost_horse", () ->
            EntityType.Builder.of((EntityType.EntityFactory<GhostHorse>) GhostHorse::new, MobCategory.CREATURE)
                    .sized(1.3964844F, 1.6F).clientTrackingRange(10)
                    .build(new ResourceLocation(SpellcraftArmory.MOD_ID, "ghost_horse").toString()));
}
