package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Man10TradeEvent implements Listener {

    Man10Delivery plugin;

    public Man10TradeEvent(Man10Delivery plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getInventory().getName().equals("§e§lトレード")){
            e.setCancelled(true);

            if (e.getSlot() == 46 || e.getSlot() == 47){
                ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)4);
                ItemMeta meta = is.getItemMeta();
                meta.setDisplayName("あなたは取引完了しています");
                is.setItemMeta(meta);

                Inventory i = e.getInventory();
                i.setItem(46,is);
                i.setItem(47,is);
                i.setItem(48,is);
                i.setItem(49,is);

                e.getWhoClicked().openInventory(i);
                return;
            }



            Bukkit.getLogger().info("clicked");

        }
    }


}
