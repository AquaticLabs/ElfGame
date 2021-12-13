package com.aquaticlabsdev.elfgame.util;

import com.aquaticlabsdev.elfroyal.game.GameMap;
import com.aquaticlabsdev.elfroyal.loc.LocationType;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 23:50
 */
public class TemporaryBlankMap implements GameMap {


    private String name;
    public TemporaryBlankMap() {
    }
    public TemporaryBlankMap(String name) {
        this.name = name;
    }

    @Override
    public String getMapName() {
        return name;
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

    @Override
    public Selection getMapBounds() {
        return null;
    }

    @Override
    public Map<LocationType, List<Location>> getLocationList() {
        return null;
    }
}
