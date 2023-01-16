package com.slimesquared.spellcraftarmory.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.entity.custom.SpellProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SpellProjectileRenderer extends EntityRenderer<SpellProjectile> {
    private final SpellProjectileModel model;

    public SpellProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new SpellProjectileModel(ctx.bakeLayer(SpellProjectileModel.SPELL_PROJECTILE_LAYER));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SpellProjectile spellProjectileEntity) {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, "textures/entity/spell_projectile.png");
    }

    @Override
    public void render(@NotNull SpellProjectile spellProjectile, float p_114486_, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        var color = spellProjectile.getColor();
        float red = (color >> 24 & 0xFF) / 255F;
        float green = (color >> 16 & 0xFF) / 255F;
        float blue = (color >> 8 & 0xFF) / 255F;
        float alpha = (color & 0xFF) / 255F;

        poseStack.pushPose();

        if (spellProjectile.randVec == null) {
            spellProjectile.initRotation();
        }

        var rotSpeedMult = 4;
        var minSpeed = 2;
        var randVec = spellProjectile.randVec.add(minSpeed, minSpeed, minSpeed);
        double time = spellProjectile.tickCount + partialTick;

        double xRot;
        double yRot;
        double zRot;
        xRot = rotSpeedMult * time * randVec.x;
        yRot = rotSpeedMult * time * randVec.y;
        zRot = rotSpeedMult * time * randVec.z;
        if (xRot >= 360) {
            xRot -= 360;
        }
        if (yRot >= 360) {
            yRot -= 360;
        }
        if (zRot >= 360) {
            zRot -= 360;
        }

        poseStack.mulPose(Vector3f.XP.rotationDegrees( (float)xRot));
        poseStack.mulPose(Vector3f.YP.rotationDegrees( (float)yRot));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees( (float)zRot));

        poseStack.scale(3, 3, 3);

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(spellProjectile)), false, false);
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
        poseStack.popPose();
        super.render(spellProjectile, p_114486_, partialTick, poseStack, multiBufferSource, packedLight);
    }
}
