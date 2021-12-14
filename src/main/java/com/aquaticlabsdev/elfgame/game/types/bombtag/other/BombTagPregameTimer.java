package com.aquaticlabsdev.elfgame.game.types.bombtag.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.types.bombtag.BombTagGame;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import org.bukkit.Bukkit;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 20:33
 */
public class BombTagPregameTimer extends ElfTimer {

    private final ElfPlugin plugin;
    private final BombTagGame game;

    public BombTagPregameTimer(ElfPlugin plugin, Runnable runnable, BombTagGame game, int time, TimeTickType tickType, boolean async) {
        super(plugin, runnable, time, tickType, async);
        this.plugin = plugin;
        this.game = game;
    }

    @Override
    public void start() {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();

        timerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            if (timeTickType == TimeTickType.UP) {

                if (time >= topTime) {
                    stop();
                    whenComplete();
                    return;
                }

                time++;
            } else {
                if (time % 10 == 0 && time > 4) {
                    game.broadcastGameMessage(messageFile.getBombTagStartCountdown()
                            .replace("%seconds%", time + "")
                            .replace("%prefix%", messageFile.getBombTagPrefix()));
                }
                if (time <= 5) {
                    game.broadcastGameMessage(messageFile.getBombTagStartCountdown()
                            .replace("%seconds%", time + "")
                            .replace("%prefix%", messageFile.getBombTagPrefix()));
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
            }
        }, 0, 20);
    }
}


