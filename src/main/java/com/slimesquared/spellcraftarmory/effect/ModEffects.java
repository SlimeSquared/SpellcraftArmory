package com.slimesquared.spellcraftarmory.effect;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpellcraftArmory.MOD_ID);
    public static void register(IEventBus e) {
        MOB_EFFECTS.register(e);
    }

    public static final RegistryObject<MobEffect> CURSE = MOB_EFFECTS.register("curse", () ->
            new CurseEffect(MobEffectCategory.HARMFUL, 3484199));
    public static final RegistryObject<MobEffect> PETRIFY = MOB_EFFECTS.register("petrify", () ->
            new PetrifyEffect(MobEffectCategory.HARMFUL, 1335822));
    public static final RegistryObject<MobEffect> SILENCE = MOB_EFFECTS.register("silence", () ->
            new SilenceEffect(MobEffectCategory.HARMFUL, 11809360));
}
