package playingcards.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import playingcards.PCReference;
import playingcards.tileentity.TileEntityPokerTable;

public class InitTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, PCReference.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntityPokerTable>> POKER_TABLE = TILE_ENTITY_TYPES.register("poker_table", () -> TileEntityType.Builder.create(TileEntityPokerTable::new, InitItems.POKER_TABLE.get()).build(null));
}
