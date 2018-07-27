package red.man10.man10delivery;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import red.man10.man10vaultapiplus.JPYBalanceFormat;
import red.man10.man10vaultapiplus.enums.TransactionCategory;
import red.man10.man10vaultapiplus.enums.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class MletterCommand implements CommandExecutor {
    Man10Delivery plugin;
    public MletterCommand(Man10Delivery plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mdv.use")) {
            p.sendMessage(plugin.prefix + "§cあなたには権限がありません！");
            return true;
        }
        if (!plugin.power && !p.hasPermission("mdv.op")) {
            p.sendMessage(plugin.prefix + "§c動作停止中");
            return true;
        }
        if (args.length == 0 || args.length == 1) {
            MDVData.sendSuggestCommand(p, "§e§l/mletter [題名] [内容(;で改行)] §f§l: 手紙を作る§6(内容はカラコとスペース使えます)", "§aクリックでチャットに打ち込む", "/mletter ");
        }else{
            if (plugin.vault.getBalance(p.getUniqueId()) < plugin.fee) {
                p.sendMessage(plugin.prefix + "§cお金が足りないため作成を中止しました。§e(必要: "+new JPYBalanceFormat(plugin.fee).getString()+"円)");
                return true;
            }
            String title = "§6§l"+args[0];
            String lore = "";
            for(int i = 1; i < args.length; i++){
                if(lore.equalsIgnoreCase("")){
                    lore = args[i];
                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append(lore);
                    sb.append(" ");
                    sb.append(args[i]);
                    lore = new String(sb);
                }
            }
            List<String> k = new ArrayList<>();
            k.add("§e発送元: "+p.getName());
            for(String m:ChatColor.translateAlternateColorCodes('&',lore).split(";")) {
                k.add("§f"+m);
            }
            ItemStack item = new ItemStack(Material.DIAMOND_HOE,1,(short)857);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DAMAGE_ALL,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(title);
            meta.setLore(k);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
            p.getInventory().addItem(item);
            plugin.vault.transferMoneyPlayerToCountry(p.getUniqueId(),plugin.fee,TransactionCategory.TAX,TransactionType.FEE,"mletter fee");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        LogManager.createLog(LogManager.Category.createLetter, null,
                                "create letter",
                                p.getUniqueId().toString(),
                                p.getName(),
                                null,
                                null,
                                0,
                                null
                        );
            });
            p.sendMessage(plugin.prefix + "§a手紙作成完了！");
            return true;
        }
        return true;
    }
}
