package com.tm.playingcards.block.base;

import com.tm.playingcards.main.PlayingCards;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

/**
 * The base class for Items that place Blocks.
 */
public class BlockItemBase extends BlockItem {

    public BlockItemBase(Block block, boolean onCreativeTab) {
        super(block, onCreativeTab ? new Properties().group(PlayingCards.TAB) : new Properties());
    }

    public BlockItemBase(Block block) {
        this(block, true);
    }
}
