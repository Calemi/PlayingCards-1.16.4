package playingcards.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import playingcards.item.ItemCardCovered;

import java.util.function.Supplier;

public class PacketInteractCard {

    private String command;

    public PacketInteractCard () {}

    public PacketInteractCard (String command) {
        this.command = command;
    }

    public PacketInteractCard (PacketBuffer buf) {
        command = buf.readString(11).trim();
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeString(command, 11);
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {

                if (command.equalsIgnoreCase("flipinv")) {

                    Item item = player.getHeldItemMainhand().getItem();

                    if (item instanceof ItemCardCovered) {
                        ItemCardCovered card = (ItemCardCovered)player.getHeldItemMainhand().getItem();
                        card.flipCard(player.getHeldItemMainhand(), player);
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
