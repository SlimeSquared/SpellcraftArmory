package com.slimesquared.spellcraftarmory.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerRuneProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerRunes> PLAYER_RUNES = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerRunes runes = null;
    private final LazyOptional<PlayerRunes> optional = LazyOptional.of(this::createPlayerRunes);

    private @NotNull PlayerRunes createPlayerRunes() {
        if (this.runes == null) {
            this.runes = new PlayerRunes();
        }

        return this.runes;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_RUNES) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerRunes().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerRunes().loadNBTData(nbt);
    }
}
