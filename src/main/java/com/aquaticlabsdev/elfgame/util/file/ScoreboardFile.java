package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Trevor/extremesnow
 * @since 10/10/2020 at 2:36 PM
 */
@Getter
public class ScoreboardFile {

    private ElfPlugin plugin;
    private File scoreboardFile;
    private FileConfiguration scoreboard;

    private ConfigurationSection scoreboardSection;

    public ScoreboardFile(ElfPlugin plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.scoreboardFile = file;
        this.scoreboard = fileConfiguration;

        scoreboardSection = scoreboard.getConfigurationSection("Scoreboards");
    }
}
