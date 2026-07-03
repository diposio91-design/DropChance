package ru.anarchyserver.dropchance;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DropChance extends JavaPlugin implements Listener {

    private final Random random = new Random();
    private final Map<Material, DropData> drops = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadDrops();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("=================================");
        getLogger().info("  DropChance v1.0.0 запущен!    ");
        getLogger().info("  Шансы дропа изменены!         ");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("DropChance отключён.");
    }

    private void loadDrops() {
        drops.clear();

        // УГОЛЬ 35%
        drops.put(Material.COAL_ORE,              new DropData(Material.COAL,         0.35, 1, 1));
        drops.put(Material.DEEPSLATE_COAL_ORE,    new DropData(Material.COAL,         0.35, 1, 1));

        // ЖЕЛЕЗО 30%
        drops.put(Material.IRON_ORE,              new DropData(Material.RAW_IRON,     0.30, 1, 1));
        drops.put(Material.DEEPSLATE_IRON_ORE,    new DropData(Material.RAW_IRON,     0.30, 1, 1));

        // МЕДЬ 30%
        drops.put(Material.COPPER_ORE,            new DropData(Material.RAW_COPPER,   0.30, 1, 2));
        drops.put(Material.DEEPSLATE_COPPER_ORE,  new DropData(Material.RAW_COPPER,   0.30, 1, 2));

        // ЗОЛОТО 20%
        drops.put(Material.GOLD_ORE,              new DropData(Material.RAW_GOLD,     0.20, 1, 1));
        drops.put(Material.DEEPSLATE_GOLD_ORE,    new DropData(Material.RAW_GOLD,     0.20, 1, 1));
        drops.put(Material.NETHER_GOLD_ORE,       new DropData(Material.GOLD_NUGGET,  0.20, 1, 2));

        // РЕДСТОУН 25%
        drops.put(Material.REDSTONE_ORE,          new DropData(Material.REDSTONE,     0.25, 1, 2));
        drops.put(Material.DEEPSLATE_REDSTONE_ORE,new DropData(Material.REDSTONE,     0.25, 1, 2));

        // ЛАЗУРИТ 15%
        drops.put(Material.LAPIS_ORE,             new DropData(Material.LAPIS_LAZULI, 0.15, 1, 2));
        drops.put(Material.DEEPSLATE_LAPIS_ORE,   new DropData(Material.LAPIS_LAZULI, 0.15, 1, 2));

        // КВАРЦ 30%
        drops.put(Material.NETHER_QUARTZ_ORE,     new DropData(Material.QUARTZ,       0.30, 1, 2));

        // АЛМАЗ 8%
        drops.put(Material.DIAMOND_ORE,           new DropData(Material.DIAMOND,      0.08, 1, 1));
        drops.put(Material.DEEPSLATE_DIAMOND_ORE, new DropData(Material.DIAMOND,      0.08, 1, 1));

        // ИЗУМРУД 5%
        drops.put(Material.EMERALD_ORE,           new DropData(Material.EMERALD,      0.05, 1, 1));
        drops.put(Material.DEEPSLATE_EMERALD_ORE, new DropData(Material.EMERALD,      0.05, 1, 1));

        // ДРЕВНИЙ МУСОР 6%
        drops.put(Material.ANCIENT_DEBRIS,        new DropData(Material.ANCIENT_DEBRIS, 0.06, 1, 1));

        getLogger().info("Загружено " + drops.size() + " правил дропа.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();

        if (!drops.containsKey(type)) return;

        // Отменяем стандартный дроп
        event.setDropItems(false);

        DropData data = drops.get(type);

        // Проверяем шанс
        if (random.nextDouble() <= data.chance) {
            int amount = data.minAmount + random.nextInt(data.maxAmount - data.minAmount + 1);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(data.drop, amount));
        }
    }

    // Класс для хранения данных дропа
    static class DropData {
        Material drop;
        double chance;
        int minAmount;
        int maxAmount;

        DropData(Material drop, double chance, int minAmount, int maxAmount) {
            this.drop = drop;
            this.chance = chance;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
        }
    }
}
