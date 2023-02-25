package de.minefactprogress.progressplugin.listeners;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());

        // Send chat socket
        JsonObject joinObj = new JsonObject();
        joinObj.addProperty("username",p.getName());
        joinObj.addProperty("uuid",p.getUniqueId().toString());
        joinObj.addProperty("message",msg);
        Main.getSocketManager().sendMessage("chat",joinObj);
        System.out.println("chat");
    }
}
