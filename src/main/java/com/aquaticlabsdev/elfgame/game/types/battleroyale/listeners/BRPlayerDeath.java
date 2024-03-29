package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:29
 */
public class BRPlayerDeath implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public BRPlayerDeath(ElfPlugin plugin, GameHandler gameHandler) {
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
        Player p = (Player) event.getEntity();
        PlayerData data = plugin.getPlayerData(p);
        BattleRoyaleGame game = (BattleRoyaleGame) gameHandler.getActiveGame();

        game.killPlayer(p, p.getKiller() != null ? p.getKiller() : null);

    }

}
