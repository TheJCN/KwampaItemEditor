package jcn.itemeditor;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class ItemEditor extends JavaPlugin {
    private Economy economy;
    private static final Logger logger = Bukkit.getLogger();
    private FileConfiguration config;
    private File configFile;

    public FileConfiguration getFileConfiguration() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    @Override
    public void onEnable() {
        logger.info("Плагин запущен");

        this.config = getConfig();

        // Создаем папку плагина, если ее нет
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Создаем и загружаем конфигурационный файл
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config.set("type", "lvl");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        String price = config.getString("lvl");

        if (!setupEconomy()) {
            logger.warning("Vault не обнаружен на сервере");
        }

        getCommand("rename").setExecutor(new Rename());
        getCommand("relore").setExecutor(new Relore());

        saveConfig();
    }

    @Override
    public void onDisable() {
        logger.info("Плагин выключен");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return true;
        }

        return false;
    }
}
