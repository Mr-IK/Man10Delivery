package red.man10.man10delivery;

import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogManager {
    static Man10Delivery plugin;

    public static void loadEnable(Man10Delivery plugin){
        LogManager.plugin = plugin;
    }

    static class Category{
        static String sendBox = "send_box";
        static String openBox = "open_box";
        static String createLetter = "create_letter";
        static String getBox = "get_box";
        static String sendCodBox = "send_cod_box";
        static String sendTimeBox = "send_time_box";
    }

    static class Log{
        int id;
        String category;
        String tag;
        String time;
        String memo;
        String fromuuid;
        String fromname;
        String touuid;
        String toname;
        double cod;
        ArrayList<ItemStack> items;
        public Log(int id,String category,String tag,
                   String time,String memo,
                   String fromuuid,String fromname,
                   String touuid, String toname,ArrayList<ItemStack> itemlist,double cod){
            this.id = id;
            this.category = category;
            this.tag = tag;
            this.time = time;
            this.memo = memo;
            this.fromuuid = fromuuid;
            this.fromname = fromname;
            this.touuid = touuid;
            this.toname = toname;
            this.items = itemlist;
            this.cod = cod;
        }
    }


    public static boolean createLog(String category,String tag,String memo,String fromuuid,
                                 String fromname,String touuid,String toname,double cod,ArrayList<ItemStack> items){
        if(category == null){
            category = "Other";
        }
        if(tag == null){
            tag = "Empty";
        }
        if(memo== null){
            memo = "Empty";
        }
        if(fromuuid == null){
            fromuuid = "Empty";
        }
        if(fromname== null){
            fromname = "Empty";
        }
        if(touuid== null){
            touuid = "Empty";
        }
        if(toname== null){
            toname = "Empty";
        }
        if(items == null){
            items = new ArrayList<>();
        }
        if(items.size() < 9){
            for(int i = items.size(); i <= 9; i++){
                items.add(null);
            }
        }
        ArrayList<String> itemlist = new ArrayList<>();
        for(int i = 0;i < 9;i++){
            if(items.get(i) != null) {
                itemlist.add(MDVData.itemToBase64(items.get(i)));
            }else{
                itemlist.add("Empty");
            }
        }
        String sql = "INSERT INTO logs (category,tag,memo,from_uuid,from_name,to_uuid,to_name,cod,one,two,three,four,five,six,seven,eight,nine) VALUES (" +
                "'"+category+"'," +
                "'"+tag+"'," +
                "'"+memo+"'," +
                "'"+fromuuid+"'," +
                "'"+fromname+"'," +
                "'"+touuid+"'," +
                "'"+toname+"'," +
                ""+cod+"," +
                "'"+itemlist.get(0)+"'," +
                "'"+itemlist.get(1)+"'," +
                "'"+itemlist.get(2)+"'," +
                "'"+itemlist.get(3)+"'," +
                "'"+itemlist.get(4)+"'," +
                "'"+itemlist.get(5)+"'," +
                "'"+itemlist.get(6)+"'," +
                "'"+itemlist.get(7)+"'," +
                "'"+itemlist.get(8)+"'" +
                ");";
        return MDVData.mysql.execute(sql);
    }

    public static ArrayList<Log> getLogfromTag(String tag){
        String sql = "SELECT * FROM logs WHERE tag = '" + tag + "';";
        ResultSet rs = MDVData.mysql.query(sql);
        ArrayList<Log> loglist = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    if(!rs.getString("one").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("one")));
                    }
                    if(!rs.getString("two").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("two")));
                    }
                    if(!rs.getString("three").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("three")));
                    }
                    if(!rs.getString("four").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("four")));
                    }
                    if(!rs.getString("five").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("five")));
                    }
                    if(!rs.getString("six").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("six")));
                    }
                    if(!rs.getString("seven").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("seven")));
                    }
                    if(!rs.getString("eight").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("eight")));
                    }
                    if(!rs.getString("nine").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("nine")));
                    }
                    Log log = new Log(rs.getInt("id"),rs.getString("category"),tag,
                            rs.getString("time"),
                            rs.getString("memo"),
                            rs.getString("from_uuid"),
                            rs.getString("from_name"),
                            rs.getString("to_uuid"),
                            rs.getString("to_name"),
                            list,
                            rs.getDouble("cod")
                            );
                    loglist.add(log);
                }
            } catch (SQLException e) {
                MDVData.mysql.close();
                return loglist;
            }
        }
        MDVData.mysql.close();
        return loglist;
    }

    public static ArrayList<Log> getLogfrom_fromname(String from_name){
        String sql = "SELECT * FROM logs WHERE from_name = '" + from_name+ "';";
        ResultSet rs = MDVData.mysql.query(sql);
        ArrayList<Log> loglist = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    if(!rs.getString("one").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("one")));
                    }
                    if(!rs.getString("two").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("two")));
                    }
                    if(!rs.getString("three").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("three")));
                    }
                    if(!rs.getString("four").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("four")));
                    }
                    if(!rs.getString("five").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("five")));
                    }
                    if(!rs.getString("six").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("six")));
                    }
                    if(!rs.getString("seven").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("seven")));
                    }
                    if(!rs.getString("eight").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("eight")));
                    }
                    if(!rs.getString("nine").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("nine")));
                    }
                    Log log = new Log(rs.getInt("id"),rs.getString("category"),rs.getString("tag"),
                            rs.getString("time"),
                            rs.getString("memo"),
                            rs.getString("from_uuid"),
                            from_name,
                            rs.getString("to_uuid"),
                            rs.getString("to_name"),
                            list,
                            rs.getDouble("cod")
                    );
                    loglist.add(log);
                }
            } catch (SQLException e) {
                MDVData.mysql.close();
                return loglist;
            }
        }
        MDVData.mysql.close();
        return loglist;
    }

    public static ArrayList<Log> getLogfrom_toname(String to_name){
        String sql = "SELECT * FROM logs WHERE to_name = '" + to_name+ "';";
        ResultSet rs = MDVData.mysql.query(sql);
        ArrayList<Log> loglist = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    if(!rs.getString("one").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("one")));
                    }
                    if(!rs.getString("two").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("two")));
                    }
                    if(!rs.getString("three").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("three")));
                    }
                    if(!rs.getString("four").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("four")));
                    }
                    if(!rs.getString("five").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("five")));
                    }
                    if(!rs.getString("six").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("six")));
                    }
                    if(!rs.getString("seven").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("seven")));
                    }
                    if(!rs.getString("eight").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("eight")));
                    }
                    if(!rs.getString("nine").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("nine")));
                    }
                    Log log = new Log(rs.getInt("id"),rs.getString("category"),rs.getString("tag"),
                            rs.getString("time"),
                            rs.getString("memo"),
                            rs.getString("from_uuid"),
                            rs.getString("from_name"),
                            rs.getString("to_uuid"),
                            to_name,
                            list,
                            rs.getDouble("cod")
                    );
                    loglist.add(log);
                }
            } catch (SQLException e) {
                MDVData.mysql.close();
                return loglist;
            }
        }
        MDVData.mysql.close();
        return loglist;
    }

    public static ArrayList<Log> getLogfromid(int id){
        String sql = "SELECT * FROM logs WHERE id = '" + id + "';";
        ResultSet rs = MDVData.mysql.query(sql);
        ArrayList<Log> loglist = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    if(!rs.getString("one").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("one")));
                    }
                    if(!rs.getString("two").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("two")));
                    }
                    if(!rs.getString("three").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("three")));
                    }
                    if(!rs.getString("four").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("four")));
                    }
                    if(!rs.getString("five").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("five")));
                    }
                    if(!rs.getString("six").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("six")));
                    }
                    if(!rs.getString("seven").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("seven")));
                    }
                    if(!rs.getString("eight").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("eight")));
                    }
                    if(!rs.getString("nine").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("nine")));
                    }
                    Log log = new Log(id,rs.getString("category"),rs.getString("tag"),
                            rs.getString("time"),
                            rs.getString("memo"),
                            rs.getString("from_uuid"),
                            rs.getString("from_name"),
                            rs.getString("to_uuid"),
                            rs.getString("to_name"),
                            list,
                            rs.getDouble("cod")
                    );
                    loglist.add(log);
                }
            } catch (SQLException e) {
                MDVData.mysql.close();
                return loglist;
            }
        }
        MDVData.mysql.close();
        return loglist;
    }

    public static ArrayList<Log> getLogfromall(){
        String sql = "SELECT * FROM logs ;";
        ResultSet rs = MDVData.mysql.query(sql);
        ArrayList<Log> loglist = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    if(!rs.getString("one").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("one")));
                    }
                    if(!rs.getString("two").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("two")));
                    }
                    if(!rs.getString("three").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("three")));
                    }
                    if(!rs.getString("four").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("four")));
                    }
                    if(!rs.getString("five").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("five")));
                    }
                    if(!rs.getString("six").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("six")));
                    }
                    if(!rs.getString("seven").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("seven")));
                    }
                    if(!rs.getString("eight").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("eight")));
                    }
                    if(!rs.getString("nine").equalsIgnoreCase("Empty")) {
                        list.add(MDVData.itemFromBase64(rs.getString("nine")));
                    }
                    Log log = new Log(rs.getInt("id"),rs.getString("category"),rs.getString("tag"),
                            rs.getString("time"),
                            rs.getString("memo"),
                            rs.getString("from_uuid"),
                            rs.getString("from_name"),
                            rs.getString("to_uuid"),
                            rs.getString("to_name"),
                            list,
                            rs.getDouble("cod")
                    );
                    loglist.add(log);
                }
            } catch (SQLException e) {
                MDVData.mysql.close();
                return loglist;
            }
        }
        MDVData.mysql.close();
        return loglist;
    }

}
