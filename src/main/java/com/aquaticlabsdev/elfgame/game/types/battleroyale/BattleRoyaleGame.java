package com.aquaticlabsdev.elfgame.game.types.battleroyale;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyaleMap;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyalePregameTimer;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagMap;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagPregameTimer;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GamePlacements;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.loc.LocationType;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 20:57
 */
public class BattleRoyaleGame extends ElfGame {

    private final ElfPlugin plugin;

    private GameTimer preGameTimer;
    private GameTimer postGameTimer;
    private final GameType type = GameType.BATTLE_ROYALE;
    @Setter
    private BattleRoyaleMap map;
    @Getter
    private GamePlacements placements = new GamePlacements();

    private List<UUID> alivePlayers = new ArrayList<>();

    public BattleRoyaleGame(ElfPlugin plugin, String id) {
        super(id);
        this.plugin = plugin;
    }

    @Override
    public void activate() {
        setState(GameState.ACTIVATED);
        map.load();
        for (Map.Entry<UUID, PlayerData> entry : plugin.getGameHandler().getAvailablePlayersToPlay().entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            PlayerData data = plugin.getPlayerData(p);
            data.setCurrentGame(this);

            if (p == null || !p.isOnline()) {
                return;
            }
            getPlayersToPlay().putIfAbsent(entry.getKey(), p);
        }
        if (map == null) {
            System.out.println("Cant Start game. Picked Map Failed to load, or doesn't exist..");
            return;
        }
        teleportPlayersToGameLobby();
    }

    @Override
    public void startPregameCountdown() {
        setState(GameState.PREGAME);
        preGameTimer = new BattleRoyalePregameTimer(plugin, () -> {
            teleportPlayersToGame();
            start();
        }, this, 10, TimeTickType.DOWN, false);
        preGameTimer.start();

        System.out.println("Game: " + getGameID() + " Type: " + type.name() + " starting in " + preGameTimer.getTime() + " seconds");
        alivePlayers.addAll(getPlayersToPlay().keySet());
    }

    @Override
    public void start() {

    }

    @Override
    public String type() {
        return type.name();
    }

    @Override
    public void stop() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void broadcastGameMessage(String string) {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(string.replace("%prefix%", messageFile.getBattleRoyalePrefix()));
        }
    }

    @Override
    public void playerAbandon(Player player) {
        alivePlayers.remove(player.getUniqueId());
        getPlayersToPlay().remove(player.getUniqueId());
        checkWinner();
    }

    private void teleportPlayersToGame() {
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            player.teleport(map.getPlayerSpawn());
        }
    }

    private void teleportPlayersToGameLobby() {
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            player.teleport(map.getLobbyLocation());
        }
    }
    public void checkWinner() {
        if (alivePlayers.size() <= 1) {
            placements.addPlacement(alivePlayers.get(0), 1);
            finish();
        }
    }
}
