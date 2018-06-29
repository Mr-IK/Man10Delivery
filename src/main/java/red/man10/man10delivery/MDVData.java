package red.man10.man10delivery;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MDVData {
    private static MySQLManager mysql;
    private static Man10Delivery plugin;

    public static HashMap<ItemStack,UUID> getitems;

    public static void loadEnable(Man10Delivery plugin, MySQLManager mysql){
        MDVData.plugin = plugin;
        MDVData.mysql = mysql;
        getitems = new HashMap<>();
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
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sender.toString() + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
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
    public static void addBox(String sendername, UUID destination, ArrayList<ItemStack> itemlist,ItemStack box,UUID tag){
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
            String sql = "INSERT INTO boxs (sender,uuid,tag,gets,box,one,two,three,four,five,six,seven,eight,nine) VALUES ('" + sendername + "','" + destination.toString() + "'" +
                    ",'" + tags + "'" +
                    ",false" +
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

    public static void GetBox(Player destination,UUID tag){
        Bukkit.getScheduler().runTaskAsynchronously(MDVData.plugin, () -> {
            String sql = "SELECT * FROM boxs WHERE tag = '" + tag.toString() + "';";
            ResultSet rs = mysql.query(sql);
            if (rs == null) {
                mysql.close();
                return;
            }
            try {
                if (rs.next()) {
                    String result = rs.getString("box");
                    ItemStack box = itemFromBase64(result);
                    destination.getInventory().addItem(box);
                    Gettrue(tag);
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
                    GetBox(p, tag);
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
                        String result = rs.getString("box");
                        ItemStack box = itemFromBase64(result);
                        unloadBox(box);
                        //one item
                        String result1 = rs.getString("one");
                        ItemStack item1 = itemFromBase64(result1);
                        p.getInventory().addItem(item1);
                        //two item
                        String result2 = rs.getString("two");
                        ItemStack item2 = itemFromBase64(result2);
                        p.getInventory().addItem(item2);
                        //three item
                        String result3 = rs.getString("three");
                        ItemStack item3 = itemFromBase64(result3);
                        p.getInventory().addItem(item3);
                        //four item
                        String result4 = rs.getString("four");
                        ItemStack item4 = itemFromBase64(result4);
                        p.getInventory().addItem(item4);
                        //five item
                        String result5 = rs.getString("five");
                        ItemStack item5 = itemFromBase64(result5);
                        p.getInventory().addItem(item5);
                        //six item
                        String result6 = rs.getString("six");
                        ItemStack item6 = itemFromBase64(result6);
                        p.getInventory().addItem(item6);
                        //seven item
                        String result7 = rs.getString("seven");
                        ItemStack item7 = itemFromBase64(result7);
                        p.getInventory().addItem(item7);
                        //eight item
                        String result8 = rs.getString("eight");
                        ItemStack item8 = itemFromBase64(result8);
                        p.getInventory().addItem(item8);
                        //nine item
                        String result9 = rs.getString("nine");
                        ItemStack item9 = itemFromBase64(result9);
                        p.getInventory().addItem(item9);
                        removeBox(tag);
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
}
