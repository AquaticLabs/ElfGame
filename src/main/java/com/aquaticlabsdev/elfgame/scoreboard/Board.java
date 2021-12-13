package com.aquaticlabsdev.elfgame.scoreboard;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.scoreboard.valuetypes.AnimatedTitle;
import com.aquaticlabsdev.elfgame.scoreboard.valuetypes.AnimationManager;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: extremesnow
 * On: 10/13/2021
 * At: 17:16
 */
public class Board {

    private final ElfPlugin plugin;
    private PlayerData player;
    @Setter
    private boolean loaded = false;
    @Getter
    private FastBoard fastBoard;
    @Getter
    @Setter
    private BukkitTask updateBoardTaskID;
    @Getter
    @Setter
    private BukkitTask updateTitleTaskID;

    @Getter
    @Setter
    private boolean isAllowed = true;
    @Getter
    @Setter
    private boolean isToggledOn = true;

    @Setter
    private String title;
    @Setter
    private AnimatedTitle animatedTitle;
    @Setter
    private List<String> values;


    public Board(ElfPlugin plugin) {
        this.plugin = plugin;
    }

    public Board(ElfPlugin plugin, PlayerData player) {
        this.plugin = plugin;
        this.player = player;
    }

    public boolean isActive() {
        return updateBoardTaskID != null;
    }

    public FastBoard get() {
        return fastBoard;
    }

    public boolean start() {
        return start(0);
    }

    public void restart() {
        remove();
        start();
    }

    public boolean start(int delay) {
        if (!isAllowed || !isToggledOn) {
            DebugLogger.logDebugMessage("Tried to start a new scoreboard task when the the board is not allowed or the players board is not toggled.");
            return false;
        }
        Player p = Objects.requireNonNull(Bukkit.getPlayer(player.getUuid()));
        if (getScoreboardTitleForPlayer(p).contains("%animation:")) {
            String s = getScoreboardTitleForPlayer(p).replace("%", "");
            String name = StringUtils.split(s, ":")[1];
            animatedTitle = (AnimatedTitle) AnimationManager.animations.get(name);
        }

        try {
            if (updateBoardTaskID != null) {
                DebugLogger.logDebugMessage("Tried to start a new scoreboard task when one is already running.");
                return true;
            }
            if (animatedTitle != null) {
                this.updateTitleTaskID = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTitle, delay, animatedTitle.getAnimationSpeed());

            } else
                this.updateTitleTaskID = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTitle, delay, 20);

            this.updateBoardTaskID = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateBoard, delay, 20);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean pause() {
        return cancel();
    }

    public boolean cancel() {
        return cancelBoard() && cancelTitle();
    }

    private boolean cancelTitle() {
        try {
            if (updateTitleTaskID == null) {
                return true;
            }
            updateTitleTaskID.cancel();

        } catch (Exception e) {
            return false;
        }
        updateTitleTaskID = null;
        return true;
    }

    private boolean cancelBoard() {
        try {
            if (updateBoardTaskID == null) {
                return true;
            }
            updateBoardTaskID.cancel();

        } catch (Exception e) {
            return false;
        }
        updateBoardTaskID = null;
        return true;
    }

    public void remove() {
        cancel();
        if (fastBoard != null) fastBoard.delete();
        fastBoard = new FastBoard(Objects.requireNonNull(Bukkit.getPlayer(player.getUuid())));
        fastBoard.delete();
        fastBoard = null;
    }

    private void updateBoard() {
        // for values
        Player p = Objects.requireNonNull(Bukkit.getPlayer(player.getUuid()));
        if (fastBoard == null) {
            fastBoard = new FastBoard(p);

        }

        values = player.replacePlaceholders(getScoreboardForPlayer(p));
        //Yup this is needed
        fastBoard.updateLines(values);

    }

    private void updateTitle() {
        // for values
        Player p = Objects.requireNonNull(Bukkit.getPlayer(player.getUuid()));
        if (fastBoard == null) {
            fastBoard = new FastBoard(p);

        }

        if (animatedTitle != null) {
            title = animatedTitle.getNextFrame();
        } else {
            title = getScoreboardTitleForPlayer(p);
        }
        title = player.replacePlaceholders(title);
        //Yup this is needed
        fastBoard.updateTitle(player.replacePlaceholders(title));

    }

    private List<String> getScoreboardForPlayer(Player player) {
        String worldName = player.getWorld().getName();
        String defaultBoard = "lobby-board";
        ConfigurationSection scoreboardSection = plugin.getFileUtil().getScoreboardFile().getScoreboardSection();

        if (scoreboardSection.getKeys(false).contains(worldName.toLowerCase())) {
            return scoreboardSection.getStringList(worldName.toLowerCase() + ".layout");
        }

        if (scoreboardSection.get(defaultBoard + ".layout") == null && scoreboardSection.get("default.layout") == null) {
            return getErrorScoreboard();
        }

        if (scoreboardSection.get(defaultBoard + ".title") != null) {
            return scoreboardSection.getStringList(defaultBoard + ".layout");
        } else {
            return scoreboardSection.getStringList("default.layout");
        }
    }

    private String getScoreboardTitleForPlayer(Player player) {
        String defaultBoard = "lobby-board";
        String worldName = player.getWorld().getName();
        ConfigurationSection configurationSection = plugin.getFileUtil().getScoreboardFile().getScoreboardSection();


        if (configurationSection.getKeys(false).contains(worldName.toLowerCase())) {
            return configurationSection.getString(worldName.toLowerCase() + ".title");
        }
        if (configurationSection.get(defaultBoard + ".title") == null && configurationSection.get("default.title") == null) {
            return "&cERROR";
        }
        if (configurationSection.get(defaultBoard + ".title") != null) {
            return configurationSection.getString(defaultBoard + ".title");
        } else {
            return configurationSection.getString("default.title");
        }
    }

    private List<String> getErrorScoreboard() {
        List<String> list = new ArrayList<>();
        list.add("&cInvalid scoreboard layout!");
        list.add("&cYou can fix this by adding a default");
        list.add("&cboard in scoreboards.yml");
        return list;
    }
}
