package com.aquaticlabsdev.elfgame.scoreboard.valuetypes;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 10/15/2021
 * At: 22:18
 */
public class AnimationManager {

    private final ElfPlugin plugin;
    public static Map<String, Animation> animations;

    public AnimationManager(ElfPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        animations = new HashMap<>();
        //for all animations
        ConfigurationSection animationSection = plugin.getFileUtil().getAnimationFile().getAnimationSection();
        for (String animationName : animationSection.getKeys(false)) {
            List<String> animationValues = animationSection.getStringList(animationName + ".animation");
            int speed = animationSection.getInt(animationName + ".animationSpeed");
            AnimatedTitle title = new AnimatedTitle(animationName, animationValues);
            title.setAnimationSpeed(speed);
            animations.put(animationName, title);
        }
    }

}
