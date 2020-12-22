package com.tm.playingcards.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tm.playingcards.entity.EntityPokerChip;
import com.tm.playingcards.item.ItemPokerChip;
import com.tm.playingcards.util.CardHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class RenderEntityPokerChip extends EntityRenderer<EntityPokerChip> {

    public RenderEntityPokerChip(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityPokerChip entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, combinedLight);

        matrixStack.push();
        matrixStack.translate(0, 0.01D, 0.07D);
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        for (byte i = 0; i < entity.getStackAmount(); i++) {
            matrixStack.push();

            Random randomX = new Random(i * 200000);
            Random randomY = new Random(i * 100000);

            matrixStack.translate(randomX.nextDouble() * 0.05D - 0.025D, 0, randomY.nextDouble() * 0.05D - 0.025D);
            matrixStack.rotate(Vector3f.XN.rotationDegrees(90));

            CardHelper.renderItem(new ItemStack(ItemPokerChip.getPokerChip(entity.getIDAt(i))), 0, 0,i * 0.032D, matrixStack, buffer, combinedLight);

            matrixStack.pop();
        }

        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityPokerChip entity) {
        return null;
    }
}
