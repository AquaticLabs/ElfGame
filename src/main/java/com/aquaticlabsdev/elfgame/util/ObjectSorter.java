package com.aquaticlabsdev.elfgame.util;

import com.aquaticlabsdev.elfgame.data.PlayerData;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 11/14/2021
 * At: 18:33
 */
public class ObjectSorter {

    public static Map<PlayerData, Object> sortInt(Map<PlayerData, Object> items) {

        Map<PlayerData, Integer> convertedItems = new LinkedHashMap<>();
        for (Map.Entry<PlayerData, Object> entry : items.entrySet()) {
            convertedItems.put(entry.getKey(), (int) entry.getValue());
        }
        Map<PlayerData, Object> sortedItems = new LinkedHashMap<>();
        convertedItems.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedItems.put(x.getKey(), x.getValue()));

        return sortedItems;
    }


}
