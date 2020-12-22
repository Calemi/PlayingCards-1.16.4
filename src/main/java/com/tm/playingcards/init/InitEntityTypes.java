package com.tm.playingcards.init;

import com.tm.playingcards.entity.*;
import com.tm.playingcards.main.PCReference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, PCReference.MOD_ID);

    public static final RegistryObject<EntityType<EntityCard>> CARD = ENTITY_TYPES.register("card", () -> EntityType.Builder.<EntityCard>create(EntityCard::new, EntityClassification.MISC).size(0.5F, 0.5F).build(new ResourceLocation(PCReference.MOD_ID, "card").toString()));
    public static final RegistryObject<EntityType<EntityCardDeck>> CARD_DECK = ENTITY_TYPES.register("card_deck", () -> EntityType.Builder.<EntityCardDeck>create(EntityCardDeck::new, EntityClassification.MISC).size(0.5F, 0.5F).build(new ResourceLocation(PCReference.MOD_ID, "card_deck").toString()));
    public static final RegistryObject<EntityType<EntityPokerChip>> POKER_CHIP = ENTITY_TYPES.register("poker_chip", () -> EntityType.Builder.<EntityPokerChip>create(EntityPokerChip::new, EntityClassification.MISC).size(0.3F, 0.3F).build(new ResourceLocation(PCReference.MOD_ID, "poker_chip").toString()));
    public static final RegistryObject<EntityType<EntityDice>> DICE = ENTITY_TYPES.register("dice", () -> EntityType.Builder.<EntityDice>create(EntityDice::new, EntityClassification.MISC).size(0.3F, 0.3F).build(new ResourceLocation(PCReference.MOD_ID, "dice").toString()));
    public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITY_TYPES.register("seat", () -> EntityType.Builder.<EntitySeat>create(EntitySeat::new, EntityClassification.MISC).size(0, 0).build(new ResourceLocation(PCReference.MOD_ID, "seat").toString()));
}
