package com.slimesquared.spellcraftarmory.item;

import com.google.common.collect.Ordering;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ModCreativeModeTab {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("spellcraftarmorytab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.SECONDARY_WAND.get());
        }
    };
}
