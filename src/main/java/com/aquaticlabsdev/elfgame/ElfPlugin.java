package com.aquaticlabsdev.elfgame;

import com.aquaticlabsdev.elfgame.commands.CommandManager;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.ElfRoyalActivateSubcommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.ElfRoyalCommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.ElfRoyalRoleSubcommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.ElfRoyalSetupSubcommand;
import com.aquaticlabsdev.elfgame.commands.elfroyal.ElfRoyalWandSubcommand;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.data.PlayerDataHolder;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.peppermint.listeners.PeppermintEDEEvent;
import com.aquaticlabsdev.elfgame.setup.events.WandBlockClick;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
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
        CommandManager elfRoyalManager = new CommandManager(this, new ElfRoyalCommand(this), erSub);
        Objects.requireNonNull(getCommand("elfroyal")).setExecutor(elfRoyalManager);

    }

    private void registerListeners() {
        new WandBlockClick(this);


        // BombTag
        new PeppermintEDEEvent(this, gameHandler);


    }

    public PlayerData getPlayerData(Player player) {
        return playerHolder.getOrNull(player.getUniqueId());
    }

}
