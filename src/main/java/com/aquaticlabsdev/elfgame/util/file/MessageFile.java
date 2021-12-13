package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:17
 */
@Getter @Setter
public class MessageFile {

    private ElfPlugin plugin;
    private File messagesFile;
    private FileConfiguration messages;


    private String setupWandLeftClick;
    private String setupWandRightClick;
    private String setupWandBlockAmount;



    public MessageFile(ElfPlugin plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.messagesFile = file;
        this.messages = fileConfiguration;


        setupWandLeftClick = replace("&bFirst position set to (%x%, %y%, %z%)");
        setupWandRightClick = replace("&bSecond position set to (%x%, %y%, %z%)");
        setupWandBlockAmount = replace("&b(%blocks%)");
    }


    private List<String> replace(List<String> list) {
        List<String> replaced = new ArrayList<>();
        for (String s : list) {
            replaced.add(Utils.tryReplaceHexCodes(s));
        }
        return replaced;
    }

    private String replace(String s) {
        return Utils.tryReplaceHexCodes(s);
    }

}
