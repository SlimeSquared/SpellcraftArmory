package com.slimesquared.spellcraftarmory.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.entity.custom.GhostHorse;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GhostHorseModel extends HorseModel<GhostHorse> {

    public static ModelLayerLocation GHOST_HORSE_LAYER = new ModelLayerLocation(new ResourceLocation(SpellcraftArmory.MOD_ID, "ghost_horse"), "ghost_horse_entity");
    private final ModelPart root;

    public GhostHorseModel(ModelPart part) {
        super(part);
        root = part;
    }

    public static LayerDefinition createLayer() {
        return LayerDefinition.create(HorseModel.createBodyMesh(CubeDeformation.NONE), 64, 64);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
