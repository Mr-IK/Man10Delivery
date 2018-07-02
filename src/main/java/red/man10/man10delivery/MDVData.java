package red.man10.man10delivery;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import red.man10.man10vaultapiplus.JPYBalanceFormat;
import red.man10.man10vaultapiplus.enums.TransactionType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MDVData {
    private static MySQLManager mysql;
    private static Man10Delivery plugin;

    public static HashMap<ItemStack,UUID> getitems;
    private static HashMap<UUID,String> SQLWait;

    public static void loadEnable(Man10Delivery plugin, MySQLManager mysql){
        MDVData.plugin = plugin;
        MDVData.mysql = mysql;
        getitems = new HashMap<>();
        SQLWait = new HashMap<>();
        AllloadBox();
    }

    public static void addBox(UUID sender, UUID destination, ArrayList<ItemStack> itemlist,ItemStack box,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            String boxs = itemToBase64(box);
            String tags = tag.toString();
            String[] list = new String[9];
            int count = 0;
            for (ItemStack i : itemlist) {
                list[count] = itemToBase64(i);
                count++;
            }
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,cod,codbal,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sender.toString() + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
                    ",false" +
                    ",0" +
                    ",'" + boxs + "'" +
                    ",'" + list[0] + "'" +
                    ",'" + list[1] + "'" +
                    ",'" + list[2] + "'" +
                    ",'" + list[3] + "'" +
                    ",'" + list[4] + "'" +
                    ",'" + list[5] + "'" +
                    ",'" + list[6] + "'" +
                    ",'" + list[7] + "'" +
                    ",'" + list[8] + "' );";
            mysql.execute(sql);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }
    public static void addBox(UUID sender, UUID destination, ArrayList<ItemStack> itemlist,ItemStack box,UUID tag,Double cash){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            String boxs = itemToBase64(box);
            String tags = tag.toString();
            String[] list = new String[9];
            int count = 0;
            for (ItemStack i : itemlist) {
                list[count] = itemToBase64(i);
                count++;
            }
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,cod,codbal,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sender.toString() + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
                    ",true" +
                    ","+cash+"" +
                    ",'" + boxs + "'" +
                    ",'" + list[0] + "'" +
                    ",'" + list[1] + "'" +
                    ",'" + list[2] + "'" +
                    ",'" + list[3] + "'" +
                    ",'" + list[4] + "'" +
                    ",'" + list[5] + "'" +
                    ",'" + list[6] + "'" +
                    ",'" + list[7] + "'" +
                    ",'" + list[8] + "' );";
            mysql.execute(sql);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void addBox(String sendername, UUID destination, ArrayList<ItemStack> itemlist,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            ItemStack items = new ItemStack(Material.DIAMOND_HOE,1,(short)48);
            ItemMeta itemmeta = items.getItemMeta();
            itemmeta.setDisplayName("§2§l[§f段ボール§6箱§2§l]§7(右クリック)§r");
            List<String> k = new ArrayList<String>();
            k.add("§6送り主: §f"+sendername);
            String name = null;
            if(Bukkit.getPlayer(destination)==null) {
                OfflinePlayer pp = Bukkit.getOfflinePlayer(destination);
                name = pp.getName();
            }else {
                Player pp = Bukkit.getPlayer(destination);
                name = pp.getName();
            }
            k.add("§e届け先: §f"+name);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E曜日 a H時mm分ss秒");
            k.add("§a配達日時: §f"+sdf.format(c.getTime()));
            k.add("§c注: インベントリに空きが");
            k.add("§cあるときにクリックしてください!");
            itemmeta.setLore(k);
            itemmeta.setUnbreakable(true);
            itemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            items.setItemMeta(itemmeta);
            String boxs = itemToBase64(items);
            String tags = tag.toString();
            String[] list = new String[9];
            int count = 0;
            for (ItemStack i : itemlist) {
                list[count] = itemToBase64(i);
                count++;
            }
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,cod,codbal,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sendername + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
                    ",false" +
                    ",0" +
                    ",'" + boxs + "'" +
                    ",'" + list[0] + "'" +
                    ",'" + list[1] + "'" +
                    ",'" + list[2] + "'" +
                    ",'" + list[3] + "'" +
                    ",'" + list[4] + "'" +
                    ",'" + list[5] + "'" +
                    ",'" + list[6] + "'" +
                    ",'" + list[7] + "'" +
                    ",'" + list[8] + "' );";
            mysql.execute(sql);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void addBox(String sendername, UUID destination, ArrayList<ItemStack> itemlist,UUID tag, double daibiki){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            ItemStack items = new ItemStack(Material.DIAMOND_HOE,1,(short)48);
            ItemMeta itemmeta = items.getItemMeta();
            itemmeta.setDisplayName("§2§l[§f段ボール§6箱§2§l]§7(右クリック)§r");
            List<String> k = new ArrayList<String>();
            k.add("§6送り主: §f"+sendername);
            String name = null;
            if(Bukkit.getPlayer(destination)==null) {
                OfflinePlayer pp = Bukkit.getOfflinePlayer(destination);
                name = pp.getName();
            }else {
                Player pp = Bukkit.getPlayer(destination);
                name = pp.getName();
            }
            k.add("§e届け先: §f"+name);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E曜日 a H時mm分ss秒");
            k.add("§a配達日時: §f"+sdf.format(c.getTime()));
            k.add("§e代引: "+ new JPYBalanceFormat(daibiki).getString()+"円");
            k.add("§c注: インベントリに空きが");
            k.add("§cあるときにクリックしてください!");
            itemmeta.setLore(k);
            itemmeta.setUnbreakable(true);
            itemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            items.setItemMeta(itemmeta);
            String boxs = itemToBase64(items);
            String tags = tag.toString();
            String[] list = new String[9];
            int count = 0;
            for (ItemStack i : itemlist) {
                list[count] = itemToBase64(i);
                count++;
            }
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,cod,codbal,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sendername + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
                    ",true" +
                    ","+daibiki+"" +
                    ",'" + boxs + "'" +
                    ",'" + list[0] + "'" +
                    ",'" + list[1] + "'" +
                    ",'" + list[2] + "'" +
                    ",'" + list[3] + "'" +
                    ",'" + list[4] + "'" +
                    ",'" + list[5] + "'" +
                    ",'" + list[6] + "'" +
                    ",'" + list[7] + "'" +
                    ",'" + list[8] + "' );";
            mysql.execute(sql);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました！!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void Gettrue(UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "UPDATE boxs SET gets = true WHERE tag = '" + tag.toString() + "';";
            mysql.execute(sql);
        });
    }

    public static void GetPlayerBox(Player p){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            p.sendMessage(plugin.prefix + "§eセンターに問合せ中です…§6§kaa");
            int kensuu = 0;
            UUID uuid = p.getUniqueId();
            String sql = "SELECT * FROM boxs WHERE uuid = '" + uuid.toString() + "' AND gets = " + false + ";";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                p.sendMessage(plugin.prefix + "§e荷物はありませんでした。");
                return;
            }
            try {
                while (rs.next()) {
                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(plugin.prefix + "§cインベントリがいっぱいになったので中止しました。");
                        mysql.close();
                        p.sendMessage(plugin.prefix + "§e" + kensuu + "§6個の荷物を受け取りました。");
                        return;
                    }
                    kensuu++;
                    UUID tag = UUID.fromString(rs.getString("tag"));
                    String sqls = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
                    ResultSet rss = mysql.query(sqls);
                    if (rss == null) {
                        mysql.close();
                        return;
                    }
                    try {
                        if (rss.next()) {
                            String result = rss.getString("box");
                            ItemStack box = itemFromBase64(result);
                            p.getInventory().addItem(box);
                            Gettrue(tag);
                            loadBox(box, tag);
                        }
                        rss.close();
                    } catch (NullPointerException | SQLException e1) {
                        e1.printStackTrace();
                        return;
                    }
                    mysql.close();
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
            p.sendMessage(plugin.prefix + "§e" + kensuu + "§6個の荷物を受け取りました。");
        });
    }

    public static void GetPlayerInfo(Player p,UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            p.sendMessage(plugin.prefix + "§eセンターに問合せ中です…§6§kaa");
            int kensuu = 0;
            String sql = "SELECT * FROM boxs WHERE uuid = '" + uuid.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                p.sendMessage(plugin.prefix + "§e受取記録はありませんでした。");
                return;
            }else {
                try {
                    while (rs.next()) {
                        kensuu++;
                        p.sendMessage(plugin.prefix+"§c記録"+kensuu+": 受取 ["+rs.getString("tag")+"] 受け取り済み: "+rs.getBoolean("gets"));
                    }
                    rs.close();
                } catch (NullPointerException | SQLException e1) {
                    e1.printStackTrace();
                    return;
                }
                mysql.close();
            }
            p.sendMessage(plugin.prefix + "§e受取履歴の確認完了。 送信履歴を検索中…");
            String sqls = "SELECT * FROM boxs WHERE sender = '" + uuid.toString() + "';";
            ResultSet rss = mysql.query(sqls);
            if (rss == null) {
                mysql.close();
                p.sendMessage(plugin.prefix + "§e送信記録はありませんでした。");
                p.sendMessage(plugin.prefix + "§e合計" + kensuu + "§6個の記録を発見しました。");
                return;
            }
            try {
                while (rss.next()) {
                    kensuu++;
                    p.sendMessage(plugin.prefix+"§c記録"+kensuu+": 送信["+rss.getString("tag")+"] 受け取り済み: "+rss.getBoolean("gets"));
                }
                rss.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
            p.sendMessage(plugin.prefix + "§e送信履歴の確認完了。");
            p.sendMessage(plugin.prefix + "§e" + kensuu + "§6個の記録を発見しました。");
        });
    }

    public static void loadBox(ItemStack item,UUID tag){
        getitems.put(item,tag);
    }

    public static void AllloadBox(){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE gets = " + true + ";";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                return;
            }
            try {
                while (rs.next()) {
                    String result = rs.getString("box");
                    ItemStack box = itemFromBase64(result);
                    unloadBox(box);
                    String tags = rs.getString("tag");
                    UUID tag = UUID.fromString(tags);
                    loadBox(box, tag);
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }


    public static void unloadBox(ItemStack item){
        getitems.remove(item);
    }

    public static void getItemCheck(Player p,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    boolean cod = rs.getBoolean("cod");
                    if(cod){
                        sendHoverText(p,plugin.prefix + "§c§lこの段ボールは代引です。支払いますか？§f§l(ここをクリック!)","§cクリックで支払い!","/mdv unlock");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE ,1.0F,1.0F);
                    }else{
                        getItem(p,tag);
                    }
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }

    public static void unLockBox(Player p,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    boolean cod = rs.getBoolean("cod");
                    if(cod){
                        double codbal = rs.getDouble("codbal");
                        UUID uuid = UUID.fromString(rs.getString("sender"));
                        if(codbal > plugin.vault.getBalance(p.getUniqueId())){
                            p.sendMessage(plugin.prefix+"§c§lお金が足りません！§e(必要金額: "+new JPYBalanceFormat(codbal).getString()+"円)");
                            return;
                        }
                        plugin.vault.takePlayerMoney(p.getUniqueId(),codbal,TransactionType.UNKNOWN,"mdv cod takemoney");
                        addOfflineBal(uuid,codbal);
                        String sqls = "UPDATE boxs SET cod = false where tag = '"+tag.toString()+"';";
                        mysql.execute(sqls);
                        p.sendMessage(plugin.prefix+"§e"+new JPYBalanceFormat(codbal).getString()+"円§a§l支払いました。§f§l(段ボールを右クリックで開きます。)");
                        p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1.0F,1.0F);
                        UUID sender = UUID.fromString(rs.getString("sender"));
                        if(Bukkit.getPlayer(sender)!=null){
                            Bukkit.getPlayer(sender).sendMessage(plugin.prefix+"§e§l"+p.getDisplayName()+"さんが§a§lあなたの代引きを支払いました。");
                            Bukkit.getPlayer(sender).playSound(Bukkit.getPlayer(sender).getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1.0F,1.0F);
                        }
                    }else{
                        p.sendMessage(plugin.prefix+"§a§lそのボックスは代引ではありません");
                    }
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }


    public static void getItem(Player p,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    boolean get = rs.getBoolean("gets");
                    if (get) {
                        p.getInventory().setItemInMainHand(null);
                        String result = rs.getString("box");
                        ItemStack box = itemFromBase64(result);
                        unloadBox(box);
                        //one item
                        String result1 = rs.getString("one");
                        ItemStack item1 = itemFromBase64(result1);
                        if(item1 != null) {
                            p.getInventory().addItem(item1);
                        }
                        //two item
                        String result2 = rs.getString("two");
                        ItemStack item2 = itemFromBase64(result2);
                        if(item2 != null) {
                            p.getInventory().addItem(item2);
                        }
                        //three item
                        String result3 = rs.getString("three");
                        ItemStack item3 = itemFromBase64(result3);
                        if(item3 != null) {
                            p.getInventory().addItem(item3);
                        }
                        //four item
                        String result4 = rs.getString("four");
                        ItemStack item4 = itemFromBase64(result4);
                        if(item4 != null) {
                            p.getInventory().addItem(item4);
                        }
                        //five item
                        String result5 = rs.getString("five");
                        ItemStack item5 = itemFromBase64(result2);
                        if(item5 != null) {
                            p.getInventory().addItem(item5);
                        }
                        //six item
                        String result6 = rs.getString("six");
                        ItemStack item6 = itemFromBase64(result6);
                        if(item6 != null) {
                            p.getInventory().addItem(item6);
                        }
                        //seven item
                        String result7 = rs.getString("seven");
                        ItemStack item7 = itemFromBase64(result7);
                        if(item7 != null) {
                            p.getInventory().addItem(item7);
                        }
                        //eight item
                        String result8 = rs.getString("eight");
                        ItemStack item8 = itemFromBase64(result8);
                        if(item8!= null) {
                            p.getInventory().addItem(item8);
                        }
                        //nine item
                        String result9 = rs.getString("nine");
                        ItemStack item9 = itemFromBase64(result9);
                        if(item9 != null) {
                            p.getInventory().addItem(item9);
                        }
                        removeBox(tag);
                        p.sendMessage(plugin.prefix + "§a段ボールを開けました。");
                        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN ,1.0F,1.0F);
                        UUID sender = UUID.fromString(rs.getString("sender"));
                        if(Bukkit.getPlayer(sender)!=null){
                            Bukkit.getPlayer(sender).sendMessage(plugin.prefix+"§e§l"+p.getDisplayName()+"さんが§a§lあなたの段ボールを開けました。");
                            Bukkit.getPlayer(sender).playSound(Bukkit.getPlayer(sender).getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1.0F,1.0F);
                        }
                    }
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }

    public static void removeBox(UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "DELETE FROM boxs WHERE tag = '" + tag.toString() + "';";
            mysql.execute(sql);
        });
    }

    public static void createUser(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM users WHERE uuid = '" + uuid.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs != null) {
                try {
                    if(rs.next()) {
                        mysql.close();
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
            mysql.close();
            String sqls = "INSERT INTO users (uuid,offline_bal) VALUES ('" + uuid.toString() + "',0.0);";
            mysql.execute(sqls);
        });
    }

    public static void createUser(UUID uuid,double bal){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM users WHERE uuid = '" + uuid.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs != null) {
                try {
                    if(rs.next()) {
                        mysql.close();
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
            mysql.close();
            String sqls = "INSERT INTO users (uuid,offline_bal) VALUES ('" + uuid.toString() + "',"+bal+");";
            mysql.execute(sqls);
        });
    }

    public static void addOfflineBal(UUID uuid,double bal){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
                String sql = "SELECT * FROM users WHERE uuid = '" + uuid.toString() + "';";
                ResultSet rs = mysql.query(sql);
                if (rs == null) {
                    createUser(uuid,bal);
                    mysql.close();
                    return;
                }
                try {
                    if (rs.next()) {
                        String sqls = "UPDATE users SET offline_bal = offline_bal+"+bal+" where uuid = '"+uuid.toString()+"';";
                        mysql.execute(sqls);
                    }
                    rs.close();
                } catch (NullPointerException | SQLException e1) {
                    e1.printStackTrace();
                    return;
                }
                mysql.close();
            });

        });
    }

    public static void getOfflineBal(Player p){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM users WHERE uuid = '" + p.getUniqueId().toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                p.sendMessage(plugin.prefix+"§e§l0円 引き出されました。");
                createUser(p.getUniqueId());
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    double addbal = rs.getDouble("offline_bal");
                    String sqls = "UPDATE users SET offline_bal = 0.0 where uuid = '"+p.getUniqueId().toString()+"';";
                    mysql.execute(sqls);
                    MDVData.plugin.vault.givePlayerMoney(p.getUniqueId(),addbal,TransactionType.UNKNOWN,"mdv get cash");
                    p.sendMessage(plugin.prefix+"§e§l"+new JPYBalanceFormat(addbal).getString() +"円 引き出されました。");
                }else{
                    p.sendMessage(plugin.prefix+"§e§l0円 引き出されました。");
                    createUser(p.getUniqueId());
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }






    public static ItemStack itemFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items[0];
        } catch (Exception e) {
            return null;
        }
    }

    public static String itemToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            ItemStack[] items = new ItemStack[1];
            items[0] = item;
            dataOutput.writeInt(items.length);

            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドを設定する関数
    // [例1] sendHoverText(player,"ここをクリック",null,"/say おはまん");
    // [例2] sendHoverText(player,"カーソルをあわせて","ヘルプメッセージとか",null);
    // [例3] sendHoverText(player,"カーソルをあわせてクリック","ヘルプメッセージとか","/say おはまん");
    public static void sendHoverText(Player p,String text,String hoverText,String command){
        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,command);
        }

        BaseComponent[] message = new ComponentBuilder(text).event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドサジェストを設定する
    public static void sendSuggestCommand(Player p,String text,String hoverText,String command){

        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND ,command);
        }

        BaseComponent[] message = new ComponentBuilder(text). event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    public static void addSQLWait(Player p){
        SQLWait.put(p.getUniqueId(),"waiting…");
    }

    public static boolean containSQLWait(Player p){
        return SQLWait.containsKey(p.getUniqueId());
    }

    public static void removeSQLWait(Player p){
        SQLWait.remove(p.getUniqueId());
    }

}
