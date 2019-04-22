package red.man10.man10delivery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class Man10TradeCommand implements CommandExecutor {

    Man10Delivery plugin;
    HashMap<Player,Player> pair = new HashMap<>();

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
            for(Player key : pair.keySet()){
                if (pair.get(key) == p){
                    Bukkit.getLogger().info("trade start");
                    openInventory(p,key);
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

    void checkTrade(Player p,Player pair){

        Bukkit.getScheduler().runTask(plugin, () -> {

            p.sendMessage(plugin.prefix+"§e§l"+pair.getName()+"§r§eからトレード申請が来ています！");
            p.sendMessage(plugin.prefix+ "§e§l/mtrade accept (30秒以上経過すると、キャンセルされます)");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Bukkit.getLogger().info(e.getMessage());
            }

            if (!this.pair.containsKey(pair)){
                pair.sendMessage("§e§l30秒経過したため、トレードがキャンセルされました");
            }

        });

    }

    void openInventory(Player p1,Player p2){
        Inventory inv = Bukkit.createInventory(null,54,"§e§lトレード");

        ItemStack panel1 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 0);
        ItemMeta p1m = panel1.getItemMeta();
        p1m.setDisplayName("");
        panel1.setItemMeta(p1m);

        ItemStack panel2 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 5);
        ItemMeta p2m = panel2.getItemMeta();
        p2m.setDisplayName("§a§l完了");
        panel2.setItemMeta(p2m);

        ItemStack panel3 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 14);
        ItemMeta p3m = panel3.getItemMeta();
        p3m.setDisplayName("§a§l取引中止(強制的に中止します)");
        panel3.setItemMeta(p3m);

        ItemStack panel4 = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 14);
        ItemMeta p4m = panel4.getItemMeta();
        p4m.setDisplayName("相手はまだ完了を押していません");
        panel4.setItemMeta(p3m);

        ItemStack money = new ItemStack(Material.BLAZE_POWDER,1,(short)0);
        ItemMeta m = money.getItemMeta();
        m.setDisplayName("§e§l10000$追加");
        money.setItemMeta(m);

        ItemStack moneyP = new ItemStack(Material.BLAZE_POWDER,1,(short)0);
        ItemMeta mP = moneyP.getItemMeta();
        mP.setDisplayName("§e§l0$");
        moneyP.setItemMeta(mP);

        ItemStack sign = new ItemStack(Material.SIGN,1,(short)0);
        ItemMeta s = sign.getItemMeta();
        s.setDisplayName("§e§l0$");
        sign.setItemMeta(s);

        int[] index = new int[]{5,14,23,32,37,38,39,40,41,42,43,44,45,50};

        for(int i : index){
            inv.setItem(i,panel1);
        }

        inv.setItem(46,panel2);
        inv.setItem(47,panel2);
        inv.setItem(48,panel3);
        inv.setItem(49,panel3);
        inv.setItem(51,panel4);
        inv.setItem(52,panel4);
        inv.setItem(53,panel4);
        inv.setItem(54,panel4);
        inv.setItem(31,money);
        inv.setItem(33,moneyP);
        inv.setItem(30,sign);

        p1.openInventory(inv);
        p2.openInventory(inv);

    }
}
