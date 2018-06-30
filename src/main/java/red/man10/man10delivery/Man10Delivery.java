package red.man10.man10delivery;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.man10vaultapiplus.Man10VaultAPI;

import java.util.HashMap;
import java.util.UUID;

public final class Man10Delivery extends JavaPlugin {

    String prefix = "§2§l[§f白猫§6ヤマント§2§l]§r";
    Man10VaultAPI vault;
    MySQLManager mysql;
    FileConfiguration config;
    HashMap<UUID,UUID> pstats = new HashMap<>();

    boolean power = true;

    int fee = -1;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        fee = config.getInt("fee");
        vault = new Man10VaultAPI("Man10Delivery");
        mysql = new MySQLManager(this,"Man10Delivery");
        MDVData.loadEnable(this,mysql);
        getCommand("mdv").setExecutor(new MDVCommand(this));
        getServer().getPluginManager().registerEvents(new MDVEvent(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
