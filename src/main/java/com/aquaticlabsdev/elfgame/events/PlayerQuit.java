package com.aquaticlabsdev.elfgame.events;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import org.bukkit.event.Listener;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 00:23
 */
public class PlayerQuit implements Listener {

    private final ElfPlugin plugin;

    public PlayerQuit(ElfPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }




}
