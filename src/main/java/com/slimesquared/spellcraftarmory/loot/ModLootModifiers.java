package com.slimesquared.spellcraftarmory.loot;

import com.mojang.serialization.Codec;
import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SpellcraftArmory.MOD_ID);

    public static void register(IEventBus e) {
        LOOT_MODIFIER_SERIALIZERS.register(e);
    }

    //Very Common: 1/5  Common: 1/10  Medium: 1/20  Rare: 1/33
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_VERY_COMMON_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_very_common_item", AddVeryCommonItem.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_COMMON_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_common_item", AddCommonItem.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_MEDIUM_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_medium_item", AddMediumItem.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_RARE_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_rare_item", AddRareItem.CODEC);
}
