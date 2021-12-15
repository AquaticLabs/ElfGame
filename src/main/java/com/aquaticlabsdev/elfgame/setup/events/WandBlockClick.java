package com.aquaticlabsdev.elfgame.setup.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:27
 */
public class WandBlockClick implements Listener {

    private final ElfPlugin plugin;

    public WandBlockClick(ElfPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void blockClick(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null || e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
            return;
        }
        if (e.getHand() != EquipmentSlot.HAND) return;
        final Player player = e.getPlayer();
        MessageFile messagesFile = plugin.getFileUtil().getMessageFile();

        // WAND
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().contains("§7(Right Click to set pos2)")) {
                return;
            }
            e.setCancelled(true);
            DebugLogger.logDebugMessage("Right Block Click with tool at... x:" + e.getClickedBlock().getX() + " y: " + e.getClickedBlock().getY() + " z: " + e.getClickedBlock().getZ());

            PlayerData playerData = plugin.getPlayerData(e.getPlayer());
            playerData.setLocation2(e.getClickedBlock().getLocation());

            if (playerData.getLocation1() != null) {
                Selection selection = new Selection(playerData.getLocation1(), playerData.getLocation2());
                playerData.setSelection(selection);
                player.sendMessage(messagesFile.getSetupWandRightClick().replace("%x%", e.getClickedBlock().getX() + "").replace("%y%", e.getClickedBlock().getY() + "").replace("%z%", e.getClickedBlock().getZ() + "") + " " + messagesFile.getSetupWandBlockAmount().replace("%blocks%", Utils.addCommas(selection.blockCount()) + ""));
                return;
            }
            player.sendMessage(messagesFile.getSetupWandRightClick().replace("%x%", e.getClickedBlock().getX() + "").replace("%y%", e.getClickedBlock().getY() + "").replace("%z%", e.getClickedBlock().getZ() + ""));

        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().contains("§7(Right Click to set pos2)")) {
                return;
            }
            e.setCancelled(true);
            DebugLogger.logDebugMessage("Left Block Click with tool at... x:" + e.getClickedBlock().getX() + " y: " + e.getClickedBlock().getY() + " z: " + e.getClickedBlock().getZ());
            PlayerData playerData = plugin.getPlayerData(e.getPlayer());
            playerData.setLocation1(e.getClickedBlock().getLocation());
            if (playerData.getLocation2() != null) {
                Selection selection = new Selection(playerData.getLocation1(), playerData.getLocation2());
                playerData.setSelection(selection);
                player.sendMessage(messagesFile.getSetupWandLeftClick().replace("%x%", e.getClickedBlock().getX() + "").replace("%y%", e.getClickedBlock().getY() + "").replace("%z%", e.getClickedBlock().getZ() + "") + " " + messagesFile.getSetupWandBlockAmount().replace("%blocks%", Utils.addCommas(selection.blockCount()) + ""));
                return;
            }
            player.sendMessage(messagesFile.getSetupWandLeftClick().replace("%x%", e.getClickedBlock().getX() + "").replace("%y%", e.getClickedBlock().getY() + "").replace("%z%", e.getClickedBlock().getZ() + ""));
        }
    }


}
