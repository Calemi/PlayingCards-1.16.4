package com.tm.playingcards.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class UnitChatMessage {

    private final String unitName;
    private final Entity[] players;

    public UnitChatMessage(String unitName, Entity... players) {
        this.unitName = unitName;
        this.players = players;
    }

    public void printMessage(TextFormatting format, TranslationTextComponent message) {

        for (Entity player : players) {
            player.sendMessage(new StringTextComponent("[").mergeStyle(TextFormatting.WHITE).append(getUnitName().appendString("] ")).append(message.mergeStyle(format)), Util.DUMMY_UUID);
        }
    }

    private TranslationTextComponent getUnitName() {
        return new TranslationTextComponent("unitname." + unitName);
    }

}
