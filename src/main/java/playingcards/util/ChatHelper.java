package playingcards.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;
import playingcards.PCReference;

public class ChatHelper {

    /**
     * Used to print the main mod's messages.
     * @param format The color or style of the message.
     * @param message The message.
     * @param players The Players that will receive the message.
     */
    public static void printModMessage (TextFormatting format, String message, Entity... players) {
        UnitChatMessage unitMessage = new UnitChatMessage(PCReference.MOD_NAME, players);
        unitMessage.printMessage(format, message);
    }
}
