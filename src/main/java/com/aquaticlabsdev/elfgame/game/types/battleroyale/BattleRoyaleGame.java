package com.aquaticlabsdev.elfgame.game.types.battleroyale;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BRRingTimer;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyaleMap;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyalePregameTimer;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.LootTables;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GamePlacements;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
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
    private BRRingTimer ringTimer;
    private GameTimer postGameTimer;
    private final GameType type = GameType.BATTLE_ROYALE;
    @Setter
    @Getter
    private BattleRoyaleMap map;
    @Getter
    private GamePlacements placements = new GamePlacements();
    @Getter
    private LootTables lootTable;

    @Getter
    private boolean grace = true;
    private int gracePeriodTime = 90;

    @Getter
    private Map<Location, BlockData> chestsToReplace = new HashMap<>();

    private List<UUID> alivePlayers = new ArrayList<>();

    public BattleRoyaleGame(ElfPlugin plugin, String id) {
        super(id);
        map = new BattleRoyaleMap(plugin, id);
        this.plugin = plugin;
    }

    @Override
    public void activate() {
        setState(GameState.ACTIVATED);
        lootTable = new LootTables(plugin);
        map.load();
        removeSigns();
        for (Map.Entry<UUID, PlayerData> entry : plugin.getGameHandler().getAvailablePlayersToPlay().entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            PlayerData data = plugin.getPlayerData(p);
            data.setCurrentGame(this);

            if (!p.isOnline()) {
                return;
            }
            getPlayersToPlay().putIfAbsent(entry.getKey(), p);
            p.getInventory().clear();
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);
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
        MessageFile file = plugin.getFileUtil().getMessageFile();
        for (String s : file.getBombTagMapCredits()) {
            broadcastGameMessage(s);
        }
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
        setState(GameState.INGAME);
        grace = true;

        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        broadcastGameMessage(messageFile.getBattleRoyaleGracePeriod().replace("%time%", Utils.formatSeconds(gracePeriodTime)));
        new ElfTimer(plugin, () -> {
            grace = false;
            broadcastGameMessage(messageFile.getBattleRoyaleGracePeriodEnded());
        }, gracePeriodTime, TimeTickType.DOWN).start();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (UUID uuid : alivePlayers) {
                Player player = Bukkit.getPlayer(uuid);
                player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                player.setGliding(true);
            }
        }, 3L);


        ringTimer = new BRRingTimer(plugin, this, 65, (2 * 60), TimeTickType.DOWN);
        ringTimer.start();


    }


    @Override
    public String type() {
        return type.name();
    }

    @Override
    public void stop() {
        setState(GameState.ENDED);
        if (map != null) {
            replaceChests();
            replaceSigns();
        }
        if (ringTimer != null) {
            ringTimer.fixBorder();
            if (ringTimer.getTimerTask() != null)
                ringTimer.stop();

            if (ringTimer.getRingClosedTimer() != null)
                ringTimer.getRingClosedTimer().stop();

        }

        if (preGameTimer != null)
            preGameTimer.stop();
        if (postGameTimer != null)
            postGameTimer.stop();

        //todo return all players gamemodes to survival and teleport them to server lobby

    }

    @Override
    public void finish() {
        setState(GameState.POSTGAME);
        ringTimer.fixBorder();
        List<String> winMessage = plugin.getFileUtil().getMessageFile().getBattleRoyaleWinners();
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
    }

    @Override
    public void broadcastGameMessage(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(string);
        }
    }

    @Override
    public void playerAbandon(Player player) {
        alivePlayers.remove(player.getUniqueId());
        getPlayersToPlay().remove(player.getUniqueId());
        checkWinner();
    }

    public void killPlayer(Player victim, Player killer) {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        placements.addPlacement(victim.getUniqueId(), alivePlayers.size());
        alivePlayers.remove(victim.getUniqueId());
        if (killer != null) {
            broadcastGameMessage(messageFile.getBattleRoyaleKill()
                    .replace("%killer_name%", killer.getName())
                    .replace("%victim_name%", victim.getName()));
        } else {
            broadcastGameMessage(messageFile.getBattleRoyaleKillNoKiller()
                    .replace("%victim_name%", victim.getName()));
        }
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

    public void replaceChests() {
        for (Map.Entry<Location, BlockData> chest : chestsToReplace.entrySet()) {
            Location location = chest.getKey();
            location.getBlock().setBlockData(chest.getValue());
        }
    }

    public void removeSigns() {
        map.removeSignsAndSignPlatforms();
    }

    public void replaceSigns() {
        map.replaceSignsAndSignPlatforms();
    }

}
