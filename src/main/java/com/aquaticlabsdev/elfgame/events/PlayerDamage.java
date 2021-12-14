package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
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
public class PlayerDamage implements Listener {

    private final ElfPlugin plugin;
    public PlayerDamage(ElfPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerData data = plugin.getPlayerData(p);

            if (data.getCurrentGame() != null && data.getCurrentGame().getState() != GameState.INGAME) {
                e.setCancelled(true);
            }
        }
    }


}
