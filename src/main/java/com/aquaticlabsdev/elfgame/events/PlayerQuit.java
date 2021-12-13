package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 00:23
 */
public class PlayerQuit implements Listener {

    private final ElfPlugin plugin;

    public PlayerQuit(ElfPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void quitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerData(player);

        if (data.getCurrentGame() != null) {
            ElfGame game = data.getCurrentGame();

            if (game.getState() == GameState.INGAME) {
                game.getPlayersToPlay().remove(data.getUuid());
                data.setCurrentGame(null);
            }

        }

    }


}
