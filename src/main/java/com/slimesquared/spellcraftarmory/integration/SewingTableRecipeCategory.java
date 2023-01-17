package com.slimesquared.spellcraftarmory.integration;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.recipe.SewingTableRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SewingTableRecipeCategory implements IRecipeCategory<SewingTableRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(SpellcraftArmory.MOD_ID, "sewing");
    public final static ResourceLocation TEXTURE = new ResourceLocation(SpellcraftArmory.MOD_ID, "textures/gui/jei/sewing_table_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SewingTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SEWING_TABLE.get()));
    }

    @Override
    public @NotNull RecipeType<SewingTableRecipe> getRecipeType() {
        return JEISpellcraftArmoryModPlugin.SEWING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Sewing Table");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SewingTableRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 27).addItemStack(recipe.getFocusItem().getStack());
        builder.addSlot(RecipeIngredientRole.INPUT, 57, 27).addItemStack(recipe.getClothItem().getStack());

        if (recipe.getTemplateItem().getStack().getItem() instanceof SpellArmorItem) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 47, 46).addItemStack(recipe.getTemplateItem().getStack());
        }
        else {
            builder.addSlot(RecipeIngredientRole.CATALYST, 47, 46).addItemStack(recipe.getTemplateItem().getStack()).addItemStack(recipe.getResultItem());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 37).addItemStack(recipe.getResultItem());
    }
}
