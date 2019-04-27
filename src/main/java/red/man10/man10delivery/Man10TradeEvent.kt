package red.man10.man10delivery

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class Man10TradeEvent(internal var plugin: Man10Delivery) : Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.inventory.name == "§e§lトレード") {
            e.isCancelled = true

            val player = e.whoClicked as Player

            ///////////////////
            //取引完了
            ////////////////
            if (e.slot in 46..47) {
                if (plugin.cmd.isFinished[player]!!){
                    return
                }
                plugin.cmd.clickFinish(player)
            }
            ///////////////////
            //取引中止
            ////////////////
            if (e.slot == 48 || e.slot == 49) {
            }

            ////////////////////
            //値段指定
            /////////////////////
            if(e.slot in 37..40){
                plugin.cmd.addMoney(player,e.slot)
            }


            Bukkit.getLogger().info("clicked")

        }
    }



}
