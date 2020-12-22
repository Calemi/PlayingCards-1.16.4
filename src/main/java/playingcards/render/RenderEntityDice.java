package playingcards.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import playingcards.entity.EntityDice;

public class RenderEntityDice extends EntityRenderer<EntityDice> {

    public RenderEntityDice(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityDice entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, combinedLight);

        //Push
        matrixStack.push();

        //Translate
        matrixStack.translate(0, 0.15D, 0);

        //Scale
        //matrixStack.func_227862_a_(0.6F, 0.6F, 0.6F);

        //CardHelper.renderItem(new ItemStack(InitItems.DICE_WHITE.get()), 0, 0,0, matrixStack, buffer, combinedLight);

        //Pop
        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityDice entity) {
        return null;
    }
}
