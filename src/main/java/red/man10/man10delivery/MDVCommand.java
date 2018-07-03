package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MDVCommand implements CommandExecutor {
    Man10Delivery plugin;
    public MDVCommand(Man10Delivery plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("mdv.use")){
            p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
            return true;
        }
        if(!plugin.power&&!p.hasPermission("mdv.op")){
            p.sendMessage(plugin.prefix + "§c動作停止中");
            return true;
        }
        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null,9,plugin.prefix);
            ItemStack item = new ItemStack(Material.DIAMOND_HOE,1,(short)48);
            ItemMeta itemm = item.getItemMeta();
            itemm.setDisplayName("§a§l荷物受け取り");
            itemm.setUnbreakable(true);
            itemm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            List<String> lore = new ArrayList<>();
            lore.add("§e荷物を受け取ります。");
            itemm.setLore(lore);
            item.setItemMeta(itemm);
            inv.setItem(1,item);
            ItemStack item2 = new ItemStack(Material.PAPER);
            ItemMeta itemm2 = item2.getItemMeta();
            itemm2.setDisplayName("§e§l手紙作成");
            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore2 = new ArrayList<>();
            lore2.add("§a手紙を作ります。心を込めて作ろう！");
            itemm2.setLore(lore2);
            item2.setItemMeta(itemm2);
            inv.setItem(3,item2);
            ItemStack item3 = new ItemStack(Material.DISPENSER);
            ItemMeta itemm3 = item3.getItemMeta();
            itemm3.setDisplayName("§b§l配達");
            List<String> lore3 = new ArrayList<>();
            lore3.add("§6配達はここから！");
            itemm3.setLore(lore3);
            item3.setItemMeta(itemm3);
            inv.setItem(5,item3);
            ItemStack item4 = new ItemStack(Material.INK_SACK,1,(short)10);
            ItemMeta itemm4 = item4.getItemMeta();
            itemm4.setDisplayName("§e§l代引金引き落とし");
            List<String> lore4 = new ArrayList<>();
            lore4.add("§6受け取られた代引きのお金をもらいます");
            itemm4.setLore(lore4);
            itemm4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
            itemm4.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item4.setItemMeta(itemm4);
            inv.setItem(7,item4);
            plugin.pstats3.put(p.getUniqueId(),"main");
            p.openInventory(inv);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("check")) {
                if(MDVData.containSQLWait(p)){
                    p.sendMessage(plugin.prefix + "§c処理中です。お待ちください。");
                    return true;
                }
                MDVData.addSQLWait(p);
                MDVData.GetPlayerBox(p);
                MDVData.removeSQLWait(p);
                return true;
            }else if (args[0].equalsIgnoreCase("unlock")) {
                if (p.getInventory().getItemInMainHand().getAmount() == 0) {
                    p.sendMessage(plugin.prefix + "§c代引を支払う段ボールを持ってください。");
                    return true;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                if (!MDVData.getitems.containsKey(item)) {
                    p.sendMessage(plugin.prefix + "§c段ボールを持ってください。");
                    return true;
                }
                UUID tag = MDVData.getitems.get(item);
                MDVData.unLockBox(p, tag);
                return true;
            }else if(args[0].equalsIgnoreCase("help")){
                p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
                MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
                MDVData.sendSuggestCommand(p, "§e§l/mdv cod [player名] [代引金額]§f§l: プレイヤーに代引でアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv cod ");
                MDVData.sendHoverText(p, "§e§l/mdv check §f§l: 送られてきたボックスを受け取る", "§aクリックで受け取る", "/mdv check");
                MDVData.sendHoverText(p, "§e§l/mdv unlock §f§l: 代引のロックを解除する", "§a段ボールを持ちクリックで解除", "/mdv unlock");
                MDVData.sendHoverText(p, "§e§l/mdv cash §f§l: 代引金額を受け取る", "§aクリックで受け取る", "/mdv cash");
                MDVData.sendHoverText(p, "§e§l/mdv view §f§l: 箱の中身を確認できる", "§a段ボールを持ちクリックで確認", "/mdv view");
                if(p.hasPermission("mdv.op")){
                    MDVData.sendHoverText(p, "§c§l/mdv on §f§l: このプラグインを起動する", "§a§l§nクリックで起動します!!確認してください!!", "/mdv on");
                    MDVData.sendHoverText(p, "§c§l/mdv off §f§l: このプラグインを停止する", "§4§l§nクリックで停止します!!確認してください!!", "/mdv off");
                    MDVData.sendSuggestCommand(p, "§c§l/mdv info [player名] §f§l: 該当プレイヤーの記録をチェックする", "§aクリックでチャットに打ち込む", "/mdv info ");
                    p.sendMessage("§cv3.3");
                }
                p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
                return true;
            }else if (args[0].equalsIgnoreCase("view")) {
                if(p.getInventory().getItemInMainHand().getAmount()==0){
                    p.sendMessage(plugin.prefix + "§c段ボールを持ってください。");
                    return true;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                if(!MDVData.getitems.containsKey(item)){
                    p.sendMessage(plugin.prefix + "§c段ボールを持ってください。");
                    return true;
                }
                UUID tag = MDVData.getitems.get(item);
                MDVData.viewBox(p,tag);
                return true;
            }else if (args[0].equalsIgnoreCase("send")) {
                MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
                MDVData.sendSuggestCommand(p, "§e§l/mdv cod [player名] [代引金額]§f§l: プレイヤーに代引でアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv cod ");
                MDVData.sendHoverText(p,"§4§l/mdv ??? ??? ??? §c§l: Coming soon…","§cお楽しみに…",null);
                return true;
            }else if (args[0].equalsIgnoreCase("cash")) {
                if(MDVData.containSQLWait(p)){
                    p.sendMessage(plugin.prefix + "§c処理中です。お待ちください。");
                    return true;
                }
                MDVData.addSQLWait(p);
                MDVData.getOfflineBal(p);
                MDVData.removeSQLWait(p);
                return true;
            }else if (args[0].equalsIgnoreCase("on")) {
                if(!p.hasPermission("mdv.op")){
                    p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
                    return true;
                }
                Bukkit.broadcastMessage(plugin.prefix+"§7§lヤマントの§a§l営業を再開中…");
                plugin.power = true;
                Bukkit.broadcastMessage(plugin.prefix+"§a§l再開完了。");
                return true;
            }else if (args[0].equalsIgnoreCase("off")) {
                if(!p.hasPermission("mdv.op")){
                    p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
                    return true;
                }
                Bukkit.broadcastMessage(plugin.prefix+"§7§lヤマントの§c§l営業を停止中…");
                plugin.power = false;
                Bukkit.broadcastMessage(plugin.prefix+"§7§l停止完了。");
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (Bukkit.getPlayer(args[0]) == null) {
                    if (!MDVData.containUser(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
                        p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                        return;
                    }
                    if (Bukkit.getOfflinePlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                    plugin.pstats.put(p.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId());
                    p.openInventory(inv);
                } else {
                    if (Bukkit.getPlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                    plugin.pstats.put(p.getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId());
                    p.openInventory(inv);
                }
            });
            return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                if(!p.hasPermission("mdv.op")){
                    p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    UUID uuid = null;
                    if (Bukkit.getPlayer(args[0]) == null) {
                        if (!MDVData.containUser(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
                            p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                            return;
                        }
                        if (Bukkit.getOfflinePlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                            p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                            return;
                        }
                        uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                    } else {
                        if (Bukkit.getPlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                            p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                            return;
                        }
                        uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                    }
                    MDVData.GetPlayerInfo(p, uuid);
                });
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("cod")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                double cash = 0;
                try{
                    cash = Double.valueOf(args[2]);
                }catch (NumberFormatException e){
                    p.sendMessage(plugin.prefix + "§c数字で入力してください");
                    return;
                }
                if(cash < 1){
                    p.sendMessage(plugin.prefix + "§c1以上の数字で入力してください");
                    return;
                }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        if (!MDVData.containUser(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
                            p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                            return;
                        }
                        if (Bukkit.getOfflinePlayer(args[1]).getUniqueId() == p.getUniqueId()) {
                            p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                            return;
                        }
                        Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                        plugin.pstats.put(p.getUniqueId(), Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                        plugin.pstats2.put(p.getUniqueId(), cash);
                        p.openInventory(inv);
                    } else {
                        if (Bukkit.getPlayer(args[1]).getUniqueId() == p.getUniqueId()) {
                            p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                            return;
                        }
                        Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                        plugin.pstats.put(p.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId());
                        plugin.pstats2.put(p.getUniqueId(), cash);
                        p.openInventory(inv);
                    }
                });
            }
        }
        Inventory inv = Bukkit.createInventory(null,9,plugin.prefix);
        ItemStack item = new ItemStack(Material.DIAMOND_HOE,1,(short)48);
        ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName("§a§l荷物受け取り");
        itemm.setUnbreakable(true);
        itemm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        List<String> lore = new ArrayList<>();
        lore.add("§e荷物を受け取ります。");
        itemm.setLore(lore);
        item.setItemMeta(itemm);
        inv.setItem(1,item);
        ItemStack item2 = new ItemStack(Material.PAPER);
        ItemMeta itemm2 = item2.getItemMeta();
        itemm2.setDisplayName("§e§l手紙作成");
        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore2 = new ArrayList<>();
        lore2.add("§a手紙を作ります。心を込めて作ろう！");
        itemm2.setLore(lore2);
        item2.setItemMeta(itemm2);
        inv.setItem(3,item2);
        ItemStack item3 = new ItemStack(Material.DISPENSER);
        ItemMeta itemm3 = item3.getItemMeta();
        itemm3.setDisplayName("§b§l配達");
        List<String> lore3 = new ArrayList<>();
        lore3.add("§6配達はここから！");
        itemm3.setLore(lore3);
        item3.setItemMeta(itemm3);
        inv.setItem(5,item3);
        ItemStack item4 = new ItemStack(Material.INK_SACK,1,(short)10);
        ItemMeta itemm4 = item4.getItemMeta();
        itemm4.setDisplayName("§e§l代引金引き落とし");
        List<String> lore4 = new ArrayList<>();
        lore4.add("§6受け取られた代引きのお金をもらいます");
        itemm4.setLore(lore4);
        itemm4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
        itemm4.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item4.setItemMeta(itemm4);
        inv.setItem(7,item4);
        plugin.pstats3.put(p.getUniqueId(),"main");
        p.openInventory(inv);
        return true;
    }
}
