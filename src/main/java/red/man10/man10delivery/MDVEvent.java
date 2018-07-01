package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import red.man10.man10vaultapiplus.JPYBalanceFormat;
import red.man10.man10vaultapiplus.enums.TransactionCategory;
import red.man10.man10vaultapiplus.enums.TransactionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MDVEvent implements Listener {
    Man10Delivery plugin;

    public MDVEvent(Man10Delivery plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(plugin.pstats.containsKey(p.getUniqueId())) {
            if(hasEmptyInventory(e.getInventory())) {
                p.sendMessage(plugin.prefix+"§c段ボールが空なので配達を中止しました。");
                plugin.pstats.remove(p.getUniqueId());
                return;
            }
            ArrayList<ItemStack> list = new ArrayList<>();
            for(int i = 0; i<=8; i++) {
                if(e.getInventory().getItem(i)!=null) {
                    ItemStack a = e.getInventory().getItem(i);
                    list.add(a);
                }
            }
            if(plugin.vault.getBalance(p.getUniqueId())< plugin.fee){
                p.sendMessage(plugin.prefix+"§cお金が足りないため配送を中止しました。");
                plugin.pstats.remove(p.getUniqueId());
                for(ItemStack i : list){
                    p.getInventory().addItem(i);
                }
                return;
            }
            ItemStack items = new ItemStack(Material.DIAMOND_HOE,1,(short)48);
            ItemMeta itemmeta = items.getItemMeta();
            itemmeta.setDisplayName("§2§l[§f段ボール§6箱§2§l]§7(右クリック)§r");
            List<String> k = new ArrayList<String>();
            k.add("§6送り主: §f"+p.getName());
            String name = null;
            if(Bukkit.getPlayer(plugin.pstats.get(p.getUniqueId()))==null) {
                OfflinePlayer pp = Bukkit.getOfflinePlayer((plugin.pstats.get(p.getUniqueId())));
                name = pp.getName();
            }else {
                Player pp = Bukkit.getPlayer((plugin.pstats.get(p.getUniqueId())));
                name = pp.getName();
            }
            k.add("§e届け先: §f"+name);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E曜日 a H時mm分ss秒");
            k.add("§a配達日時: §f"+sdf.format(c.getTime()));
            if(plugin.pstats2.containsKey(p.getUniqueId())) {
                k.add("§e代引: §f"+new JPYBalanceFormat(plugin.pstats2.get(p.getUniqueId())).getString()+"円");
            }
            k.add("§c注: インベントリに空きが");
            k.add("§cあるときにクリックしてください!");
            itemmeta.setLore(k);
            itemmeta.setUnbreakable(true);
            itemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            items.setItemMeta(itemmeta);
            if(plugin.pstats2.containsKey(p.getUniqueId())) {
                MDVData.addBox(p.getUniqueId(), plugin.pstats.get(p.getUniqueId()), list, items, UUID.randomUUID(), plugin.pstats2.get(p.getUniqueId()));
            }else{
                MDVData.addBox(p.getUniqueId(), plugin.pstats.get(p.getUniqueId()), list, items, UUID.randomUUID());
            }
            plugin.vault.transferMoneyPlayerToCountry(p.getUniqueId(),plugin.fee,TransactionCategory.TAX,TransactionType.FEE,"mdv fee");
            p.sendMessage(plugin.prefix+"§e"+new JPYBalanceFormat(plugin.fee).getString()+"§a円支払い配送しました。");
            plugin.pstats.remove(p.getUniqueId());
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack a = p.getInventory().getItemInMainHand();
            if (a.getType() == Material.AIR) {
                return;
            }
            for (ItemStack items : MDVData.getitems.keySet()) {
                if(items.getType() == a.getType()) {
                    if (items.getItemMeta() == null || items.getItemMeta().toString().equalsIgnoreCase(a.getItemMeta().toString())) {
                        e.setCancelled(true);
                        if (p.getInventory().firstEmpty() == -1) {
                            p.sendMessage(plugin.prefix + "§cインベントリがいっぱいなので中止しました。");
                            return;
                        }
                        p.getInventory().setItemInMainHand(null);
                        MDVData.getItemCheck(p,MDVData.getitems.get(items));
                    }
                }
            }
        }
    }

    public boolean hasEmptyInventory(Inventory inv) {
        for(int i = 0; i<=8; i++){
            if(inv.getItem(i)!=null) {
                return false;
            }
        }
        return true;
    }


}
