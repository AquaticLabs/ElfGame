package com.aquaticlabsdev.elfgame.game.types.battleroyale.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfgame.util.file.ConfigFile;
import com.aquaticlabsdev.elfroyal.timer.ObjectTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import com.aquaticlabsdev.elfroyal.util.WeightedRandomBag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: extremesnow
 * On: 12/14/2021
 * At: 18:20
 */
public class LootTables {


    private final ElfPlugin plugin;
    private WeightedRandomBag<List<ItemStack>> lootGroups = new WeightedRandomBag<>();

    public LootTables(ElfPlugin plugin) {
        this.plugin = plugin;
        ConfigFile configFile = plugin.getFileUtil().getConfigFile();
        ConfigurationSection section = configFile.getConfig().getConfigurationSection("Games.Types.BATTLE_ROYALE");
        for (String lootTable : section.getKeys(false)) {
            List<String> loot = section.getStringList(lootTable + ".items");
            List<ItemStack> items = new ArrayList<>();
            for (String s : loot) {
                String[] itemStr = s.split(":");
                try {
                    items.add(new ItemStack(Material.matchMaterial(itemStr[0]), Integer.parseInt(itemStr[1])));
                } catch (Exception e) {
                }
            }
            lootGroups.addEntry(items, section.getDouble(lootTable + ".weight"));
        }

    }

    private List<ItemStack> getRandomWeightedLootTable() {
        return lootGroups.getRandom();
    }

    public void dropRandomLootTableLoot(Location location) {
        List<ItemStack> lootTable = getRandomWeightedLootTable();
        int min = 3;
        int max = 5;
        int amountToDrop = Utils.randomNumber(min, max);

        List<Item> droppedItems = new ArrayList<>();
        for (int j = 0; j < amountToDrop; j++) {
            int i = new Random().nextInt(lootTable.size());

            //  droppedMaterial.add(lootTable.get(i).getType());
            Item item = location.getWorld().dropItemNaturally(location, lootTable.get(i));
            droppedItems.add(item);

        }
        new ObjectTimer(plugin, 60, TimeTickType.DOWN, false) {
            @Override
            public void whenComplete() {
                for (Item item : droppedItems) {
                    if (item.isValid())
                        item.remove();
                }
            }
        }.start();
    }

}
