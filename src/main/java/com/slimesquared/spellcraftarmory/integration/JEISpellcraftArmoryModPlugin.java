package com.slimesquared.spellcraftarmory.integration;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.recipe.ModRecipes;
import com.slimesquared.spellcraftarmory.recipe.SewingTableRecipe;
import com.slimesquared.spellcraftarmory.screen.ModMenuTypes;
import com.slimesquared.spellcraftarmory.screen.SewingTableMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JeiPlugin
public class JEISpellcraftArmoryModPlugin implements IModPlugin {
    public static RecipeType<SewingTableRecipe> SEWING_TYPE =
            new RecipeType<>(SewingTableRecipeCategory.UID, SewingTableRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SewingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<SewingTableRecipe> recipesSewing = rm.getAllRecipesFor(SewingTableRecipe.Type.INSTANCE);
        registration.addRecipes(SEWING_TYPE, recipesSewing);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SEWING_TABLE.get()), SEWING_TYPE);
    }

    /*
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new SewingRecipeTransferHandler(), SEWING_TYPE);
    }
     */

    public static class SewingRecipeTransferHandler implements IRecipeTransferHandler<SewingTableMenu, SewingTableRecipe> {
        @Override
        public @NotNull Class<? extends SewingTableMenu> getContainerClass() {
            return SewingTableMenu.class;
        }

        @Override
        public @NotNull Optional<MenuType<SewingTableMenu>> getMenuType() {
            return Optional.of(ModMenuTypes.SEWING_TABLE_MENU.get());
        }

        @Override
        public @NotNull RecipeType<SewingTableRecipe> getRecipeType() {
            return SEWING_TYPE;
        }

        @Override
        public @Nullable IRecipeTransferError transferRecipe(@NotNull SewingTableMenu container, @NotNull SewingTableRecipe recipe, @NotNull IRecipeSlotsView recipeSlots, @NotNull Player player, boolean maxTransfer, boolean doTransfer) {
            if (doTransfer) {
                var focusSlot = container.getSlot(0);
                var clothSlot = container.getSlot(1);
                var templateSlot = container.getSlot(2);

            }
            return null;
        }
    }
}
