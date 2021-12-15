package com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.timer.ObjectTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:29
 */
public class BRPlayerDrop implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public BRPlayerDrop(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!(gameHandler.getActiveGame() instanceof BattleRoyaleGame)) {
            return;
        }
        Item item = event.getItemDrop();
        despawnObject(item);

    }

    private void despawnObject(Item item) {
        new ObjectTimer(plugin, 60, TimeTickType.DOWN, false) {
            @Override
            public void whenComplete() {
                if (item.isValid())
                    item.remove();
            }
        }.start();
    }

}
