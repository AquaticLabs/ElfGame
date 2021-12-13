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
public class AnimationFile {

    private ElfPlugin plugin;
    private File animationFile;
    private FileConfiguration animations;

    private ConfigurationSection animationSection;


    public AnimationFile(ElfPlugin plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.animationFile = file;
        this.animations = fileConfiguration;

        animationSection = animations.getConfigurationSection("Animations.Title");

    }
}
