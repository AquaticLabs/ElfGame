package com.aquaticlabsdev.elfgame.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:33
 */
public enum Permission {


    ADMIN("elfroyal.admin", null),

    FORCE_START("elfroyal.admin.forcestart", ADMIN),

    SETUP_MAP("elfroyal.admin.setupmap", ADMIN)

    ;



    private final String node;
    private final Permission master;

    Permission(String node, Permission master) {
        this.node = node;
        this.master = master;
    }

    public String getNode() {
        return node;
    }

    public Permission getMaster() {
        return master;
    }

    public static boolean hasPermission(Player player, Permission permission) {
        if (player.hasPermission(ADMIN.getNode())) return true;
        if (permission.getMaster() != null) {
            if (player.hasPermission(permission.getMaster().getNode())) return true;
        }
        return player.hasPermission(permission.getNode());
    }

    public static boolean hasPermission(CommandSender sender, Permission permission) {
        if (sender.hasPermission(ADMIN.getNode())) return true;
        if (permission.getMaster() != null) {
            if (sender.hasPermission(permission.getMaster().getNode())) return true;
        }
        return sender.hasPermission(permission.getNode());
    }

}
