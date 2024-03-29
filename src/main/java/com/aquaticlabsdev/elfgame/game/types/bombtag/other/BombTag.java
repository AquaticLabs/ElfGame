package com.aquaticlabsdev.elfgame.game.types.bombtag.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.types.bombtag.BombTagGame;

import com.aquaticlabsdev.elfgame.util.file.MessageFile;
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

    private final BombTagGame game;

    @Getter @Setter
    private UUID taggedPlayer;

    private final int timeAmount;
    @Getter @Setter
    private int time;


    public BombTag(ElfPlugin plugin, BombTagGame game, int time) {
        this.plugin = plugin;
        this.game = game;
        this.timeAmount = time;
        this.time = time;

    }

    public void tagPlayer(UUID uuid) {
        if (taggedPlayer != null) {
            updateTag(uuid);
            return;
        }
        taggedPlayer = uuid;
        start();
    }

    public void start() {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        game.broadcastGameMessage(messageFile.getBombTagTaggedAnnounce().replace("%player_name%", Bukkit.getPlayer(taggedPlayer).getName()).replace("%prefix%", messageFile.getBombTagPrefix()));
        Bukkit.getPlayer(taggedPlayer).sendMessage(messageFile.getBombTagTaggedPlayer().replace("%prefix%", messageFile.getBombTagPrefix()));
        this.taskID = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {


            if (time == 0) {
                cancel();
                Player player = Bukkit.getPlayer(taggedPlayer);
                if (player == null) {
                    cancel();
                    return;
                }
                game.killPlayer(player);
                return;
            }
            if (time % 10 == 0 && time > 4) {
                Player player = Bukkit.getPlayer(taggedPlayer);
                if (player == null) {
                    cancel();
                    return;
                }
                game.broadcastGameMessage(messageFile.getBombTagBombCountdown()
                        .replace("%seconds%", time + "")
                        .replace("%prefix%", messageFile.getBombTagPrefix())
                        .replace("%player_name%", player.getName()));
            }
            if (time <= 5) {
                Player player = Bukkit.getPlayer(taggedPlayer);
                if (player == null) {
                    cancel();
                    return;
                }
                game.broadcastGameMessage(messageFile.getBombTagBombCountdown()
                        .replace("%seconds%", time + "")
                        .replace("%prefix%", messageFile.getBombTagPrefix())
                        .replace("%player_name%", player.getName()));
            }
            time--;
        }, 0, 20);

    }

    public void updateTag(UUID uuid) {
        this.taggedPlayer = uuid;
        Player player = Bukkit.getPlayer(taggedPlayer);
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        game.broadcastGameMessage(messageFile.getBombTagTaggedAnnounce().replace("%player_name%", player.getName()).replace("%prefix%", messageFile.getBombTagPrefix()));
        player.sendMessage(messageFile.getBombTagTaggedPlayer().replace("%prefix%", messageFile.getBombTagPrefix()));
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
