package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:31
 */
public class BlockPlace implements Listener {

    private final ElfPlugin plugin;

    public BlockPlace(ElfPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    private void blockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = plugin.getPlayerData(p);
        if (playerData.isCanBuild())return;
        e.setCancelled(true);

//            if (data.getCurrentGame() != null && data.getCurrentGame().getState() != GameState.INGAME) {
//                e.setCancelled(true);
//            }
    }

}
