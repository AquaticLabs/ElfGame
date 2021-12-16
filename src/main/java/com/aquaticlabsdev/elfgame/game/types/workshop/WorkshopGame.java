package com.aquaticlabsdev.elfgame.game.types.workshop;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.types.workshop.other.WorkshopTeam;
import com.aquaticlabsdev.elfgame.game.types.workshop.other.WorkshopTeamManager;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/15/2021
 * At: 20:02
 */
public class WorkshopGame extends ElfGame {

    private final ElfPlugin elfPlugin;

    private WorkshopTeamManager teamManager;
    private List<WorkshopTeam> teams;

    public WorkshopGame(ElfPlugin elfPlugin, String id) {
        super(id);
        this.elfPlugin = elfPlugin;
    }

    @Override
    public void activate() {

    }

    @Override
    public void startPregameCountdown() {

    }

    @Override
    public void start() {

    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void broadcastGameMessage(String string) {

    }

    @Override
    public void playerAbandon(Player player) {

    }
}
