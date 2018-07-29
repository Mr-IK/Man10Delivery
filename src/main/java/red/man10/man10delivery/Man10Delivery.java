package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.man10vaultapiplus.Man10VaultAPI;

import java.util.HashMap;
import java.util.UUID;

public final class Man10Delivery extends JavaPlugin {

    String prefix = "§2§l[§f白猫§6ヤマント§2§l]§r";
    Man10VaultAPI vault;
    QuestManager quest;
    MySQLManager mysql;
    FileConfiguration config;
    MDVEvent event;
    AutoCheck ac;
    HashMap<UUID,UUID> pstats = new HashMap<>();
    HashMap<UUID,Double> pstats2 = new HashMap<>();
    HashMap<UUID,String> pstats3 = new HashMap<>();

    boolean power = true;

    Material box;
    int meta;

    int fee = -1;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        fee = config.getInt("fee");
        box = Material.getMaterial(config.getString("box_material"));
        meta = config.getInt("box_meta");
        vault = new Man10VaultAPI("Man10Delivery");
        mysql = new MySQLManager(this,"Man10Delivery");
        quest = new QuestManager(this);
        MDVData.loadEnable(this,mysql);
        LogManager.loadEnable(this);
        getCommand("mdv").setExecutor(new MDVCommand(this));
        getCommand("mdvlog").setExecutor(new LogCommand(this));
        getCommand("mletter").setExecutor(new MletterCommand(this));
        event = new MDVEvent(this);
        ac = new AutoCheck(this);
        ac.start();
        getServer().getPluginManager().registerEvents(event, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ac.reboot();
    }

    public void systemReboot(){
        Bukkit.broadcastMessage(prefix+"§7§lヤマントシステム §c§lリブート中…");
        power = false;
        ac.reboot();
        Bukkit.broadcastMessage(prefix+"§7§lヤマント営業停止完了。");
        Bukkit.broadcastMessage(prefix+"§7§lヤマントを再読み込み中…");
        saveDefaultConfig();
        config = getConfig();
        fee = config.getInt("fee");
        box = Material.getMaterial(config.getString("box_material"));
        meta = config.getInt("box_meta");
        vault = new Man10VaultAPI("Man10Delivery");
        mysql = new MySQLManager(this,"Man10Delivery");
        MDVData.loadEnable(this,mysql);
        event = new MDVEvent(this);
        ac = new AutoCheck(this);
        ac.start();
        Bukkit.broadcastMessage(prefix+"§7§l再読み込み完了。");
        Bukkit.broadcastMessage(prefix+"§7§lヤマントの§a§l営業を再開中…");
        power = true;
        Bukkit.broadcastMessage(prefix+"§a§l再開完了。");
        Bukkit.broadcastMessage(prefix+"§a§lヤマントシステム リブート完了。");
    }

    public void setFee(int fee){
        getConfig().set("fee",fee);
        saveConfig();
    }

    public void setMaterial(String material,int meta){
        getConfig().set("box_material",material);
        getConfig().set("box_meta",meta);
        saveConfig();
    }

}
