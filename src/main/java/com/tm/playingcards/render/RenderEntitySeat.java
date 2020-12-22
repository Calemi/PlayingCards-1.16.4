package com.tm.playingcards.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tm.playingcards.entity.EntitySeat;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntitySeat extends EntityRenderer<EntitySeat> {

    public RenderEntitySeat(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntitySeat entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {}

    @Override
    public ResourceLocation getEntityTexture(EntitySeat entity) {
        return null;
    }
}
