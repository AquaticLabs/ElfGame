package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 21:46
 */
@Getter @Setter
public class MapFile {

    private File mapFile;
    private FileConfiguration mapConfig;
    private ElfPlugin plugin;

    public MapFile(ElfPlugin plugin, File file, FileConfiguration config) {
        this.plugin = plugin;
        this.mapFile = file;
        this.mapConfig = config;

    }
    public void save() {
        Utils.saveFile(mapFile, mapConfig);
    }
}
