package com.tm.playingcards.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CardHelper {

    public static final String[] CARD_SKIN_NAMES = {"card.skin.blue", "card.skin.red", "card.skin.black", "card.skin.pig"};

    public static void renderItem(ItemStack stack, double offsetX, double offsetY, double offsetZ, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        matrixStack.push();
        matrixStack.translate(offsetX, offsetY, offsetZ);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
    }

    public static IFormattableTextComponent getCardName(int id) {

        String type = "card.ace";

        int typeID = id / 4 + 1;

        if (typeID > 1 && typeID < 11) {
            type = "" + typeID;
        }

        if (typeID > 10) {
            type = "card.jack";

            if (typeID > 11) {
                type = "card.queen";

                if (typeID > 12) {
                    type = "card.king";
                }
            }
        }

        String suite = "card.spades";

        int suiteID = id % 4;

        switch(suiteID) {

            case 1: {
                suite = "card.clubs";
                break;
            }

            case 2: {
                suite = "card.diamonds";
                break;
            }

            case 3: {
                suite = "card.hearts";
                break;
            }
        }

        return new TranslationTextComponent(type).appendString(" ").append(new TranslationTextComponent("card.of").appendString(" ").append(new TranslationTextComponent(suite)));
    }
}
