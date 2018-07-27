package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class AutoCheck {
    private Man10Delivery plugin;
    private BukkitTask run;
    private ArrayList<UUID> autolist = new ArrayList<>();
    public AutoCheck(Man10Delivery plugin){
        this.plugin = plugin;
    }
    public void start() {
        run = new BukkitRunnable(){
            @Override
            public void run() {
                if (plugin.power) {
                    for(UUID uuid:autolist){
                        MDVData.LoginContainBox(Bukkit.getPlayer(uuid));
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 6000);
    }

    public void removeUUID(UUID uuid){
        autolist.remove(uuid);
    }

    public void addUUID(UUID uuid){
        autolist.add(uuid);
    }

    public void reboot(){
        run.cancel();
    }

}
