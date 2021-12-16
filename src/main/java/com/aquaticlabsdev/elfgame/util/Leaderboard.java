package com.aquaticlabsdev.elfgame.util;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: extremesnow
 * On: 11/14/2021
 * At: 18:26
 */
public class Leaderboard {

    private final ElfPlugin plugin;
    private Map<PlayerData, Object> dataMap = new LinkedHashMap<>();
    @Setter
    private boolean async = true;

    @Getter
    @Setter
    private boolean statsChanged = false;

    public Leaderboard(ElfPlugin plugin) {
        this.plugin = plugin;
        build();
    }

    public void build() {
        init();
    }

    public void init() {
        if (async) {
            CompletableFuture.runAsync(() -> {

                for (PlayerData playerData : plugin.getPlayerHolder().getAllPlayerDatas()) {
                    if (dataMap.containsKey(playerData)) {
                        dataMap.replace(playerData, playerData.getCookies());
                        continue;
                    }
                    int statValue = playerData.getCookies();


                    dataMap.putIfAbsent(playerData, statValue);
                }
                dataMap = ObjectSorter.sortInt(new LinkedHashMap<>(dataMap));
                DebugLogger.logDebugMessage("Cookies DataMap Sorted");

            }).whenComplete((t, s) -> updateScores());
        }
    }

    public void updateScores() {
        CompletableFuture.runAsync(() -> {
            int rank = 1;
            for (PlayerData entry : dataMap.keySet()) {
                entry.setCookiesRank(rank);
                rank++;
            }

            plugin.getPlayerHolder().save(true, () -> {
                DebugLogger.logDebugMessage("Finished Sorting Leaderboard");
            });
        });

    }

    public boolean buildIfChanged() {
        if (statsChanged) {
            build();
            statsChanged = false;
            return true;
        }
        return false;
    }


    public Map<PlayerData, Object> getTop(int amount) {
        Map<PlayerData, Object> topAmount = new LinkedHashMap<>();
        int i = 0;
        for (Map.Entry<PlayerData, Object> entry : dataMap.entrySet()) {
            if (i == amount) break;
            topAmount.put(entry.getKey(), entry.getValue());
            i++;
        }
        return topAmount;
    }

    public void addPlayer(PlayerData playerData, Object baseAmount) {
        dataMap.putIfAbsent(playerData, baseAmount);
    }


}
