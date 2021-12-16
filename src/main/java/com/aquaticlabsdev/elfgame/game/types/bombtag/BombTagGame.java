package com.aquaticlabsdev.elfgame.game.types.bombtag;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BTEffect;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTag;
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
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:48
 */
public class BombTagGame extends ElfGame {

    private final ElfPlugin plugin;
    @Getter
    private GameTimer preGameTimer;
    @Getter
    private GameTimer postGameTimer;
    private final GameType type = GameType.BOMB_TAG;
    @Setter
    private BombTagMap map;
    @Getter
    private BombTag bombTag;
    @Getter
    private GamePlacements placements = new GamePlacements();

    @Getter
    private List<UUID> alivePlayers = new ArrayList<>();


    public BombTagGame(ElfPlugin plugin, String id) {
        super(id);
        map = new BombTagMap(plugin, id);
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

            if (!p.isOnline()) {
                return;
            }
            p.getInventory().clear();
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);

            getPlayersToPlay().putIfAbsent(entry.getKey(), p);
        }

        bombTag = new BombTag(plugin, this, 25);
        if (map == null) {
            System.out.println("Cant Start game. Picked Map Failed to load, or doesn't exist..");
            return;
        }
        teleportPlayersToGameLobby();
    }


    @Override
    public void startPregameCountdown() {
        setState(GameState.PREGAME);
        MessageFile file = plugin.getFileUtil().getMessageFile();
        for (String s : file.getBombTagMapCredits()) {
            broadcastGameMessage(s);
        }

        preGameTimer = new BombTagPregameTimer(plugin, () -> {
            teleportPlayersToGame();
            start();
        }, this, 10, TimeTickType.DOWN, false);
        preGameTimer.start();

        System.out.println("Game: " + getGameID() + " Type: " + type.name() + " starting in " + preGameTimer.getTime() + " seconds");
        alivePlayers.addAll(getPlayersToPlay().keySet());
    }

    @Override
    public void start() {
        System.out.println("Game: " + getGameID() + " started");
        setState(GameState.INGAME);
        pickRandomPlayerToBeTagged();

    }

    @Override
    public String type() {
        return type.name();
    }

    @Override
    public void stop() {
        setState(GameState.ENDED);
        if (preGameTimer != null)
            preGameTimer.stop();
        if (bombTag != null) {
            bombTag.cancel();
        }
        shutdown();
    }
    public void shutdown() {
        for (Map.Entry<UUID, PlayerData> entry : plugin.getGameHandler().getAvailablePlayersToPlay().entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            PlayerData data = plugin.getPlayerData(p);
            data.setCurrentGame(null);
        }
    }
    @Override
    public void finish() {
        setState(GameState.POSTGAME);
        List<String> winMessage = plugin.getFileUtil().getMessageFile().getBombTagWinners();
        for (String s : winMessage) {
            UUID uuid1 = getPlacements().getByPlacement(1);
            UUID uuid2 = getPlacements().getByPlacement(2);
            UUID uuid3 = getPlacements().getByPlacement(3);
            String player1 = uuid1 != null ? Bukkit.getOfflinePlayer(uuid1).getName() : "";
            String player2 = uuid2 != null ? Bukkit.getOfflinePlayer(uuid2).getName() : "";
            String player3 = uuid3 != null ? Bukkit.getOfflinePlayer(uuid3).getName() : "";


            broadcastGameMessage(s
                    .replace("%1st%", player1)
                    .replace("%2nd%", player2)
                    .replace("%3rd%", player3)


            );
        }
        awardWinners();


        postGameTimer = new ElfTimer(plugin, () -> {
            for (Player player : getPlayersToPlay().values()) {
                player.getInventory().clear();
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                player.setFoodLevel(20);
            }
            plugin.getGameHandler().teleportToLobbyLocation(getPlayersToPlay());
            stop();

        }, 10, TimeTickType.DOWN, false);
        postGameTimer.start();

    }

    @Override
    public void broadcastGameMessage(String string) {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(string.replace("%prefix%", messageFile.getBombTagPrefix()));
        }
    }

    @Override
    public void playerAbandon(Player player) {
        alivePlayers.remove(player.getUniqueId());
        getPlayersToPlay().remove(player.getUniqueId());
        checkWinner();
    }

    private void teleportPlayersToGameLobby() {
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            player.teleport(map.getLobbyLocation());
        }

    }

    private void teleportPlayersToGame() {
        int i = 0;
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            if (map.getLocationList().get(LocationType.PLAYER_SPAWN).get(i) == null) i = 0;
            player.teleport(map.getPlayerSpawns().get(i));
            i++;
        }
    }


    private void pickRandomPlayerToBeTagged() {
        int i = new Random().nextInt(getPlayersToPlay().size());
        bombTag.tagPlayer((UUID) getPlayersToPlay().keySet().toArray()[i]);

    }

    public void killPlayer(Player player) {
        new BTEffect(ParticleEffect.EXPLOSION_LARGE, Sound.ENTITY_GENERIC_EXPLODE).volume(0.5f).play(player.getLocation());
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        placements.addPlacement(player.getUniqueId(), alivePlayers.size());
        player.sendMessage(messageFile.getBombTagBombPlayerDied().replace("%prefix%", messageFile.getBombTagPrefix()).replace("%placement%", (alivePlayers.size()) + ""));
        broadcastGameMessage(messageFile.getBombTagAnnounceBombExplode().replace("%player_name%", player.getName()));
        alivePlayers.remove(player.getUniqueId());
        player.teleport(map.getSpectatorSpawn());
        player.setGameMode(GameMode.SPECTATOR);
        checkWinner();

    }

    public void checkWinner() {
        if (alivePlayers.size() <= 1) {
            placements.addPlacement(alivePlayers.get(0), 1);
            finish();
            return;
        }
        pickRandomPlayerToBeTagged();
    }

    public void awardWinners() {
        UUID uuid1 = getPlacements().getByPlacement(1);
        UUID uuid2 = getPlacements().getByPlacement(2);
        UUID uuid3 = getPlacements().getByPlacement(3);
        PlayerData playerData1 = null;
        PlayerData playerData2 = null;
        PlayerData playerData3 = null;
        try {
            playerData1 = plugin.getPlayerHolder().getOrNull(uuid1);
            playerData2 = plugin.getPlayerHolder().getOrNull(uuid2);
            playerData3 = plugin.getPlayerHolder().getOrNull(uuid3);
        } catch (Exception e){}

        if (playerData1 != null) playerData1.addCookies(3);
        if (playerData2 != null) playerData2.addCookies(2);
        if (playerData3 != null) playerData3.addCookies(1);
        plugin.getLeaderboard().build();
    }

}
