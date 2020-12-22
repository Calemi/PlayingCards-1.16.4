package playingcards.block.base;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import playingcards.PlayingCards;

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
