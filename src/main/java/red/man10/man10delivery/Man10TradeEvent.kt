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

            if (e.slot == 46 || e.slot == 47) {
                val `is` = ItemStack(Material.STAINED_GLASS_PANE, 1, 4.toShort())
                val meta = `is`.itemMeta
                meta.displayName = "あなたは取引完了しています"
                `is`.itemMeta = meta

                val i = e.inventory
                i.setItem(46, `is`)
                i.setItem(47, `is`)
                i.setItem(48, `is`)
                i.setItem(49, `is`)

                e.whoClicked.openInventory(i)
                return
            }



            Bukkit.getLogger().info("clicked")

        }
    }


}
