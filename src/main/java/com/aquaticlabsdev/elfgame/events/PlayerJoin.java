package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 00:23
 */
public class PlayerJoin implements Listener {

    private final ElfPlugin plugin;

    public PlayerJoin(ElfPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerHolder().getOrInsert(player.getUniqueId());
        playerData.setName(player.getName());
        playerData.save();
        plugin.getGameHandler().addToGamePlayers(playerData);
    }

}
