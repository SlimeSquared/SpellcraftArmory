package com.slimesquared.spellcraftarmory.block;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.block.runes.CreeperSoundRune;
import com.slimesquared.spellcraftarmory.block.runes.ExplodeRune;
import com.slimesquared.spellcraftarmory.block.runes.SilenceRune;
import com.slimesquared.spellcraftarmory.block.runes.TeleportRune;
import com.slimesquared.spellcraftarmory.item.ModCreativeModeTab;
import com.slimesquared.spellcraftarmory.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpellcraftArmory.MOD_ID);

    public static void register(IEventBus e) {
        BLOCKS.register(e);
    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void setRendering() {
        setRenderType(RUNE, RenderType.cutout());
        setRenderType(CREEPER_RUNE, RenderType.cutout());
        setRenderType(EXPLODE_RUNE, RenderType.cutout());
        setRenderType(SILENCE_RUNE, RenderType.cutout());
        setRenderType(TELEPORT_RUNE, RenderType.cutout());

        setRenderType(SPECTRAL_BLOCK, RenderType.translucent());
    }
    public static void setRenderType(RegistryObject<Block> block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), renderType);
    }

    public static final RegistryObject<Block> LIGHT_SPELL = registerBlock("light_spell_block", () ->
            new LightSpellBlock(BlockBehaviour.Properties.of(Material.FIRE).instabreak().noCollission().lightLevel((light) -> 15)), null);
    public static final RegistryObject<Block> SPECTRAL_BLOCK = registerBlock("spectral_block", () ->
            new SpectralBlock(BlockBehaviour.Properties.of(new Material(MaterialColor.NONE, false, false, true, false, false, false, PushReaction.DESTROY))
                    .isSuffocating((a, b, c) -> false).instabreak().noOcclusion().lightLevel((light) -> 4)), null);
    public static final RegistryObject<Block> SEWING_TABLE = registerBlock("sewing_table", () ->
            new SewingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()), ModCreativeModeTab.MOD_TAB);

    //Setup rune json files, add to rendering method to cutout textures, add to ModBlockEntities list, add to chalk
    public static final RegistryObject<Block> RUNE = registerBlock("rune_block", () ->
            new GenericRuneBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission()), null);
    public static final RegistryObject<Block> CREEPER_RUNE = registerBlock("creeper_rune_block", () ->
            new CreeperSoundRune(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission()), null);
    public static final RegistryObject<Block> EXPLODE_RUNE = registerBlock("explode_rune_block", () ->
            new ExplodeRune(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission()), null);
    public static final RegistryObject<Block> SILENCE_RUNE = registerBlock("silence_rune_block", () ->
            new SilenceRune(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission()), null);
    public static final RegistryObject<Block> TELEPORT_RUNE = registerBlock("teleport_rune_block", () ->
            new TeleportRune(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission()), null);
}
