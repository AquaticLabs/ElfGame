package com.aquaticlabsdev.elfgame.game;

import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 00:36
 */
public class ElfTimer extends GameTimer {

    private ElfGame game;
    private Runnable runnable;

    public ElfTimer(ElfPlugin plugin, ElfGame game, int time, TimeTickType timeTickType) {
        super(plugin, time, timeTickType, true);
        this.game = game;
    }
    public ElfTimer(ElfPlugin plugin, ElfGame game, int time, TimeTickType timeTickType, boolean async) {
        super(plugin, time, timeTickType, async);
        this.game = game;
    }
    public ElfTimer(ElfPlugin plugin, Runnable runnable, int time, TimeTickType timeTickType, boolean async) {
        super(plugin, time, timeTickType, async);
        this.runnable = runnable;
    }
    public ElfTimer(ElfPlugin plugin, Runnable runnable, int time, TimeTickType timeTickType) {
        super(plugin, time, timeTickType, true);
        this.runnable = runnable;
    }

    @Override
    public void whenComplete() {
        if (game != null) {
            game.finish();
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public int getTime() {
        return time;
    }
}
