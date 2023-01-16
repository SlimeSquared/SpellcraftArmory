package com.slimesquared.spellcraftarmory.recipe;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SpellcraftArmory.MOD_ID);

    public static void register(IEventBus e) {
        SERIALIZERS.register(e);
    }

    public static final RegistryObject<RecipeSerializer<SewingTableRecipe>> SEWING_SERIALIZER =
            SERIALIZERS.register("sewing", () -> SewingTableRecipe.Serializer.INSTANCE);
}
