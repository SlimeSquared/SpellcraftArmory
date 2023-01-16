package com.slimesquared.spellcraftarmory.screen;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SpellcraftArmory.MOD_ID);

    public static void register(IEventBus e) {
        MENUS.register(e);
    }

    public static final RegistryObject<MenuType<SewingTableMenu>> SEWING_TABLE_MENU =
            MENUS.register("sewing_table_menu", () -> new MenuType<>(SewingTableMenu::new));

    @OnlyIn(Dist.CLIENT)
    public static void renderScreens() {
        MenuScreens.register(SEWING_TABLE_MENU.get(), SewingTableScreen::new);
    }
}
