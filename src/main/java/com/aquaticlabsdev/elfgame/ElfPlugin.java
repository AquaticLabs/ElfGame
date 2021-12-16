package com.aquaticlabsdev.elfgame;

import com.aquaticlabsdev.elfgame.commands.CommandManager;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.*;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.data.PlayerDataHolder;
import com.aquaticlabsdev.elfgame.events.BlockBreak;
import com.aquaticlabsdev.elfgame.events.BlockPlace;
import com.aquaticlabsdev.elfgame.events.PlayerDamage;
import com.aquaticlabsdev.elfgame.events.PlayerJoin;
import com.aquaticlabsdev.elfgame.events.PlayerQuit;
import com.aquaticlabsdev.elfgame.events.PlayerRespawn;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerDamage;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerDamageByPlayer;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerDeath;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerDrop;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerInteract;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerMove;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.listeners.BRPlayerRespawn;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BRRingTimer;
import com.aquaticlabsdev.elfgame.game.types.bombtag.listeners.BombTagEDEEvent;
import com.aquaticlabsdev.elfgame.setup.events.WandBlockClick;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import com.aquaticlabsdev.elfgame.util.Leaderboard;
import com.aquaticlabsdev.elfgame.util.file.FileUtil;
import com.aquaticlabsdev.elfroyal.ElfRoyalPlugin;
import lombok.Getter;
import me.extremesnow.datalib.DataManager;
import me.extremesnow.datalib.data.SQLCredential;
import me.extremesnow.datalib.data.sqlite.SQLiteCredential;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ElfPlugin extends ElfRoyalPlugin {

    @Getter
    private static ElfPlugin instance;

    @Getter
    private PlayerDataHolder playerHolder;

    @Getter
    private Leaderboard leaderboard;

    @Getter
    private FileUtil fileUtil;
    @Getter
    private GameHandler gameHandler;


    @Override
    public void onEnable() {
        instance = this;
        reloadFileUtils();
        loadDataCredential();
        this.gameHandler = new GameHandler(this);
        registerCommands();
        registerListeners();
        this.leaderboard = new Leaderboard(this);

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            onReload();
        }
    }

    @Override
    public void onDisable() {
        gameHandler.cancelActiveGame();
        // Plugin shutdown logic
    }

    public void onReload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerHolder().getOrInsert(player.getUniqueId());
            playerData.setName(player.getName());
            playerData.save();
            gameHandler.addToGamePlayers(playerData);
            playerData.getBoard().start();

        }
    }


    public void reloadFileUtils() {
        if (fileUtil == null) {
            fileUtil = new FileUtil(this);
        }
        fileUtil.setupConfig("config");
        fileUtil.setupConfig("messages");
        fileUtil.setupConfig("maps");
        fileUtil.setupConfig("scoreboards");
        fileUtil.setupConfig("animations");
    }


    private void loadDataCredential() {

        new DataManager(task -> {
            Bukkit.getScheduler().runTaskAsynchronously(this, task);
        }, Runnable::run, error -> {
            System.out.println("DataLibError: " + error);
        }, true);


        long loadStart = System.currentTimeMillis();
        File dataFolder = getDataFolder();

        SQLCredential sqlCredential;

        DebugLogger.logDebugMessage("SQLiteCredential Start");

        sqlCredential = new SQLiteCredential(dataFolder, "elf_data", "elfdata");

        DebugLogger.logDebugMessage("SQLiteCredential Finish");


        if (playerHolder != null) {
            DebugLogger.logDebugMessage("Trying to create a new Data Holder when the previous one is already loaded.");
            return;
        }
        playerHolder = new PlayerDataHolder(this, sqlCredential);

        long loadEnd = System.currentTimeMillis();
        long loadElapsedTime = loadEnd - loadStart;
        DebugLogger.logDebugMessage("Data Loading time: " + loadElapsedTime + "ms");
        DebugLogger.logDebugMessage("Loaded " + playerHolder.getAllPlayerDatas().size() + " users.");

    }


    private void registerCommands() {
        Map<String, Subcommand> erSub = new HashMap<>();
        erSub.put("setup", new ElfRoyalSetupSubcommand(this));
        erSub.put("wand", new ElfRoyalWandSubcommand());
        erSub.put("activate", new ElfRoyalActivateSubcommand(this));
        erSub.put("role", new ElfRoyalRoleSubcommand(this));
        erSub.put("start", new ElfRoyalStartGameSubcommand(this));
        erSub.put("stop", new ElfRoyalStopGameSubcommand(this));
        erSub.put("deletemap", new ElfRoyalDeleteMapSubcommand(this));
        erSub.put("setmainlobbyloc", new ElfRoyalSetLobbySubcommand(this));
        erSub.put("build", new ElfRoyalToggleBuildSubcommand(this));
        CommandManager elfRoyalManager = new CommandManager(this, new ElfRoyalCommand(this), erSub);
        Objects.requireNonNull(getCommand("elfroyal")).setExecutor(elfRoyalManager);

    }

    private void registerListeners() {
        new WandBlockClick(this);
        new PlayerJoin(this);
        new PlayerQuit(this);
        new BlockBreak(this);
        new BlockPlace(this);
        new PlayerDamage(this);
        new PlayerRespawn(this, gameHandler);

        // BombTag
        new BombTagEDEEvent(this, gameHandler);

        // Battle Royale
        new BRPlayerDeath(this, gameHandler);
        new BRPlayerInteract(this, gameHandler);
        new BRPlayerMove(this, gameHandler);
        new BRPlayerMove(this, gameHandler);
        new BRPlayerDrop(this, gameHandler);
        new BRPlayerDamage(this, gameHandler);
//        new BRPlayerRespawn(this, gameHandler);
        new BRPlayerDamageByPlayer(this, gameHandler);

    }

    public PlayerData getPlayerData(Player player) {
        return playerHolder.getOrNull(player.getUniqueId());
    }

}
