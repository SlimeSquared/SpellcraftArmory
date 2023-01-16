package com.slimesquared.spellcraftarmory.block.entity;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SpellcraftArmory.MOD_ID);

    public static final RegistryObject<BlockEntityType<RuneBlockEntity>> RUNE =
            BLOCK_ENTITIES.register("rune", ()
                    -> BlockEntityType.Builder.of(RuneBlockEntity::new, ModBlocks.RUNE.get(), ModBlocks.CREEPER_RUNE.get(), ModBlocks.EXPLODE_RUNE.get(), ModBlocks.SILENCE_RUNE.get(), ModBlocks.TELEPORT_RUNE.get()).build(null));

    public static void register(IEventBus e) {
        BLOCK_ENTITIES.register(e);
    }
}
