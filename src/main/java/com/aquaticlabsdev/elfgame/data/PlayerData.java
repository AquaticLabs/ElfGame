package com.aquaticlabsdev.elfgame.data;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.scoreboard.Board;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import com.aquaticlabsdev.elfgame.util.DebugLogger;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
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
    private int cookies;

    private boolean canBuild = false;

    private Location location1;
    private Location location2;
    private Selection selection;

    private ElfGame currentGame;
    private Board board;

    public PlayerData() {}

    public PlayerData(ElfPlugin plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void loadBoard() {
        board = new Board(plugin, this);

    }
    public void startBoard() {
        boolean successful = board.start();

        if(!successful) {
            DebugLogger.logDebugMessage("&c[StatsSB] Something caused the scoreboard startup to fail. Check the scoreboards.yml for any errors.");
        }

    }

    public String replacePlaceholders(String s) {

        return s.replace("%uuid%", uuid.toString())
                .replace("%name%", name)
                .replace("%cookies%", cookies + "");
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

        String cook = (data.getValue("cookies").equalsIgnoreCase("ALnullAL$")) ? "0" : data.getValue("cookies");
        this.cookies = Integer.parseInt(cook);
    }
}
