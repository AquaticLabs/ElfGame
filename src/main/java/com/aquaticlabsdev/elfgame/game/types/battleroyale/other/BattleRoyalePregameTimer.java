package com.aquaticlabsdev.elfgame.game.types.battleroyale.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import org.bukkit.Bukkit;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 20:33
 */
public class BattleRoyalePregameTimer extends ElfTimer {

    private final ElfPlugin plugin;
    private final BattleRoyaleGame game;

    public BattleRoyalePregameTimer(ElfPlugin plugin, Runnable runnable, BattleRoyaleGame game, int time, TimeTickType tickType, boolean async) {
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
                    game.broadcastGameMessage(messageFile.getBattleRoyaleStartCountdown()
                            .replace("%seconds%", time + "")
                            .replace("%prefix%", messageFile.getBattleRoyalePrefix()));
                }
                if (time <= 5) {
                    game.broadcastGameMessage(messageFile.getBattleRoyaleStartCountdown()
                            .replace("%seconds%", time + "")
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
            }
        }, 0, 20);
    }
}


