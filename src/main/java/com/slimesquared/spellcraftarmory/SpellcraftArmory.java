package com.slimesquared.spellcraftarmory;

import com.mojang.logging.LogUtils;
import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.block.entity.ModBlockEntities;
import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.entity.ModEntityTypes;
import com.slimesquared.spellcraftarmory.item.ModItems;
import com.slimesquared.spellcraftarmory.loot.ModLootModifiers;
import com.slimesquared.spellcraftarmory.networking.ModMessages;
import com.slimesquared.spellcraftarmory.particle.ModParticles;
import com.slimesquared.spellcraftarmory.recipe.ModRecipes;
import com.slimesquared.spellcraftarmory.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpellcraftArmory.MOD_ID)
public class SpellcraftArmory {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spellcraftarmory";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SpellcraftArmory() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        ModEffects.register(modEventBus);

        ModParticles.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();

        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModBlocks.setRendering();
            ModMenuTypes.renderScreens();
        }
    }
}
