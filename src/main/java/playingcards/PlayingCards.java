package playingcards;

import net.minecraft.item.ItemGroup;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import playingcards.entity.data.PCDataSerializers;
import playingcards.event.CardInteractEvent;
import playingcards.init.*;
import playingcards.packet.PacketInteractCard;
import playingcards.render.*;

@Mod(PCReference.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class PlayingCards {

    public static final ItemGroup TAB = new PCTab();
    public static PlayingCards instance;
    public static SimpleChannel network;
    public static IEventBus MOD_EVENT_BUS;

    public PlayingCards() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.addListener(this::setup);
        MOD_EVENT_BUS.addListener(this::doClientStuff);

        DataSerializers.registerSerializer(PCDataSerializers.STACK);

        InitTileEntityTypes.TILE_ENTITY_TYPES.register(MOD_EVENT_BUS);
        InitEntityTypes.ENTITY_TYPES.register(MOD_EVENT_BUS);
        InitItems.init();
        InitRecipes.RECIPES.register(MOD_EVENT_BUS);

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

        int id = 0;
        network = NetworkRegistry.newSimpleChannel(new ResourceLocation(PCReference.MOD_ID, PCReference.MOD_ID), () -> "1.0", s -> true, s -> true);
        network.registerMessage(++id, PacketInteractCard.class, PacketInteractCard::toBytes, PacketInteractCard::new, PacketInteractCard::handle);

        MinecraftForge.EVENT_BUS.register(new CardInteractEvent());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(InitEntityTypes.CARD.get(), RenderEntityCard::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntityTypes.CARD_DECK.get(), RenderEntityCardDeck::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntityTypes.POKER_CHIP.get(), RenderEntityPokerChip::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntityTypes.DICE.get(), RenderEntityDice::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntityTypes.SEAT.get(), RenderEntitySeat::new);
    }
}