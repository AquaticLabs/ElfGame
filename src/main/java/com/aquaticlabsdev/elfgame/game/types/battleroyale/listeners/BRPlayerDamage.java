package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.Game;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:31
 */
public class BRPlayerDamage implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public BRPlayerDamage(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            if (!(gameHandler.getActiveGame() instanceof BattleRoyaleGame)) return;
            if (gameHandler.getActiveGame().getState() != GameState.INGAME) return;

            Player p = (Player) e.getEntity();
            PlayerData data = plugin.getPlayerData(p);

            if (data.getCurrentGame() != null && data.getCurrentGame().getState() != GameState.INGAME) return;
            BattleRoyaleGame game = (BattleRoyaleGame) gameHandler.getActiveGame();
            if (game.isGrace()) {
                e.setCancelled(true);
            }

        }
    }


}
