package com.tm.playingcards.init;

import com.tm.playingcards.block.BlockBarStool;
import com.tm.playingcards.block.BlockPokerTable;
import com.tm.playingcards.item.ItemCard;
import com.tm.playingcards.item.ItemCardCovered;
import com.tm.playingcards.item.ItemCardDeck;
import com.tm.playingcards.item.ItemPokerChip;
import com.tm.playingcards.main.PCReference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.tm.playingcards.block.base.BlockItemBase;

public class InitItems {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PCReference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PCReference.MOD_ID);

    public static void init () {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //----- BLOCKS ------\\

    public static final RegistryObject<Block> POKER_TABLE = BLOCKS.register("poker_table", BlockPokerTable::new);
    public static final RegistryObject<Item> POKER_TABLE_ITEM = ITEMS.register("poker_table", () -> new BlockItemBase(POKER_TABLE.get()));

    public static final RegistryObject<Block> BAR_STOOL = BLOCKS.register("bar_stool", BlockBarStool::new);
    public static final RegistryObject<Item> BAR_STOOL_ITEM = ITEMS.register("bar_stool", () -> new BlockItemBase(BAR_STOOL.get()));

    //public static final RegistryObject<Block> CASINO_CARPET_SPACE = BLOCKS.register("casino_carpet_space", BlockCasinoCarpet::new);
    //public static final RegistryObject<Item> CASINO_CARPET_SPACE_ITEM = ITEMS.register("casino_carpet_space", () -> new BlockItemBase(CASINO_CARPET_SPACE.get()));

    //----- ITEMS ------\\

    public static final RegistryObject<Item> CARD_DECK = ITEMS.register("card_deck", ItemCardDeck::new);
    public static final RegistryObject<Item> CARD_COVERED = ITEMS.register("card_covered", ItemCardCovered::new);
    public static final RegistryObject<Item> CARD = ITEMS.register("card", ItemCard::new);

    public static final RegistryObject<Item> POKER_CHIP_WHITE = ITEMS.register("poker_chip_white", () -> new ItemPokerChip((byte)0, 1));
    public static final RegistryObject<Item> POKER_CHIP_RED = ITEMS.register("poker_chip_red", () -> new ItemPokerChip((byte)1,5));
    public static final RegistryObject<Item> POKER_CHIP_BLUE = ITEMS.register("poker_chip_blue", () -> new ItemPokerChip((byte)2,10));
    public static final RegistryObject<Item> POKER_CHIP_GREEN = ITEMS.register("poker_chip_green", () -> new ItemPokerChip((byte)3,25));
    public static final RegistryObject<Item> POKER_CHIP_BLACK = ITEMS.register("poker_chip_black", () -> new ItemPokerChip((byte)4,100));

    //public static final RegistryObject<Item> DICE_WHITE = ITEMS.register("dice_white", ItemDice::new);
}
