package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LogCommand implements CommandExecutor {
    Man10Delivery plugin;

    public LogCommand(Man10Delivery plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mdv.log")) {
            p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
            return true;
        }
        if(args.length == 0){
            p.sendMessage("§a/mdvlog [id/fromname/toname/tag/all] [val] §e: ログを見る");
            p.sendMessage("§a/mdvlog viewitem [id] §e: ログを見る");
            return true;
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("all")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfromall();
                    if (loglist.size() == 0) {
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    for (int i = (loglist.size() - 1); i > (loglist.size() - 15); i--) {
                        if (i < 0) {
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        p.sendMessage(plugin.prefix + "§e" + log.id + ": §b" + log.category + " §6" + log.time + " §a" + log.fromname + " -> " + log.toname + " §e" + log.cod + "円");
                    }
                    p.sendMessage(plugin.prefix + "§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                });
                return true;
            }
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("id")){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    int id = -1;
                    try{
                        id = Integer.parseInt(args[1]);
                    }catch (NumberFormatException e){
                        p.sendMessage(plugin.prefix + "§cidならvalは数字で入力！");
                        return;
                    }
                    if(id <= 0){
                        p.sendMessage(plugin.prefix + "§cidならvalは1以上の数字で入力！");
                        return;
                    }
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfromid(id);
                    if(loglist.size()==0){
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    for(int i = (loglist.size()-1); i >(loglist.size() - 15); i--){
                        if(i < 0){
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        p.sendMessage(plugin.prefix+"§e"+log.id+": §b"+log.category+" §6"+log.time+" §a"+log.fromname+" -> "+log.toname+" §e"+log.cod+"円");
                    }
                    p.sendMessage(plugin.prefix+"§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                });
                return true;
            }else if(args[0].equalsIgnoreCase("fromname")){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfrom_fromname(args[1]);
                    if(loglist.size()==0){
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    for(int i = (loglist.size()-1); i >(loglist.size() - 15); i--){
                        if(i < 0){
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        p.sendMessage(plugin.prefix+"§e"+log.id+": §b"+log.category+" §6"+log.time+" §a"+log.fromname+" -> "+log.toname+" §e"+log.cod+"円");
                    }
                    p.sendMessage(plugin.prefix+"§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                });
                return true;
            }else if(args[0].equalsIgnoreCase("toname")){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfrom_toname(args[1]);
                    if(loglist.size()==0){
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    for(int i = (loglist.size()-1); i >(loglist.size() - 15); i--){
                        if(i < 0){
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        p.sendMessage(plugin.prefix+"§e"+log.id+": §b"+log.category+" §6"+log.time+" §a"+log.fromname+" -> "+log.toname+" §e"+log.cod+"円");
                    }
                    p.sendMessage(plugin.prefix+"§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                });
                return true;
            }else if(args[0].equalsIgnoreCase("tag")){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfromTag(args[1]);
                    if(loglist.size()==0){
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    for(int i = (loglist.size()-1); i >(loglist.size() - 15); i--){
                        if(i < 0){
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        p.sendMessage(plugin.prefix+"§e"+log.id+": §b"+log.category+" §6"+log.time+" §a"+log.fromname+" -> "+log.toname+" §e"+log.cod+"円");
                    }
                    p.sendMessage(plugin.prefix+"§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                });
                return true;
            }else if(args[0].equalsIgnoreCase("viewitem")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    int id = -1;
                    try {
                        id = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(plugin.prefix + "§cidならvalは数字で入力！");
                        return;
                    }
                    if (id <= 0) {
                        p.sendMessage(plugin.prefix + "§cidならvalは1以上の数字で入力！");
                        return;
                    }
                    ArrayList<LogManager.Log> loglist = LogManager.getLogfromid(id);
                    if (loglist.size() == 0) {
                        p.sendMessage(plugin.prefix + "§cログを発見できなかった");
                        return;
                    }
                    int ids = 0;
                    ArrayList<ItemStack> itemlist = new ArrayList<>();
                    for (int i = (loglist.size() - 1); i > (loglist.size() - 15); i--) {
                        if (i < 0) {
                            break;
                        }
                        LogManager.Log log = loglist.get(i);
                        ids = log.id;
                        itemlist = log.items;
                        p.sendMessage(plugin.prefix + "§e" + log.id + ": §b" + log.category + " §6" + log.time + " §a" + log.fromname + " -> " + log.toname + " §e" + log.cod + "円");
                    }
                    p.sendMessage(plugin.prefix + "§c詳しいログはdbにアクセスできる人にidで検索を頼んでください。");
                    Inventory inv = Bukkit.createInventory(null, 9, "view中 id:" + ids);
                    if (itemlist.size() != 0) {
                        for (ItemStack item : itemlist) {
                            if (item != null) {
                                inv.addItem(item);
                            }
                        }
                        plugin.pstats3.put(p.getUniqueId(), "viewLogitem");
                        p.openInventory(inv);
                    } else {
                        p.sendMessage(plugin.prefix + "§cアイテムがこのlogには存在しなかった");
                    }
                });
                return true;
            }

        }
        p.sendMessage("§a/mdvlog [id/fromname/toname/tag/all] [val] §e: ログを見る");
        p.sendMessage("§a/mdvlog viewitem [id] §e: ログを見る");
        return true;
    }
}
