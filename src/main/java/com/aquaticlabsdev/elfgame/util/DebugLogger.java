package com.aquaticlabsdev.elfgame.util;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:10
 */
public class DebugLogger {

    @Setter
    private static boolean debug = true;

    public static void logDebugMessage(String message) {
        if (debug) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static void logConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
