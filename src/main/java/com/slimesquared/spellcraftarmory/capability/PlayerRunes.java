package com.slimesquared.spellcraftarmory.capability;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import oshi.annotation.concurrent.Immutable;

import java.util.HashMap;

public class PlayerRunes {
    private HashMap<RuneSpells, DimPosition> runes = new HashMap<>();

    public DimPosition getRune(RuneSpells spell) {
        if (runes != null) {
            return runes.get(spell);
        }
        return null;
    }

    public void addRunes(RuneSpells spell, BlockPos pos, Player player) {
        var dimPos = new DimPosition(pos, player.level.dimension());
        //removes old spell, adds new one. Returns old pos if applicable.
        var oldDimPos = this.removeRunes(spell);
        //ToDo: clear player cap runes if rune block not present (i.e. broken while offline) (make server track runes)
        if (oldDimPos != null) {
            if (player.level.dimension().equals(oldDimPos.dim) && player.level.getBlockState(oldDimPos.pos).getBlock() instanceof GenericRuneBlock runeBlock) {
                runeBlock.fizzle(player, oldDimPos);
            }
        }
        this.runes.put(spell, dimPos);
    }

    public DimPosition removeRunes(RuneSpells spell) {
        if (runes != null) {
            return this.runes.remove(spell);
        }
        return null;
    }

    public void copyFrom(PlayerRunes source) {
        this.runes = source.runes;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (runes != null) {
            for (var spell : runes.keySet()) {
                var dimPos = runes.get(spell);
                CompoundTag posTag = new CompoundTag();
                posTag.putInt("x", dimPos.pos.getX());
                posTag.putInt("y", dimPos.pos.getY());
                posTag.putInt("z", dimPos.pos.getZ());

                Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, dimPos.dim).result().ifPresentOrElse(
                        (dimTag) -> posTag.put("dimension", dimTag),
                        () -> System.out.println("dimension not present!"));

                nbt.put(spell.name(), posTag);
            }
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        for (var spell : RuneSpells.values()) {
            if (spell == RuneSpells.None) {
                return;
            }
            CompoundTag posTag = nbt.getCompound(spell.name());
            try {
                if (posTag.contains("dimension")) {
                    int x = posTag.getInt("x");
                    int y = posTag.getInt("y");
                    int z = posTag.getInt("z");
                    var dim = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, posTag.get("dimension")).getOrThrow(false, (a) -> System.out.println(a));
                    var dimPos = new DimPosition(new BlockPos(x, y, z), dim);
                    runes.put(spell, dimPos);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Immutable
    public static class DimPosition {
        public final BlockPos pos;
        public final ResourceKey<Level> dim;

        DimPosition(BlockPos pos, ResourceKey<Level> dim) {
            this.pos = pos;
            this.dim = dim;
        }
    }
}
