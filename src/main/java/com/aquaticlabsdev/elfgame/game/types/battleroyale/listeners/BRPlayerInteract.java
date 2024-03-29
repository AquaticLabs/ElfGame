package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:46
 */
public class BRPlayerInteract implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public BRPlayerInteract(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!(gameHandler.getActiveGame() instanceof BattleRoyaleGame)) {
            return;
        }
        if (gameHandler.getActiveGame().getState() != GameState.INGAME) return;

        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();

        if (block.getType() != Material.CHEST) return;

        BattleRoyaleGame game = (BattleRoyaleGame) gameHandler.getActiveGame();
        game.getChestsToReplace().put(block.getLocation(), block.getBlockData());

        block.setType(Material.AIR);
        game.getLootTable().dropRandomLootTableLoot(block.getLocation());

    }



}
