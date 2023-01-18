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

public class PlayerSpellCooldownProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSpellCooldowns> PLAYER_SPELL_COOLDOWNS = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerSpellCooldowns spellCooldowns = null;
    private final LazyOptional<PlayerSpellCooldowns> optional = LazyOptional.of(this::createPlayerSpellCooldowns);

    private @NotNull PlayerSpellCooldowns createPlayerSpellCooldowns() {
        if (this.spellCooldowns == null) {
            this.spellCooldowns = new PlayerSpellCooldowns();
        }

        return this.spellCooldowns;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SPELL_COOLDOWNS) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSpellCooldowns().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSpellCooldowns().loadNBTData(nbt);
    }
}
