package red.man10.man10delivery;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class MDVAPI {
    public static void sendPlayertoBox(String sendername, UUID Destination , ArrayList<ItemStack> list,UUID tag){
        MDVData.addBox(sendername,Destination,list,tag);
    }

    public static void sendPlayertoBox(String sendername, UUID Destination , ArrayList<ItemStack> list,UUID tag,double daibiki){
        MDVData.addBox(sendername,Destination,list,tag,daibiki);
    }

    public static boolean checkOfflinePlayer(OfflinePlayer p){
        UUID uuid = p.getUniqueId();
        return MDVData.containUser(uuid);
    }
}
