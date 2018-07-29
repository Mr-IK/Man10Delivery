package red.man10.man10delivery;

import org.bukkit.plugin.java.JavaPlugin;
import red.man10.man10quest.api.QuestAPI;
import red.man10.man10quest.data.Man10QuestData;

import java.util.UUID;

public class QuestManager {
    JavaPlugin plugin;
    public QuestManager(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void createQuest(){
        int id = QuestAPI.createQuest(null,"no_open_hide","MDVで始めてアイテムを送ろう"
                ,"mdv sendbox","§a/mdv 名前 で他の人にアイテムを送ろう！",
                "§e§lあなたはmdvで初めてアイテムを送ろうを達成しました！","single",1000000,null,null);
        if(id == -1){
            return;
        }
        plugin.getConfig().set("quest",id);
        plugin.saveConfig();
    }

    public void openQuest(String name,UUID uuid){
        QuestAPI.createPlayerQuest(plugin.getConfig().getInt("quest"),"MDVで始めてアイテムを送ろう","mdv sendbox",name,uuid.toString(),"empty",false);
    }

    public void clearQuest(UUID uuid){
        Man10QuestData data = QuestAPI.getPlayerQuestfromQuestId(uuid,plugin.getConfig().getInt("quest"));
        if(data == null){
            return;
        }
        QuestAPI.successQuest(data);
    }
}
