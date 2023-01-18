package com.slimesquared.spellcraftarmory.capability;

import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public class PlayerSpellCooldowns {
    private HashMap<Spells.SpellList, Integer> spellCooldowns = new HashMap<>();

    public Integer getCooldown(Spells.SpellList spell) {
        if (spellCooldowns != null) {
            return spellCooldowns.get(spell) != null ? spellCooldowns.get(spell) : 0;
        }
        return 0;
    }


    //decrements 40x per second
    public void setCooldown(Spells.SpellList spell, Integer cooldown) {
        this.removeCooldown(spell);
        this.spellCooldowns.put(spell, cooldown);
    }

    public void removeCooldown(Spells.SpellList spell) {
        if (spellCooldowns != null) {
            spellCooldowns.remove(spell);
        }
    }

    //currently unused, cooldowns reset on death
    public void copyFrom(PlayerSpellCooldowns source) {
        this.spellCooldowns = source.spellCooldowns;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (spellCooldowns != null) {
            for (var spell : Spells.SpellList.values()) {
                var cooldown = getCooldown(spell);
                nbt.putInt(spell.name(), cooldown);
            }
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        for (var spell : Spells.SpellList.values()) {
            if (spell == Spells.SpellList.None) {
                return;
            }
            try {
                spellCooldowns.put(spell, nbt.getInt(spell.name()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
