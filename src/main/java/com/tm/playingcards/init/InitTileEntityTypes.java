package com.tm.playingcards.init;

import com.tm.playingcards.main.PCReference;
import com.tm.playingcards.tileentity.TileEntityPokerTable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PCReference.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntityPokerTable>> POKER_TABLE = TILE_ENTITY_TYPES.register("poker_table", () -> TileEntityType.Builder.create(TileEntityPokerTable::new, InitItems.POKER_TABLE.get()).build(null));
}
