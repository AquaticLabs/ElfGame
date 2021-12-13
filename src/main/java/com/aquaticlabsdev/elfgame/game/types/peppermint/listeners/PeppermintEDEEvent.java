package com.aquaticlabsdev.elfgame.game.types.peppermint.listeners;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.peppermint.PeppermintGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 23:16
 */
public class PeppermintEDEEvent implements Listener {

    private final ElfPlugin plugin;
    private final GameHandler gameHandler;

    public PeppermintEDEEvent(ElfPlugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.gameHandler = gameHandler;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    private void entityDBEEvent(EntityDamageByEntityEvent event) {

        if (!(gameHandler.getActiveGame() instanceof PeppermintGame)) {
            return;
        }
        if (gameHandler.getActiveGame().getState() != GameState.INGAME) return;

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setDamage(0);
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            PeppermintGame game = (PeppermintGame) gameHandler.getActiveGame();

            if (attacker.getUniqueId() == game.getBombTag().getTaggedPlayer()) {
                game.getBombTag().tagPlayer(victim.getUniqueId());
            }
        }
    }
}
