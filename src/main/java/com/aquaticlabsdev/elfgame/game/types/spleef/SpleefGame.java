package com.aquaticlabsdev.elfgame.game.types.spleef;

import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 00:35
 */
public class SpleefGame extends ElfGame {


    private ElfPlugin plugin;
    private GameTimer gameTimer;
    private final GameType type = GameType.SPLEEF;


    public SpleefGame(ElfPlugin plugin, String id) {
        super(id);
        setState(GameState.STARTING);
        this.plugin = plugin;
        gameTimer = new ElfTimer(plugin, this,300, TimeTickType.DOWN);
    }


    public void pregameStart() {
        setState(GameState.PREGAME);
        pregameCountdown();
    }

    private void pregameCountdown() {
        GameTimer pregameTimer = new ElfTimer(plugin, this::start, 15, TimeTickType.DOWN);
        pregameTimer.start();
    }


    @Override
    public void activate() {

    }

    @Override
    public void startPregameCountdown() {

    }

    @Override
    public void start() {
        setState(GameState.INGAME);

        gameTimer.start();


        // game stuff here


    }


    @Override
    public void stop() {
        setState(GameState.ENDED);

    }

    @Override
    public void finish() {
        setState(GameState.POSTGAME);

    }
}
