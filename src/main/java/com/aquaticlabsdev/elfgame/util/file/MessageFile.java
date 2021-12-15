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


    // BombTag
    private String bombTagPrefix;
    private List<String> bombTagMapCredits;

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
    private List<String> battleRoyaleMapCredits;
    private List<String> battleRoyaleActivated;
    private String battleRoyaleStartNaturally;
    private String battleRoyaleStartForcefully;
    private String battleRoyaleStartCountdown;
    private String battleRoyaleStarted;
    private String battleRoyaleKill;
    private String battleRoyaleKillNoKiller;
    private String battleRoyalePlayerDied;
    private String battleRoyaleRing;
    private String battleRoyaleRingClosing;
    private String battleRoyaleGracePeriod;
    private String battleRoyaleGracePeriodEnded;
    private List<String> battleRoyaleWinners;


    public MessageFile(ElfPlugin plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.messagesFile = file;
        this.messages = fileConfiguration;

        setupWandLeftClick = replace("&bFirst position set to (%x%, %y%, %z%)");
        setupWandRightClick = replace("&bSecond position set to (%x%, %y%, %z%)");
        setupWandBlockAmount = replace("&b(%blocks% total blocks)");

        bombTagPrefix = replace(fileConfiguration.getString("Games.BOMB_TAG.prefix"));
        bombTagMapCredits = replace(fileConfiguration.getStringList("Games.BOMB_TAG.map-credits"));
        bombTagActivated = replace(fileConfiguration.getStringList("Games.BOMB_TAG.activated"));
        bombTagStartNaturally = replace(fileConfiguration.getString("Games.BOMB_TAG.start"), GameType.BOMB_TAG);
        bombTagStartForcefully = replace(fileConfiguration.getString("Games.BOMB_TAG.force-start"), GameType.BOMB_TAG);
        bombTagStartCountdown = replace(fileConfiguration.getString("Games.BOMB_TAG.start-countdown"), GameType.BOMB_TAG);
        bombTagStarted = replace(fileConfiguration.getString("Games.BOMB_TAG.started"), GameType.BOMB_TAG);
        bombTagFirstPick = replace(fileConfiguration.getString("Games.BOMB_TAG.first-pick"), GameType.BOMB_TAG);
        bombTagTaggedAnnounce = replace(fileConfiguration.getString("Games.BOMB_TAG.tagged"), GameType.BOMB_TAG);
        bombTagTaggedPlayer = replace(fileConfiguration.getString("Games.BOMB_TAG.tagged-player"), GameType.BOMB_TAG);
        bombTagBombCountdown = replace(fileConfiguration.getString("Games.BOMB_TAG.tag-countdown"), GameType.BOMB_TAG);
        bombTagAnnounceBombExplode = replace(fileConfiguration.getString("Games.BOMB_TAG.announce-player-explode"), GameType.BOMB_TAG);
        bombTagBombPlayerDied = replace(fileConfiguration.getString("Games.BOMB_TAG.player-died"), GameType.BOMB_TAG);
        bombTagWinners = replace(fileConfiguration.getStringList("Games.BOMB_TAG.winners"));


        battleRoyalePrefix = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.prefix"));
        bombTagMapCredits = replace(fileConfiguration.getStringList("Games.BATTLE_ROYALE.map-credits"));
        battleRoyaleActivated = replace(fileConfiguration.getStringList("Games.BATTLE_ROYALE.activated"));
        battleRoyaleStartNaturally = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.start"), GameType.BATTLE_ROYALE);
        battleRoyaleStartForcefully = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.force-start"), GameType.BATTLE_ROYALE);
        battleRoyaleStartCountdown = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.start-countdown"), GameType.BATTLE_ROYALE);
        battleRoyaleStarted = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.prefix"), GameType.BATTLE_ROYALE);
        battleRoyaleKill = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.kill"), GameType.BATTLE_ROYALE);
        battleRoyaleKillNoKiller = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.kill-no-killer"), GameType.BATTLE_ROYALE);
        battleRoyalePlayerDied = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.player-died"), GameType.BATTLE_ROYALE);
        battleRoyaleGracePeriod = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.grace-period"), GameType.BATTLE_ROYALE);
        battleRoyaleGracePeriodEnded = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.grace-period-ended"), GameType.BATTLE_ROYALE);
        battleRoyaleRing = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.ring"), GameType.BATTLE_ROYALE);
        battleRoyaleRingClosing = replace(fileConfiguration.getString("Games.BATTLE_ROYALE.ring-closing"), GameType.BATTLE_ROYALE);
        battleRoyaleWinners = replace(fileConfiguration.getStringList("Games.BATTLE_ROYALE.winners"));

    }

    public List<String> getGameActivatedMessage(GameType type) {
        switch (type) {
            case BOMB_TAG:
                return bombTagActivated;
            case BATTLE_ROYALE:
                return battleRoyaleActivated;
            default:
                return new ArrayList<>();
        }
    }
    public String replaceGamePrefixes(String s, GameType type) {
        switch (type) {
            case BOMB_TAG:
                return s.replace("%prefix%", bombTagPrefix);
            case BATTLE_ROYALE:
                return s.replace("%prefix%", battleRoyalePrefix);
            default:
                return s;
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

    private String replace(String s, GameType gameType) {
        return replaceGamePrefixes(Utils.tryReplaceHexCodes(s), gameType);
    }

}
