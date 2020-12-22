package playingcards.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import playingcards.PlayingCards;
import playingcards.item.ItemCardCovered;
import playingcards.packet.PacketInteractCard;

public class CardInteractEvent {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onLeftClick (InputEvent.MouseInputEvent event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen == null) {

            if (event.getAction() == 1 && event.getButton() == 0) {

                PlayerEntity player = mc.player;

                if (mc.world != null && player != null) {

                    ItemStack heldStack = player.getHeldItemMainhand();

                    if (heldStack.getItem() instanceof ItemCardCovered) {

                        ItemCardCovered card = (ItemCardCovered)player.getHeldItemMainhand().getItem();
                        card.flipCard(player.getHeldItemMainhand(), player);

                        PlayingCards.network.sendToServer(new PacketInteractCard("flipinv"));
                    }
                }
            }
        }
    }
}
