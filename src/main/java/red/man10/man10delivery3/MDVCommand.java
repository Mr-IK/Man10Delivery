package red.man10.man10delivery3;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
        if (args.length == 0) {
            p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
            MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
            MDVData.sendHoverText(p, "§e§l/mdv check §f§l: 送られてきたボックスを受け取る", "§aクリックで受け取る", "/mdv check");
            p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("check")) {
                MDVData.GetPlayerBox(p);
                return true;
            }
            if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                p.sendMessage(plugin.prefix + "§cそのプレイヤーは一度もこのサーバーに入っていません！");
                return true;
            }
            Inventory inv = Bukkit.createInventory(null, 9, "§0アイテムを入れて閉じると配達します");
            plugin.pstats.put(p.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId());
            p.openInventory(inv);
            return true;
        }
        p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
        MDVData.sendSuggestCommand(p, "§e§l/mdv [player名] §f§l: プレイヤーにアイテムを送る", "§aクリックでチャットに打ち込む", "/mdv ");
        MDVData.sendHoverText(p, "§e§l/mdv check §f§l: 送られてきたボックスを受け取る", "§aクリックで受け取る", "/mdv check");
        p.sendMessage("§b§l=============§f§lヤマント§e§lヘルプメニュー§b§l=============");
        return true;
    }
}
