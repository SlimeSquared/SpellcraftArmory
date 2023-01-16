package com.slimesquared.spellcraftarmory.block.entity;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.runecapability.PlayerRuneProvider;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RuneBlockEntity extends BlockEntity {
    public RuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNE.get(), pos, state);
    }

    private UUID ownerUUID;
    private RuneSpells spell;
    private Integer timer;

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    public RuneSpells getRuneSpell() {
        return this.spell;
    }
    public Integer getTimer() {
        return this.timer == null ? -1 : this.timer;
    }
    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }
    public void setRuneSpell(RuneSpells spell) {
        this.spell = spell;
    }
    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        if (this.ownerUUID != null) {
            nbt.putUUID("rune.owner", this.ownerUUID);
        }
        nbt.putString("rune.spell", this.spell == null ? RuneSpells.None.name() : this.spell.name());
        nbt.putInt("rune.timer", this.timer == null ? -1 : this.timer);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        try {
            if (nbt.hasUUID("rune.owner")) {
                this.ownerUUID = nbt.getUUID("rune.owner");
            }
            this.spell = RuneSpells.valueOf(nbt.getString("rune.spell"));
            this.timer = nbt.getInt("rune.timer");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RuneBlockEntity runeEntity) {
        var player = runeEntity.ownerUUID != null ? level.getPlayerByUUID(runeEntity.ownerUUID) : null;
        if (level.isClientSide) {
            if (runeEntity.ownerUUID != null && level.getBlockState(pos).getBlock() instanceof GenericRuneBlock rune) {
                rune.effectsTick(state, level, pos, level.random);
            }
        }
        else {
            if ((runeEntity.ownerUUID == null) != (runeEntity.spell == RuneSpells.None)) {
                runeEntity.spell = RuneSpells.None;
                runeEntity.ownerUUID = null;
                level.setBlockAndUpdate(pos, ModBlocks.RUNE.get().defaultBlockState()
                        .setValue(GenericRuneBlock.FACING, state.getValue(GenericRuneBlock.FACING))
                        .setValue(GenericRuneBlock.FACE, state.getValue(GenericRuneBlock.FACE)));
                System.out.println(pos + " rune set to blank");
            }
            if (runeEntity.timer != null && runeEntity.timer > -1 && player != null
                    && level.getBlockState(pos).getBlock() instanceof GenericRuneBlock rune) {
                runeEntity.timer--;
                if (runeEntity.timer == 0) {
                    rune.timerActivate(player, pos);
                }
            }
        }
        if (player != null && runeEntity.spell != RuneSpells.None) {
            player.getCapability(PlayerRuneProvider.PLAYER_RUNES).ifPresent((runes) -> {
                if (runes.getRune(runeEntity.spell).dim.equals(level.dimension())
                        && !runes.getRune(runeEntity.spell).pos.equals(pos)
                        && level.getBlockState(pos).getBlock() instanceof GenericRuneBlock rune) {
                    rune.unsafeFizzle(level, pos, runeEntity);
                    runeEntity.spell = RuneSpells.None;
                    runeEntity.ownerUUID = null;
                }
            });
        }
    }
}
