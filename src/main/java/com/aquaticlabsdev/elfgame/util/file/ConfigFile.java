package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.util.Utils;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:17
 */
@Getter
public class ConfigFile {


    private File configFile;
    private FileConfiguration config;
    private ElfPlugin plugin;

    private String mainLobbyLocation;


    public ConfigFile(ElfPlugin plugin, File file, FileConfiguration config) {
        this.plugin = plugin;
        this.configFile = file;
        this.config = config;

        this.mainLobbyLocation = config.getString("Settings.mainLobbyLocation");
    }


    public void save() {
        Utils.saveFile(configFile, config);
    }

}
