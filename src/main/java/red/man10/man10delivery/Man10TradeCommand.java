package red.man10.man10delivery;

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Man10TradeCommand implements CommandExecutor {

    Man10Delivery plugin;
    private List<String> tradePair = new ArrayList<>();

    public Man10TradeCommand(Man10Delivery plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("mtrade.use")){
            p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
            return true;
        }

        if (args.length == 0){

            p.sendMessage(plugin.prefix+ "§eトレードをするプレイヤーを入力してください");

            return true;
        }

        //////////////
        //permission check
        if (!p.hasPermission("mtrade."+args[0])){
            p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
            return true;

        }

        if (args[0].equalsIgnoreCase("accept")){
            for (String player : tradePair){
                if (player.indexOf(p.getName()) >= 1){
                    String[] s = player.split(",");
                    openInventory(Bukkit.getPlayer(s[0]),Bukkit.getPlayer(s[1]));
                    return true;
                }
            }


            p.sendMessage(plugin.prefix+"§eあなたにトレード申請をした人はいません");
            return true;
        }

        if (!Bukkit.getPlayer(args[0]).isOnline()){
            p.sendMessage(plugin.prefix+"§eトレードする相手の名前が間違えてる、もしくはオフラインです");
            return true;
        }

        checkTrade(Bukkit.getPlayer(args[0]),p);


        return true;
    }

    private void checkTrade(Player p, Player pair){

        tradePair.add(pair.getName()+","+p.getName());

        Bukkit.getScheduler().runTask(plugin, () -> {

            p.sendMessage(plugin.prefix+"§e§l"+pair.getName()+"§r§eからトレード申請が来ています！");
            p.sendMessage(plugin.prefix+ "§e§l/mtrade accept (30秒以上経過すると、キャンセルされます)");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Bukkit.getLogger().info(e.getMessage());
            }


        });

    }

    private void openInventory(Player p1, Player p2){
        Inventory inv = Bukkit.createInventory(null,54,"§e§lトレード");

        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName("");
        is.setItemMeta(meta);

        int[] index = new int[]{5,14,23,32,41,50};

        for(int i : index){
            inv.setItem(i,is);
        }

        is.setType(Material.STAINED_GLASS_PANE);
        is.setDurability((short)5);
        meta.setDisplayName("§a§l完了");
        is.setItemMeta(meta);
        inv.setItem(46,is);
        inv.setItem(47,is);

        is.setDurability((short)14);
        meta.setDisplayName("§4§l取引中止");
        is.setItemMeta(meta);
        inv.setItem(48,is);
        inv.setItem(49,is);

        is.setDurability((short)1);
        meta.setDisplayName("§l相手は取引完了していません");
        is.setItemMeta(meta);
        inv.setItem(51,is);
        inv.setItem(52,is);
        inv.setItem(53,is);
        inv.setItem(54,is);


        is.setType(Material.BLAZE_POWDER);
        meta.setDisplayName("§a§l+10,000$");
        is.setItemMeta(meta);
        inv.setItem(40,is);

        is.setType(Material.INK_SACK);
        is.setDurability((short)10);
        meta.setDisplayName("§a§l+100,000$");
        is.setItemMeta(meta);
        inv.setItem(39,is);

        is.setType(Material.INK_SACK);
        is.setDurability((short)7);
        meta.setDisplayName("§a§l+1,000,000$");
        is.setItemMeta(meta);
        inv.setItem(38,is);

        is.setType(Material.INK_SACK);
        is.setDurability((short)8);
        meta.setDisplayName("§a§l+10,000,000$");
        is.setItemMeta(meta);
        inv.setItem(37,is);


        is.setType(Material.SIGN);
        meta.setDisplayName("§lあなたが支払う金額:§e§l0$");
        is.setItemMeta(meta);
        inv.setItem(43,is);

        meta.setDisplayName("§l相手が支払う金額：§e§l0$");
        is.setItemMeta(meta);
        inv.setItem(44,is);

        meta.setDisplayName(p2.getName());
        inv.setItem(45,is);
        p1.openInventory(inv);

        meta.setDisplayName(p1.getName());
        inv.setItem(45,is);

        p2.openInventory(inv);

    }
}
