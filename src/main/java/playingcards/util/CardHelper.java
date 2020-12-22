package playingcards.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;

public class CardHelper {

    public static final String[] CARD_SKIN_NAMES = {"Classic Blue", "Classic Red", "Classic Black", "Pig Variant"};

    public static void renderItem(ItemStack stack, double offsetX, double offsetY, double offsetZ, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        matrixStack.push();
        matrixStack.translate(offsetX, offsetY, offsetZ);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
    }

    public static String getCardName(int id) {

        String type = "Ace";

        int typeID = id / 4 + 1;

        if (typeID > 1 && typeID < 11) {
            type = "" + typeID;
        }

        if (typeID > 10) {
            type = "Jack";

            if (typeID > 11) {
                type = "Queen";

                if (typeID > 12) {
                    type = "King";
                }
            }
        }

        String suite = "Spades";

        int suiteID = id % 4;

        switch(suiteID) {

            case 1: {
                suite = "Clubs";
                break;
            }

            case 2: {
                suite = "Diamonds";
                break;
            }

            case 3: {
                suite = "Hearts";
                break;
            }
        }

        return type + " of " + suite;
    }
}
