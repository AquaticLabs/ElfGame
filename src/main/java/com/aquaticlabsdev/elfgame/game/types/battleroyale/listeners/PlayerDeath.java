package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfgame.game.types.bombtag.BombTagGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:29
 */
public class PlayerDeath implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public PlayerDeath(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (!(gameHandler.getActiveGame() instanceof BattleRoyaleGame)) {
            return;
        }
        if (gameHandler.getActiveGame().getState() != GameState.INGAME) return;




    }

}
