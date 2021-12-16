package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:29
 */
public class PlayerRespawn implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public PlayerRespawn(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        if (gameHandler.getActiveGame() != null) {
            if (gameHandler.getActiveGame().getState() == GameState.INGAME && gameHandler.getActiveGame().getState() == GameState.POSTGAME)
                return;
        }


        Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.teleport(plugin.getGameHandler().getMainLobbyLoc());
        }, 1L);


    }
}
