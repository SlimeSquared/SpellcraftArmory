package com.slimesquared.spellcraftarmory.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SewingTableRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final StackIngredient focusItem;
    private final StackIngredient clothItem;
    private final StackIngredient templateItem;

    public SewingTableRecipe(ResourceLocation id, ItemStack output, NonNullList<StackIngredient> inputs) {
        this.id = id;
        this.output = output;
        this.focusItem = inputs.get(0);
        this.clothItem = inputs.get(1);
        this.templateItem = inputs.get(2);
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (level.isClientSide) {
            return false;
        }

        return focusItem.test(container.getItem(0))
                && clothItem.test(container.getItem(1))
                && (templateItem.test(container.getItem(2)) || output.getItem() == container.getItem(2).getItem());
    }

    @Override
    public ItemStack assemble(Container container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public StackIngredient getFocusItem() {
        return focusItem;
    }
    public StackIngredient getClothItem() {
        return clothItem;
    }
    public StackIngredient getTemplateItem() {
        return templateItem;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SewingTableRecipe> {
        private Type() {}
        public static Type INSTANCE = new Type();
        public static final String ID = "sewing";
    }

    public static class Serializer implements RecipeSerializer<SewingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(SpellcraftArmory.MOD_ID, "sewing");

        @Override
        public SewingTableRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<StackIngredient> inputs = NonNullList.withSize(3, StackIngredient.EMPTY);

            //focus first, cloth second, template third
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, StackIngredient.fromJson(ingredients.get(i).getAsJsonObject()));
            }

            return new SewingTableRecipe(recipeId, output, inputs);
        }

        @Override
        public @Nullable SewingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            throw new UnsupportedOperationException("sewing recipes from network not supported");
            /*
            NonNullList<FocusIngredient> inputs = NonNullList.withSize(buf.readInt(), FocusIngredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new SewingTableRecipe(id, output, inputs);
             */
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SewingTableRecipe recipe) {
            throw new UnsupportedOperationException("sewing recipes to network not supported");
            /*
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
             */
        }
    }

    public static class StackIngredient implements Predicate<ItemStack> {
        public static final StackIngredient EMPTY = new StackIngredient(ItemStack.EMPTY);
        private final ItemStack stack;

        public StackIngredient(ItemStack stack) {
            this.stack = stack;
        }

        public ItemStack getStack() {
            return stack;
        }

        public static StackIngredient fromJson(JsonObject json) {
            ResourceLocation itemId = new ResourceLocation(json.get("item").getAsString());

            var item = ForgeRegistries.ITEMS.getValue(itemId);
            int count = 1;
            if (json.has("count")) {
                count = json.get("count").getAsInt();
            }

            return new StackIngredient(new ItemStack(item, count));
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && itemStack.getItem() == this.stack.getItem()
                    && this.stack.getCount() <= itemStack.getCount();
        }
    }
}
