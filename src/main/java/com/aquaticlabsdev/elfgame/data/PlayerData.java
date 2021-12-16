package com.aquaticlabsdev.elfgame.data;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameHandler;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.BattleRoyaleGame;
import com.aquaticlabsdev.elfgame.game.types.bombtag.BombTagGame;
import com.aquaticlabsdev.elfgame.scoreboard.Board;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import lombok.Getter;
import lombok.Setter;
import me.extremesnow.datalib.data.storage.DataObject;
import me.extremesnow.datalib.data.storage.SerializedData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:00
 */

@Getter
@Setter
public class PlayerData extends DataObject {

    private ElfPlugin plugin;
    private UUID uuid;
    private String name;
    private int cookies = 0;
    private int cookiesRank = 0;

    private boolean canBuild = false;

    private Location location1;
    private Location location2;
    private Selection selection;

    private ElfGame currentGame;
    private Board board;

    public PlayerData() {
        this.plugin = ElfPlugin.getInstance();
        loadBoard();
    }

    public PlayerData(ElfPlugin plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        loadBoard();
    }

    public void loadBoard() {
        board = new Board(plugin, this);

    }

    public void startBoard() {
        boolean successful = board.start();

        if (!successful) {
            DebugLogger.logDebugMessage("&c[StatsSB] Something caused the scoreboard startup to fail. Check the scoreboards.yml for any errors.");
        }

    }

    public void addCookies(int i) {
        this.cookies += i;
    }
    public String replacePlaceholders(String s) {

        GameHandler handler = plugin.getGameHandler();
        String playerRank = handler.getRank(this);
        String startTime = "";
        String endingTime = "";
        String bombTagTime = "";
        String ringCloseInTime = "";
        String ringClosingTime = "";
        String playersAlive = "";

        if (handler.getActiveGame() != null) {
            switch (handler.getActiveGame().type()) {
                case "BOMB_TAG":
                    BombTagGame tagGame = (BombTagGame) handler.getActiveGame();
                    startTime = tagGame.getPreGameTimer() != null ? Utils.formatSeconds2(tagGame.getPreGameTimer().getTime()) + "" : "";
                    endingTime = tagGame.getPostGameTimer() != null ? Utils.formatSeconds2(tagGame.getPostGameTimer().getTime()) + "" : "";
                    bombTagTime = tagGame.getBombTag() != null ? Utils.formatSeconds2(tagGame.getBombTag().getTime()) + "" : "";
                    playersAlive = tagGame.getAlivePlayers().size() + "";
                    break;
                case "BATTLE_ROYALE":
                    BattleRoyaleGame battleRoyaleGame = (BattleRoyaleGame) handler.getActiveGame();
                    startTime = battleRoyaleGame.getPreGameTimer() != null ? Utils.formatSeconds2(battleRoyaleGame.getPreGameTimer().getTime()) + "" : "";
                    endingTime = battleRoyaleGame.getPostGameTimer() != null ? Utils.formatSeconds2(battleRoyaleGame.getPostGameTimer().getTime()) + "" : "";

                    if (battleRoyaleGame.getRingTimer() != null) {
                        ringCloseInTime = Utils.formatSeconds2(battleRoyaleGame.getRingTimer().getTime()) + "";
                        ringClosingTime = battleRoyaleGame.getRingTimer().getRingClosedTimer() != null ? Utils.formatSeconds2(battleRoyaleGame.getRingTimer().getRingClosedTimer().getTime()) + "" : "";
                    }

                    playersAlive = battleRoyaleGame.getAlivePlayers().size() + "";

            }
        }

        s = Utils.tryReplaceHexCodes(s);

        return s.replace("%uuid%", uuid.toString())
                .replace("%name%", name)
                .replace("%cookies%", cookies + "")
                .replace("%player-rank%", playerRank)
                .replace("%start-time%", startTime)
                .replace("%ending-time%", endingTime)
                .replace("%ring-time%", ringClosingTime)
                .replace("%ring-close-in%", ringCloseInTime)
                .replace("%kills%", "0") //todo
                .replace("%bomb-time%", bombTagTime)
                .replace("%amount%", playersAlive)

                ;
    }

    public List<String> replacePlaceholders(List<String> s) {
        List<String> replaced = new ArrayList<>();

        for (String str : s) {
            replaced.add(replacePlaceholders(str));
        }

        return replaced;
    }


    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public String[] getStructure() {
        return new String[]{
                "uuid",
                "name",
                "cookies"
        };
    }

    @Override
    public void save() {
        ElfPlugin.getInstance().getPlayerHolder().save(this, true, null);
    }

    @Override
    public void serialize(SerializedData data) {
        data.write("uuid", uuid.toString());
        data.write("name", name);
        data.write("cookies", cookies);
    }

    @Override
    public void deserialize(SerializedData data) {
        this.uuid = UUID.fromString(data.getValue("uuid"));
        this.name = data.getValue("name");
        System.out.println(data.getDataMap());
        DebugLogger.logDebugMessage("cooks: " + data.getValue("cookies"));
        String cook = (data.getValue("cookies").equalsIgnoreCase("ALnullAL$")) ? "00" : data.getValue("cookies");
        this.cookies = Integer.parseInt(cook);
    }
}
