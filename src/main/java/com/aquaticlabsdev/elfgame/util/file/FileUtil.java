package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Trevor/extremesnow
 * @since 4/12/2020 at 11:43 PM
 */
public class FileUtil {


    @Getter
    @Setter
    private MessageFile messageFile;
    @Getter
    @Setter
    private ConfigFile configFile;
    @Getter
    @Setter
    private AnimationFile animationFile;
    @Getter
    @Setter
    private ScoreboardFile scoreboardFile;
    @Getter
    @Setter
    private MapFile mapFile;


    private final ElfPlugin plugin;
    private FileConfiguration custom = null;
    //private Configuration custom2 = null;
    private File customFile = null;


    public FileUtil(ElfPlugin plugin) {
        this.plugin = plugin;
    }


    public void setupConfig(String fileName) {
        customFile = new File(plugin.getDataFolder(), fileName + ".yml");
        custom = new YamlConfiguration();
        if (!customFile.exists()) {
            customFile.getParentFile().mkdirs();
            try {
                customFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            copy(plugin.getResource(fileName + ".yml"), customFile);
            loadConfig(fileName);
            loadSettingFile(fileName);
        } else {
            mergeConfig(fileName);
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(String fileName) {
        if (customFile == null) {
            customFile = new File(plugin.getDataFolder(), fileName + ".yml");
        }
        custom = YamlConfiguration.loadConfiguration(customFile);
    }

    public void loadSettingFile(String fileName) {
        if (fileName.equals("config")) {
            setConfigFile(new ConfigFile(plugin, customFile, custom));
        }
        if (fileName.equals("messages")) {
            setMessageFile(new MessageFile(plugin, customFile, custom));
        }
        if (fileName.equals("animations")) {
            setAnimationFile(new AnimationFile(plugin, customFile, custom));
        }
        if (fileName.equals("scoreboards")) {
            setScoreboardFile(new ScoreboardFile(plugin, customFile, custom));
        }
        if (fileName.equals("maps")) {
            setMapFile(new MapFile(plugin, customFile, custom));
        }

    }

    public void reloadConfig(String fileName) {
        loadConfig(fileName);
        loadSettingFile(fileName);
    }

    private void mergeConfig(String fileName) {
        try {
            loadConfig(fileName);
            FileConfiguration newConfig = new YamlConfiguration();
            Reader defConfigStream = new InputStreamReader(plugin.getResource(fileName + ".yml"), StandardCharsets.UTF_8);
            newConfig.load(defConfigStream);
            ArrayList<String> currentEntries = getAllEntries(custom);
            ArrayList<String> newEntries = getAllEntries(newConfig);

            for (String s : newEntries) {
                if (!currentEntries.contains(s)) {
                    custom.set(s, newConfig.get(s));
                }
            }

            custom.save(customFile);
            reloadConfig(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllEntries(FileConfiguration custom) {
        ArrayList<String> entries = new ArrayList<String>();
        Map<String, Object> contents = custom.getValues(true);

        for (Iterator<String> it = contents.keySet().iterator(); it.hasNext(); ) {
            String s = it.next();
            if (s != null) entries.add(s);
        }

        return entries;
    }

}
