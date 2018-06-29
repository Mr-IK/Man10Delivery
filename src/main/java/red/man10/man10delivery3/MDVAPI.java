package red.man10.man10delivery3;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class MDVAPI {
    public static void sendPlayertoBox(String sendername, UUID Destination , ArrayList<ItemStack> list,ItemStack box,UUID tag){
        MDVData.addBox(sendername,Destination,list,box,tag);
    }
}
