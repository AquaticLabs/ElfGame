package com.aquaticlabsdev.elfgame.game.types.battleroyale.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.timer.ObjectTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:35
 */
public class BRRingTimer extends ObjectTimer {

    private ElfPlugin plugin;
    private BattleRoyaleGame game;
    private BattleRoyaleMap map;
    private WorldBorder border;
    private int borderSize = 200;
    private int borderSmallSize = 25;
    private int shrinkTime;

    @Getter
    private ElfTimer ringClosedTimer;


    public BRRingTimer(ElfPlugin plugin, BattleRoyaleGame game, int time, int shrinkTime,  TimeTickType type) {
        super(plugin, time, type);
        this.plugin = plugin;
        this.shrinkTime = shrinkTime;
        this.game = game;
        this.map = game.getMap();
        border = map.getRingCenterLocation().getWorld().getWorldBorder();
        border.setCenter(map.getRingCenterLocation());
        border.setSize(borderSize*2);
        border.setWarningDistance(10);

    }


    @Override
    public void start() {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();

        timerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            if (time > 59 && time % 60 == 0) {
                game.broadcastGameMessage(messageFile.getBattleRoyaleRing()
                        .replace("%time%", Utils.formatSeconds(time) + "")
                        .replace("%prefix%", messageFile.getBattleRoyalePrefix()));
            }
            if (time == 10) {
                game.broadcastGameMessage(messageFile.getBattleRoyaleRing()
                        .replace("%time%", Utils.formatSeconds(time) + "")
                        .replace("%prefix%", messageFile.getBattleRoyalePrefix()));
            }
            if (time == 5) {
                game.broadcastGameMessage(messageFile.getBattleRoyaleRing()
                        .replace("%time%", Utils.formatSeconds(time) + "")
                        .replace("%prefix%", messageFile.getBattleRoyalePrefix()));
            }
            if (getTime() <= 0) {
                stop();
                if (!async) {
                    Bukkit.getScheduler().runTask(plugin, this::whenComplete);
                    return;
                }
                whenComplete();
                return;
            }
            time--;

        }, 0, 20);
    }

    @Override
    public void whenComplete() {
        // Ring Starts Moving
        for (Player player : game.getPlayersToPlay().values()) {
            new BREffect(null, Sound.BLOCK_NOTE_BLOCK_HARP).play(player.getLocation());
        }
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        game.broadcastGameMessage(messageFile.getBattleRoyaleRingClosing()
                .replace("%prefix%", messageFile.getBattleRoyalePrefix()));

        border.setSize(borderSmallSize*2, shrinkTime);
        ringClosedTimer = new ElfTimer(plugin, () -> {
            // Ring has fully Closed.
            border.setWarningDistance(5);
            DebugLogger.logDebugMessage("Ring Has Closed.");
        }, shrinkTime, TimeTickType.DOWN);
        ringClosedTimer.start();
    }

    public void fixBorder() {
        border.setSize(10000*2);
    }
}
