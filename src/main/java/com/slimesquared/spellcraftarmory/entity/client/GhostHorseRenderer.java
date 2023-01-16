package com.slimesquared.spellcraftarmory.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.entity.custom.GhostHorse;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.slimesquared.spellcraftarmory.entity.client.GhostHorseModel.GHOST_HORSE_LAYER;

public class GhostHorseRenderer extends AbstractHorseRenderer<GhostHorse, HorseModel<GhostHorse>> {
    public GhostHorseRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HorseModel<>(ctx.bakeLayer(GHOST_HORSE_LAYER)), 1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GhostHorse ghostHorse) {
        return new ResourceLocation(SpellcraftArmory.MOD_ID, "textures/entity/ghost_horse.png");
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GhostHorse ghostHorse, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        return RenderType.entityTranslucent(getTextureLocation(ghostHorse));
    }

    @Override
    public void render(@NotNull GhostHorse ghostHorse, float p_115456_, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        super.render(ghostHorse, p_115456_, partialTick, poseStack, multiBufferSource, packedLight);
    }
}
