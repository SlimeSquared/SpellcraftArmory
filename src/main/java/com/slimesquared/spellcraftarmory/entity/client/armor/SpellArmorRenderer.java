package com.slimesquared.spellcraftarmory.entity.client.armor;

import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SpellArmorRenderer extends GeoArmorRenderer<SpellArmorItem> {
    public SpellArmorRenderer() {
        super(new SpellArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }

    @Override
    public GeoArmorRenderer applySlot(EquipmentSlot slot) {
        this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelResource(this.currentArmorItem));

        setBoneVisibility(this.headBone, true);
        setBoneVisibility(this.bodyBone, true);
        setBoneVisibility(this.rightArmBone, true);
        setBoneVisibility(this.leftArmBone, true);
        setBoneVisibility(this.rightLegBone, true);
        setBoneVisibility(this.leftLegBone, true);
        setBoneVisibility(this.rightBootBone, true);
        setBoneVisibility(this.rightBootBone, true);
        setBoneVisibility(this.leftBootBone, true);

        return this;
    }
}
