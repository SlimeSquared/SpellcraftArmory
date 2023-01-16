package com.slimesquared.spellcraftarmory.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;


public enum RuneSpells implements StringRepresentable {
    CreeperSound("creeper_sound"),
    Explode("explode"),
    Silence("silence"),
    Teleport("teleport"),

    None("none");


    private final String name;

    RuneSpells(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }

    public static int getCooldown(RuneSpells runeSpell) {
        int cooldown = 0;
        switch (runeSpell) {
            case CreeperSound -> cooldown = 10;
            case Explode -> cooldown = 400;
            case Silence -> cooldown = 200;
            case Teleport -> cooldown = 400;
        }
        //remove later
        if (cooldown > 25) {
            cooldown = 25;
        }
        return cooldown;
    }
}
