package com.slimesquared.spellcraftarmory.entity.client.armor;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SpellArmorModel extends AnimatedGeoModel<SpellArmorItem> {
    @Override
    public ResourceLocation getModelResource(SpellArmorItem armor) {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, armor.model);
    }

    @Override
    public ResourceLocation getTextureResource(SpellArmorItem armor) {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, armor.textureLocation);
    }

    @Override
    public ResourceLocation getAnimationResource(SpellArmorItem animatable) {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, "animations/armor_animation.json");
    }
}
