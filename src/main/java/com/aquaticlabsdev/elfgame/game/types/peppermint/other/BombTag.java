package com.aquaticlabsdev.elfgame.game.types.peppermint.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.types.peppermint.PeppermintGame;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 23:56
 */
public class BombTag {

    private final ElfPlugin plugin;
    private BukkitTask taskID = null;

    private final PeppermintGame game;

    @Getter @Setter
    private UUID taggedPlayer;

    private final int timeAmount;
    @Getter @Setter
    private int time;


    public BombTag(ElfPlugin plugin, PeppermintGame game, int time) {
        this.plugin = plugin;
        this.game = game;
        this.timeAmount = time;
        this.time = time;

    }

    public void tagPlayer(UUID uuid) {
        if (taggedPlayer != null) {
            cancel();
        }
        taggedPlayer = uuid;
        start();
    }

    public void start() {
        if (taskID != null) {
            restart();
            return;
        }
        Player player = Bukkit.getPlayer(taggedPlayer);

        System.out.println("Player: " + player.getName() + " tagged");

        this.taskID = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (time == 0) {
                game.killPlayer(player);
                cancel();
                return;
            }
            player.sendMessage("You have " + time + " seconds till you explode!");
            time--;
        }, 0, 20);

    }


    public void restart() {
        if (taskID == null) {
            start();
            return;
        }
        time = timeAmount;
    }

    public boolean cancel() {
        try {
            if (taskID == null) {
                return true;
            }
            taskID.cancel();
        } catch (Exception e) {
            return false;
        }
        taskID = null;
        return true;
    }

}
