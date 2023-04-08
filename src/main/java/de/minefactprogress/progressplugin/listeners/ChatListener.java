package de.minefactprogress.progressplugin.listeners;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        User user = User.getUserByUUID(p.getUniqueId());
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());

        if(user == null) return;

        // Send chat socket
        if(user.getSetting(SettingType.MINECRAFT_MAP_VISIBLE).equals("true")) {
            JsonObject joinObj = new JsonObject();
            joinObj.addProperty("username", p.getName());
            joinObj.addProperty("uuid", p.getUniqueId().toString());
            joinObj.addProperty("message", msg);
            Main.getSocketManager().sendMessage("nyc_server_player_chat", joinObj);
        }
    }
}
