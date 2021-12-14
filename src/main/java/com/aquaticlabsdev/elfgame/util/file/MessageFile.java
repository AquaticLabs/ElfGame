package com.aquaticlabsdev.elfgame.util.file;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameType;
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

    private List<String> mapCredits;

    // BombTag
    private String bombTagPrefix;
    private List<String> bombTagActivated;
    private String bombTagStartNaturally;
    private String bombTagStartForcefully;
    private String bombTagStartCountdown;
    private String bombTagStarted;
    private String bombTagFirstPick;
    private String bombTagTaggedAnnounce;
    private String bombTagTaggedPlayer;
    private String bombTagBombCountdown;
    private String bombTagAnnounceBombExplode;
    private String bombTagBombPlayerDied;
    private List<String> bombTagWinners;

    // Battle Royale
    private String battleRoyalePrefix;
    private List<String> battleRoyaleActivated;
    private String battleRoyaleStartNaturally;
    private String battleRoyaleStartForcefully;
    private String battleRoyaleStartCountdown;
    private String battleRoyaleStarted;
    private List<String> battleRoyaleWinners;






    public MessageFile(ElfPlugin plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.messagesFile = file;
        this.messages = fileConfiguration;

        setupWandLeftClick = replace("&bFirst position set to (%x%, %y%, %z%)");
        setupWandRightClick = replace("&bSecond position set to (%x%, %y%, %z%)");
        setupWandBlockAmount = replace("&b(%blocks%)");

        bombTagPrefix = replace(fileConfiguration.getString("Games.BOMB_TAG.prefix"));
        bombTagActivated = replace(fileConfiguration.getStringList("Games.BOMB_TAG.activated"));
        bombTagStartNaturally = replace(fileConfiguration.getString("Games.BOMB_TAG.start"));
        bombTagStartForcefully = replace(fileConfiguration.getString("Games.BOMB_TAG.force-start"));
        bombTagStartCountdown = replace(fileConfiguration.getString("Games.BOMB_TAG.start-countdown"));
        bombTagStarted = replace(fileConfiguration.getString("Games.BOMB_TAG.started"));
        bombTagFirstPick = replace(fileConfiguration.getString("Games.BOMB_TAG.first-pick"));
        bombTagTaggedAnnounce = replace(fileConfiguration.getString("Games.BOMB_TAG.tagged"));
        bombTagTaggedPlayer = replace(fileConfiguration.getString("Games.BOMB_TAG.tagged-player"));
        bombTagBombCountdown = replace(fileConfiguration.getString("Games.BOMB_TAG.tag-countdown"));
        bombTagAnnounceBombExplode = replace(fileConfiguration.getString("Games.BOMB_TAG.announce-player-explode"));
        bombTagBombPlayerDied = replace(fileConfiguration.getString("Games.BOMB_TAG.player-died"));
        bombTagWinners = replace(fileConfiguration.getStringList("Games.BOMB_TAG.winners"));

    }

    public List<String> getGameActivatedMessage(String type) {
        switch (type) {
            case "BOMB_TAG":
                return bombTagActivated;
            default:
                return new ArrayList<>();
        }
    }

    private List<String> replace(List<String> list) {
        List<String> replaced = new ArrayList<>();
        for (String s : list) {
            replaced.add(replace(s));
        }
        return replaced;
    }

    private String replace(String s) {

        return Utils.tryReplaceHexCodes(s);
    }

}
