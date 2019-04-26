package red.man10.man10delivery;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MDVData {
    public static MySQLManager mysql;
    public static Man10Delivery plugin;

    public static HashMap<ItemStack,UUID> getitems;

    public static void loadEnable(Man10Delivery plugin, MySQLManager mysql){
        MDVData.plugin = plugin;
        MDVData.mysql = mysql;
        getitems = new HashMap<>();
        AllloadBox();
    }

    public static void addBox(UUID sender, UUID destination, ArrayList<ItemStack> itemlist,ItemStack box,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if(!sqlConnectSafety()){
                return;
            }
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
            LogManager.createLog(LogManager.Category.sendBox,
                    tags,
                    "send simple box",
                    sender.toString(),
                    Bukkit.getOfflinePlayer(sender).getName(),
                    destination.toString(),
                    Bukkit.getOfflinePlayer(destination).getName(),0,itemlist);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void addBox(UUID sender, UUID destination, ArrayList<ItemStack> itemlist,ItemStack box,UUID tag,Double cash){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if(!sqlConnectSafety()){
                return;
            }
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
            LogManager.createLog(LogManager.Category.sendCodBox,
                    tags,
                    "send cod box",
                    sender.toString(),
                    Bukkit.getOfflinePlayer(sender).getName(),
                    destination.toString(),
                    Bukkit.getOfflinePlayer(destination).getName(),cash,itemlist);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void addBox(String sendername, UUID destination, ArrayList<ItemStack> itemlist,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if(!sqlConnectSafety()){
                return;
            }
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            ItemStack items = new ItemStack(plugin.box, 1, (short) plugin.meta);
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
            LogManager.createLog(LogManager.Category.sendBox,
                    tags,
                    "send simple box (OtherPlugin)",
                    "OtherPlugin",
                    sendername,
                    destination.toString(),
                    Bukkit.getOfflinePlayer(destination).getName(),0,itemlist);
            if(Bukkit.getPlayer(destination)!=null){
                sendHoverText(Bukkit.getPlayer(destination),plugin.prefix+"§a§l§n荷物が届きました!!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static void addBox(String sendername, UUID destination, ArrayList<ItemStack> itemlist,UUID tag, double daibiki){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if(!sqlConnectSafety()){
                return;
            }
            if (itemlist.size() > 9 || itemlist.size() == 0) {
                return;
            }
            ItemStack items = new ItemStack(plugin.box, 1, (short) plugin.meta);
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
            LogManager.createLog(LogManager.Category.sendCodBox,
                    tags,
                    "send cod box (OtherPlugin)",
                    "OtherPlugin",
                    sendername,
                    destination.toString(),
                    Bukkit.getOfflinePlayer(destination).getName(),daibiki,itemlist);
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

    public static boolean sqlConnectSafety(){
        if(!mysql.connectCheck()){
            Bukkit.broadcastMessage(plugin.prefix+"§c§lDB接続に失敗したため一時的にMDVを停止します。");
            Bukkit.broadcastMessage(plugin.prefix+"§7§lヤマントの§c§l営業を停止中…");
            plugin.power = false;
            Bukkit.broadcastMessage(plugin.prefix+"§7§l停止完了。");
            return false;
        }
        return true;
    }

    //使う側でスレッド化必須!!!
    public static boolean ContainPlayerBox(UUID uuid){
        String sql = "SELECT * FROM boxs WHERE uuid = '" + uuid.toString() + "' AND gets = " + false + ";";
        ResultSet rs = mysql.query(sql);
        if (rs == null) {
            mysql.close();
            return false;
        }
        try {
            if(rs.next()){
                return true;
            }
        } catch (NullPointerException | SQLException e1) {
            e1.printStackTrace();
            return false;
        }
        mysql.close();
        return false;
    }

    public static void LoginContainBox(Player p){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            if(ContainPlayerBox(p.getUniqueId())) {
                sendHoverText(p,plugin.prefix+"§a§l§n荷物が届いています！!§f§l(クリック)","/mdv check","/mdv check");
            }
        });
    }

    public static UUID checkTonameFromTag(UUID tag){
        String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "' ;";
        ResultSet rs = mysql.query(sql);
        if (rs == null) {
            mysql.close();
            return null;
        }
        try {
            if(rs.next()){
                UUID uuid = UUID.fromString( rs.getString("sender"));
                mysql.close();
                rs.close();
                return uuid;
            }
            mysql.close();
            rs.close();
            return null;
        } catch (NullPointerException | SQLException e1) {
            return null;
        }
    }

    synchronized public static void GetPlayerBox(Player p){
        if(!sqlConnectSafety()){
            p.sendMessage(plugin.prefix+"§cデータベースアクセスに失敗しました。");
            return;
        }
        p.sendMessage(plugin.prefix + "§eセンターに問合せ中です…§6§kaa");
        int kensuu = 0;
        UUID uuid = p.getUniqueId();
        //代引・通常boxを受け取る
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
                        LogManager.createLog(LogManager.Category.getBox,tag.toString(),"get box",null,null,p.getUniqueId().toString(),p.getName(),0,null);
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

    public static void pelloff(Player p,ItemStack item){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String items = itemToBase64(item);
            String sql = "SELECT * FROM boxs WHERE box = '" + items + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                p.sendMessage(plugin.prefix+"§c§l箱ではありません");
                mysql.close();
                return;
            }
            try {
                if(rs.next()) {
                    String tags = rs.getString("tag");
                    UUID tag = UUID.fromString(tags);
                    unloadBox(item);
                    loadBox(item, tag);
                    p.sendMessage(plugin.prefix+"§c§lガムテープを剥がしました！");
                }else{
                    p.sendMessage(plugin.prefix+"§c§l箱ではありません");
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
        });
    }

    public static void AllloadBox(){
        getitems.clear();
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

    synchronized public static void getItemCheck(Player p,UUID tag){
        if(!sqlConnectSafety()){
            p.sendMessage(plugin.prefix+"§cデータベースアクセスに失敗しました。");
            return;
        }
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
                    if(rs.getString("sender").equalsIgnoreCase(p.getUniqueId().toString())){
                        p.sendMessage(plugin.prefix+"§a§lこの段ボールは代引きですが、作った本人のため無料で開きます！");
                        getItem(p,tag);
                    }
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
    }




    synchronized public static void unLockBox(Player p,UUID tag){
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
                        UUID uuid;
                        try {
                            uuid = UUID.fromString(rs.getString("sender"));
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
                            if(Bukkit.getPlayer(uuid)!=null){
                                Bukkit.getPlayer(uuid).sendMessage(plugin.prefix+"§e§l"+p.getDisplayName()+"さんが§a§lあなたの代引きを支払いました。");
                                Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1.0F,1.0F);
                            }
                        }catch (IllegalArgumentException e){
                            if(codbal > plugin.vault.getBalance(p.getUniqueId())){
                                p.sendMessage(plugin.prefix+"§c§lお金が足りません！§e(必要金額: "+new JPYBalanceFormat(codbal).getString()+"円)");
                                return;
                            }
                            plugin.vault.takePlayerMoney(p.getUniqueId(),codbal,TransactionType.UNKNOWN,"mdv cod takemoney");
                            String sqls = "UPDATE boxs SET cod = false where tag = '"+tag.toString()+"';";
                            mysql.execute(sqls);
                            p.sendMessage(plugin.prefix+"§e"+new JPYBalanceFormat(codbal).getString()+"円§a§l支払いました。§f§l(段ボールを右クリックで開きます。)");
                            p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1.0F,1.0F);
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


    synchronized public static void getItem(Player p,UUID tag){
            String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                p.getInventory().setItemInMainHand(null);
                p.sendMessage(plugin.prefix + "§aそのboxはすでに開けられています。");
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
                        ArrayList<ItemStack> itemlist = new ArrayList<>();
                        //one item
                        String result1 = rs.getString("one");
                        ItemStack item1 = itemFromBase64(result1);
                        itemlist.add(item1);
                        if(item1 != null) {
                            p.getInventory().addItem(item1);
                        }
                        //two item
                        String result2 = rs.getString("two");
                        ItemStack item2 = itemFromBase64(result2);
                        itemlist.add(item2);
                        if(item2 != null) {
                            p.getInventory().addItem(item2);
                        }
                        //three item
                        String result3 = rs.getString("three");
                        ItemStack item3 = itemFromBase64(result3);
                        itemlist.add(item3);
                        if(item3 != null) {
                            p.getInventory().addItem(item3);
                        }
                        //four item
                        String result4 = rs.getString("four");
                        ItemStack item4 = itemFromBase64(result4);
                        itemlist.add(item4);
                        if(item4 != null) {
                            p.getInventory().addItem(item4);
                        }
                        //five item
                        String result5 = rs.getString("five");
                        ItemStack item5 = itemFromBase64(result5);
                        itemlist.add(item5);
                        if(item5 != null) {
                            p.getInventory().addItem(item5);
                        }
                        //six item
                        String result6 = rs.getString("six");
                        ItemStack item6 = itemFromBase64(result6);
                        itemlist.add(item6);
                        if(item6 != null) {
                            p.getInventory().addItem(item6);
                        }
                        //seven iteme
                        String result7 = rs.getString("seven");
                        ItemStack item7 = itemFromBase64(result7);
                        itemlist.add(item7);
                        if(item7 != null) {
                            p.getInventory().addItem(item7);
                        }
                        //eight item
                        String result8 = rs.getString("eight");
                        ItemStack item8 = itemFromBase64(result8);
                        itemlist.add(item8);
                        if(item8!= null) {
                            p.getInventory().addItem(item8);
                        }
                        //nine item
                        String result9 = rs.getString("nine");
                        ItemStack item9 = itemFromBase64(result9);
                        itemlist.add(item9);
                        if(item9 != null) {
                            p.getInventory().addItem(item9);
                        }
                        removeBox(tag);
                        p.sendMessage(plugin.prefix + "§a段ボールを開けました。");
                        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN ,1.0F,1.0F);
                        try {
                            UUID sender = UUID.fromString(rs.getString("sender"));
                            if (Bukkit.getPlayer(sender) != null) {
                                Bukkit.getPlayer(sender).sendMessage(plugin.prefix + "§e§l" + p.getDisplayName() + "さんが§a§lあなたの段ボールを開けました。");
                                Bukkit.getPlayer(sender).playSound(Bukkit.getPlayer(sender).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                LogManager.createLog(LogManager.Category.openBox,tag.toString(),"open box",rs.getString("sender"),Bukkit.getPlayer(sender).getName(),p.getUniqueId().toString(),p.getName(),0,itemlist);
                            }else{
                                LogManager.createLog(LogManager.Category.openBox,tag.toString(),"open box",rs.getString("sender"),Bukkit.getOfflinePlayer(sender).getName(),p.getUniqueId().toString(),p.getName(),0,itemlist);
                            }
                        }catch (IllegalArgumentException e){
                            LogManager.createLog(LogManager.Category.openBox,tag.toString(),"open box",rs.getString("sender"),"Empty",p.getUniqueId().toString(),p.getName(),0,itemlist);
                            rs.close();
                            mysql.close();
                            return;
                        }
                    }
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
    }

    public static void viewBox(Player p,UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE tag = '" + uuid.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                p.sendMessage(plugin.prefix + "§aIDを発見できませんでした。");
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    Inventory inv = Bukkit.createInventory(null,9,"§8表示中");
                    boolean get = rs.getBoolean("gets");
                    if (get) {
                        //one item
                        String result1 = rs.getString("one");
                        ItemStack item1 = itemFromBase64(result1);
                        if(item1 != null) {
                            inv.addItem(item1);
                        }
                        //two item
                        String result2 = rs.getString("two");
                        ItemStack item2 = itemFromBase64(result2);
                        if(item2 != null) {
                            inv.addItem(item2);
                        }
                        //three item
                        String result3 = rs.getString("three");
                        ItemStack item3 = itemFromBase64(result3);
                        if(item3 != null) {
                            inv.addItem(item3);
                        }
                        //four item
                        String result4 = rs.getString("four");
                        ItemStack item4 = itemFromBase64(result4);
                        if(item4 != null) {
                            inv.addItem(item4);
                        }
                        //five item
                        String result5 = rs.getString("five");
                        ItemStack item5 = itemFromBase64(result5);
                        if(item5 != null) {
                            inv.addItem(item5);
                        }
                        //six item
                        String result6 = rs.getString("six");
                        ItemStack item6 = itemFromBase64(result6);
                        if(item6 != null) {
                            inv.addItem(item6);
                        }
                        //seven iteme
                        String result7 = rs.getString("seven");
                        ItemStack item7 = itemFromBase64(result7);
                        if(item7 != null) {
                            inv.addItem(item7);
                        }
                        //eight item
                        String result8 = rs.getString("eight");
                        ItemStack item8 = itemFromBase64(result8);
                        if(item8!= null) {
                            inv.addItem(item8);
                        }
                        //nine item
                        String result9 = rs.getString("nine");
                        ItemStack item9 = itemFromBase64(result9);
                        if(item9 != null) {
                            inv.addItem(item9);
                        }
                        p.sendMessage(plugin.prefix + "§a段ボールの中身を開きました。");
                        plugin.pstats3.put(p.getUniqueId(),"view");
                        p.openInventory(inv);
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
            String sql = "DELETE FROM boxs WHERE tag = '" + tag.toString() + "';";
            mysql.execute(sql);
    }

    public static void createUser(String name,UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM userstwo WHERE uuid = '" + uuid.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs != null) {
                try {
                    if(rs.next()) {
                        if(rs.getString("name").equalsIgnoreCase(name)) {
                            String sqls = "UPDATE userstwo SET name = '" + name + "' where uuid = '" + uuid.toString() + "';";
                            mysql.execute(sqls);
                        }
                        rs.close();
                        mysql.close();
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
            mysql.close();
            String sqls = "INSERT INTO userstwo (name,uuid,offline_bal) VALUES ('"+name+"','" + uuid.toString() + "',0.0);";
            mysql.execute(sqls);
        });
    }

    public static void createUser(String name,UUID uuid,double bal){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM userstwo WHERE uuid = '" + uuid.toString() + "';";
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
            String sqls = "INSERT INTO userstwotwo (name,uuid,offline_bal) VALUES ('"+name+"','" + uuid.toString() + "',"+bal+");";
            mysql.execute(sqls);
        });
    }

    //使う側を絶対にスレッド化すること！！
    public static boolean containUser(UUID uuid){
        String sql = "SELECT * FROM userstwotwo WHERE uuid = '" + uuid.toString() + "';";
        ResultSet rs = mysql.query(sql);
        if (rs != null) {
            try {
                if(rs.next()) {
                    mysql.close();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        mysql.close();
        return false;
    }

    //使う側を絶対にスレッド化すること！！
    public static UUID containUser(String name){
        String sql = "SELECT * FROM userstwotwo WHERE name = '" + name + "';";
        ResultSet rs = mysql.query(sql);
        if (rs != null) {
            try {
                if(rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    mysql.close();
                    return uuid;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        mysql.close();
        return null;
    }

    public static void addOfflineBal(UUID uuid,double bal){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
                String sql = "SELECT * FROM userstwo WHERE uuid = '" + uuid.toString() + "';";
                ResultSet rs = mysql.query(sql);
                if (rs == null) {
                    createUser("not found",uuid,bal);
                    mysql.close();
                    return;
                }
                try {
                    if (rs.next()) {
                        String sqls = "UPDATE userstwo SET offline_bal = offline_bal+"+bal+" where uuid = '"+uuid.toString()+"';";
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

    synchronized public static void getOfflineBal(Player p){
            String sql = "SELECT * FROM userstwo WHERE uuid = '" + p.getUniqueId().toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                p.sendMessage(plugin.prefix+"§e§l0円 引き出されました。");
                createUser("not found",p.getUniqueId());
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    double addbal = rs.getDouble("offline_bal");
                    String sqls = "UPDATE userstwo SET offline_bal = 0.0 where uuid = '"+p.getUniqueId().toString()+"';";
                    mysql.execute(sqls);
                    MDVData.plugin.vault.givePlayerMoney(p.getUniqueId(),addbal,TransactionType.UNKNOWN,"mdv get cash");
                    p.sendMessage(plugin.prefix+"§e§l"+new JPYBalanceFormat(addbal).getString() +"円 引き出されました。");
                }else{
                    p.sendMessage(plugin.prefix+"§e§l0円 引き出されました。");
                    createUser("not found",p.getUniqueId());
                }
                rs.close();
            } catch (NullPointerException | SQLException e1) {
                e1.printStackTrace();
                return;
            }
            mysql.close();
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

    public static String convertSQLDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }


}
