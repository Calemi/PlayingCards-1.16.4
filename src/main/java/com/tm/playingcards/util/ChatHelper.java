package com.tm.playingcards.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ChatHelper {

    /**
     * Used to print the main mod's messages.
     * @param format The color or style of the message.
     * @param component The message.
     * @param players The Players that will receive the message.
     */
    public static void printModMessage (TextFormatting format, TranslationTextComponent component, Entity... players) {
        UnitChatMessage unitMessage = new UnitChatMessage("mod_name", players);
        unitMessage.printMessage(format, component);
    }
}
