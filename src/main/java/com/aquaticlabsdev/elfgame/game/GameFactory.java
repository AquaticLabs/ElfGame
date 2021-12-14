package com.aquaticlabsdev.elfgame.game;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.types.bombtag.BombTagGame;
import com.aquaticlabsdev.elfroyal.game.ElfGame;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 01:17
 */
public class GameFactory {

    public static ElfGame createGame(ElfPlugin plugin, GameType type, String id) {
        switch (type) {
            case SPLEEF:
            default:
                return new BombTagGame(plugin, id);
        }
    }

}
