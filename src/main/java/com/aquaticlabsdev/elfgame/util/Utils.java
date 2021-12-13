package com.aquaticlabsdev.elfgame.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:00
 */
public class Utils {

    private static final Pattern HEX_REGEX = Pattern.compile("#[0-9A-Fa-f]{6}|#[0-9A-Fa-f]{3}");

    public static String tryReplaceHexCodes(String input) {
        Version version = new Version(getVersion());
        if (version.compareTo(new Version("1.16.0")) > 0) {
            final Matcher matcher = HEX_REGEX.matcher(input);
            final StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group()).toString());
            }
            return matcher.appendTail(buffer).toString().replace("&", "§");
        }
        return input.replace("&", "§");
    }

    public static String getVersion() {
        String bukkitver = Bukkit.getServer().getVersion();
        String mcver = "1.0.0";
        int idx = bukkitver.indexOf("(MC: ");
        if (idx > 0) {
            mcver = bukkitver.substring(idx + 5);
            idx = mcver.indexOf(')');
            if (idx > 0) mcver = mcver.substring(0, idx);
        }
        return mcver;
    }
    public static void saveFile(File file, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ItemStack wandItem() {
        ItemStack i = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta m = i.getItemMeta();
        assert m != null;
        List<String> lore;
        lore = new ArrayList<>();

        m.setDisplayName("§cSetup Tool");
        lore.add("§7(Left Click to set pos1)");
        lore.add("§7(Right Click to set pos2)");
        m.setLore(lore);
        i.setItemMeta(m);
        return i;
    }
    public static float faceToYaw(BlockFace face) {
        switch (face) {
            case NORTH_NORTH_EAST:
                return -157.5f;
            case NORTH_EAST:
                return -135;
            case EAST_NORTH_EAST:
                return -112.5f;
            case EAST:
                return -90;
            case EAST_SOUTH_EAST:
                return -67.5f;
            case SOUTH_EAST:
                return -45;
            case SOUTH_SOUTH_EAST:
                return -22.5f;
            case SOUTH:
                return 0;
            case SOUTH_SOUTH_WEST:
                return 22.5f;
            case SOUTH_WEST:
                return 45;
            case WEST_SOUTH_WEST:
                return 67.5f;
            case WEST:
                return 90;
            case WEST_NORTH_WEST:
                return 112.5f;
            case NORTH_WEST:
                return 135;
            case NORTH_NORTH_WEST:
                return 157.5f;
            default:
                return 180;
        }
    }
}
