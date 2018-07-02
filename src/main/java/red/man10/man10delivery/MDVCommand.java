package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
            p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
            MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
            MDVData.sendSuggestCommand(p, "§e§l/mdv cod [player名] [代引金額]§f§l: プレイヤーに代引でアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv cod ");
            MDVData.sendHoverText(p, "§e§l/mdv check §f§l: 送られてきたボックスを受け取る", "§aクリックで受け取る", "/mdv check");
            MDVData.sendHoverText(p, "§e§l/mdv unlock §f§l: 代引のロックを解除する", "§a段ボールを持ちクリックで解除", "/mdv unlock");
            MDVData.sendHoverText(p, "§e§l/mdv cash §f§l: 代引金額を受け取る", "§aクリックで受け取る", "/mdv cash");
            if(p.hasPermission("mdv.op")){
                MDVData.sendHoverText(p, "§c§l/mdv on §f§l: このプラグインを起動する", "§a§l§nクリックで起動します!!確認してください!!", "/mdv on");
                MDVData.sendHoverText(p, "§c§l/mdv off §f§l: このプラグインを停止する", "§4§l§nクリックで停止します!!確認してください!!", "/mdv off");
                MDVData.sendSuggestCommand(p, "§c§l/mdv info [player名] §f§l: 該当プレイヤーの記録をチェックする", "§aクリックでチャットに打ち込む", "/mdv info ");
                p.sendMessage("§cv3.2");
            }
            p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
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
                if(p.getInventory().getItemInMainHand().getAmount()==0){
                    p.sendMessage(plugin.prefix + "§c代引を支払う段ボールを持ってください。");
                    return true;
                }
                ItemStack item = p.getInventory().getItemInMainHand();
                if(!MDVData.getitems.containsKey(item)){
                    p.sendMessage(plugin.prefix + "§c段ボールを持ってください。");
                    return true;
                }
                UUID tag = MDVData.getitems.get(item);
                MDVData.unLockBox(p,tag);
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
            if(Bukkit.getPlayer(args[0])==null) {
                if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                    p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                    return true;
                }
                if(Bukkit.getOfflinePlayer(args[0]).getUniqueId()==p.getUniqueId()){
                    p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                    return true;
                }
                Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                plugin.pstats.put(p.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId());
                p.openInventory(inv);
                return true;
            }else {
                if (Bukkit.getPlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                    p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                    return true;
                }
                Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                plugin.pstats.put(p.getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId());
                p.openInventory(inv);
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                if(!p.hasPermission("mdv.op")){
                    p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
                    return true;
                }
                UUID uuid = null;
                if(Bukkit.getPlayer(args[0])==null) {
                    if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                        p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                        return true;
                    }
                    if(Bukkit.getOfflinePlayer(args[0]).getUniqueId()==p.getUniqueId()){
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return true;
                    }
                    uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                }else {
                    if (Bukkit.getPlayer(args[0]).getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return true;
                    }
                    uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                }
                MDVData.GetPlayerInfo(p,uuid);
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("cod")) {
                double cash = 0;
                try{
                    cash = Double.valueOf(args[2]);
                }catch (NumberFormatException e){
                    p.sendMessage(plugin.prefix + "§c数字で入力してください");
                    return true;
                }
                if(cash < 1){
                    p.sendMessage(plugin.prefix + "§c1以上の数字で入力してください");
                    return true;
                }
                if(Bukkit.getPlayer(args[1])==null) {
                    if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                        p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                        return true;
                    }
                    if(Bukkit.getOfflinePlayer(args[1]).getUniqueId()==p.getUniqueId()){
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return true;
                    }
                    Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                    plugin.pstats.put(p.getUniqueId(), Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    plugin.pstats2.put(p.getUniqueId(),cash);
                    p.openInventory(inv);
                    return true;
                }else {
                    if (Bukkit.getPlayer(args[1]).getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(plugin.prefix + "§c自分自身には送れません！");
                        return true;
                    }
                    Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
                    plugin.pstats.put(p.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId());
                    plugin.pstats2.put(p.getUniqueId(),cash);
                    p.openInventory(inv);
                    return true;
                }
            }
        }
        p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
        MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
        MDVData.sendSuggestCommand(p, "§e§l/mdv cod [player名] [代引金額]§f§l: プレイヤーに代引でアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv cod ");
        MDVData.sendHoverText(p, "§e§l/mdv check §f§l: 送られてきたボックスを受け取る", "§aクリックで受け取る", "/mdv check");
        MDVData.sendHoverText(p, "§e§l/mdv unlock §f§l: 代引のロックを解除する", "§a段ボールを持ちクリックで解除", "/mdv unlock");
        MDVData.sendHoverText(p, "§e§l/mdv cash §f§l: 代引金額を受け取る", "§aクリックで受け取る", "/mdv cash");
        if(p.hasPermission("mdv.op")){
            MDVData.sendHoverText(p, "§c§l/mdv on §f§l: このプラグインを起動する", "§a§l§nクリックで起動します!!確認してください!!", "/mdv on");
            MDVData.sendHoverText(p, "§c§l/mdv off §f§l: このプラグインを停止する", "§4§l§nクリックで停止します!!確認してください!!", "/mdv off");
            MDVData.sendSuggestCommand(p, "§c§l/mdv info [player名] §f§l: 該当プレイヤーの記録をチェックする", "§aクリックでチャットに打ち込む", "/mdv info ");
            p.sendMessage("§cv3.2");
        }
        p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
        return true;
    }
}
