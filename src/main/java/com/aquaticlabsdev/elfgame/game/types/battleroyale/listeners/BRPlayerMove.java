package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @Author: extremesnow
 * On: 12/14/2021
 * At: 20:15
 */
public class BRPlayerMove implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public BRPlayerMove(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (!(gameHandler.getActiveGame() instanceof BattleRoyaleGame)) {
            return;
        }
        if (gameHandler.getActiveGame().getState() != GameState.INGAME) return;
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) return; //The player hasn't moved

        Player player = event.getPlayer();

        if (!(!player.isFlying() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid())) return;

        if (player.getInventory().getChestplate() == null) return;
        if (player.getInventory().getChestplate().getType() == Material.ELYTRA) {
            player.getInventory().setChestplate(null);
        }


    }
}
