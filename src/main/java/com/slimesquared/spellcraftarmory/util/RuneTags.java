package com.slimesquared.spellcraftarmory.util;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class RuneTags {
    public static BlockPos addPlayerRune(Player player, RuneSpells runeSpell, BlockPos pos) {
        var playerTag = player.serializeNBT();

        CompoundTag posTag = new CompoundTag();
        posTag.putInt("x", pos.getX());
        posTag.putInt("y", pos.getY());
        posTag.putInt("z", pos.getZ());

        BlockPos oldRunePos = null;
        if (!playerTag.getCompound(SpellcraftArmory.MOD_ID + "_runes").isEmpty()) {
            oldRunePos = new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z"));
        }

        playerTag.put(SpellcraftArmory.MOD_ID + runeSpell.name(), posTag);
        return oldRunePos;
    }

    public static BlockPos getPlayerRune(Player player, RuneSpells runeSpell) {
        var playerTag = player.serializeNBT();
        var posTag = playerTag.getCompound(SpellcraftArmory.MOD_ID + runeSpell.name());
        return new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z"));
    }
}
