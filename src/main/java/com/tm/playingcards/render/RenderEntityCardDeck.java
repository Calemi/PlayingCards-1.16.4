package com.tm.playingcards.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.util.CardHelper;
import com.tm.playingcards.util.ItemHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import com.tm.playingcards.entity.EntityCardDeck;

public class RenderEntityCardDeck extends EntityRenderer<EntityCardDeck> {

    public RenderEntityCardDeck(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityCardDeck entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, combinedLight);

        ItemStack cardStack = new ItemStack(InitItems.CARD_COVERED.get());
        ItemHelper.getNBT(cardStack).putByte("SkinID", entity.getSkinID());

        matrixStack.push();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-entity.getRotation() + 180));
        matrixStack.scale(1.5F, 1.5F, 1.5F);

        for (byte i = 0; i < entity.getStackAmount() + 2; i++) {
            CardHelper.renderItem(cardStack, 0, i * 0.003D, 0, matrixStack, buffer, combinedLight);
        }

        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCardDeck entity) {
        return null;
    }
}
