package com.aquaticlabsdev.elfgame.data;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import lombok.Getter;
import me.extremesnow.datalib.data.SQLCredential;
import me.extremesnow.datalib.data.storage.StorageHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:06
 */

public class PlayerDataHolder extends StorageHolder<PlayerData> {

    private final ElfPlugin plugin;

    @Getter
    private Map<UUID, PlayerData> data = new ConcurrentHashMap<>();

    public PlayerDataHolder(ElfPlugin plugin, SQLCredential credential) {
        super(credential.build());
        this.plugin = plugin;
        addVariant(credential.getTable(), PlayerData.class);
        load(false);
    }

    public PlayerData getOrNull(UUID uuid) {
        return data.get(uuid);
    }

    public PlayerData getOrInsert(UUID uuid) {
        PlayerData playerData = data.get(uuid);
        if (playerData == null) {
            playerData = new PlayerData(plugin, uuid);
            onAdd(playerData);
        }
        return playerData;
    }

    public Set<String> getAllNames() {
        Set<String> userNames = new HashSet<>();
        for (PlayerData entry : data.values()) {
            userNames.add(entry.getName());
        }
        return userNames;
    }

    public Set<PlayerData> getAllPlayerDatas() {
        return new HashSet<>(data.values());
    }

    public Set<UUID> getAllUUIDs() {
        return new HashSet<>(data.keySet());
    }

    public Set<String> getAllNamesLowercase() {
        Set<String> userNames = new HashSet<>();
        for (PlayerData entry : data.values()) {
            userNames.add(entry.getName().toLowerCase());
        }
        return userNames;
    }

    @Override
    protected void onAdd(PlayerData object) {
        data.put(object.getUuid(), object);
    }

    @Override
    protected void onRemove(PlayerData object) {
        data.remove(object.getUuid());
    }

    @Override
    public Iterator<PlayerData> iterator() {
        return data.values().iterator();
    }
}

