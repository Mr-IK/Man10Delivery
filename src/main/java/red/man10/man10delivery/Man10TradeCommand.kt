package red.man10.man10delivery

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util.ArrayList

class Man10TradeCommand(internal var plugin: Man10Delivery) : CommandExecutor {
    private val tradePair = ArrayList<Pair<Player,Player>>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return true
        }

        if (!sender.hasPermission("mtrade.use")) {
            sender.sendMessage(plugin.prefix + "§cあなたには権限がありません！")
            return true
        }

        if (args.isEmpty()) {

            sender.sendMessage(plugin.prefix + "§eトレードをするプレイヤーを入力してください")

            return true
        }

        //////////////
        //permission check
        if (!sender.hasPermission("mtrade." + args[0])) {
            sender.sendMessage(plugin.prefix + "§cあなたには権限がありません！")
            return true

        }

        if (args[0] == "accept") {
            for (player in tradePair) {
                if (player == sender){
                    openInventory(player.first,player.second)
                    return true
                }
            }

            sender.sendMessage(plugin.prefix + "§eあなたにトレード申請をした人はいません")
            return true
        }

        if (!Bukkit.getPlayer(args[0]).isOnline) {
            sender.sendMessage(plugin.prefix + "§eトレードする相手の名前が間違えてる、もしくはオフラインです")
            return true
        }
        for (player in tradePair) {
            if (player == sender){
                sender.sendMessage(plugin.prefix+"§eあなたは既に他のプレイヤーにトレード申請をしています")
            }
        }

        checkTrade(Bukkit.getPlayer(args[0]), sender)


        return true
    }

    private fun checkTrade(p: Player, pair: Player) {

        tradePair.add(Pair(pair,p))

        Bukkit.getScheduler().runTask(plugin) {

            p.sendMessage(plugin.prefix + "§e§l" + pair.name + "§r§eからトレード申請が来ています！")
            p.sendMessage(plugin.prefix + "§e§l/mtrade accept (30秒以上経過すると、キャンセルされます)")

            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Bukkit.getLogger().info(e.message)
            }


        }

    }

    private fun openInventory(p1: Player, p2: Player) {
        val inv = Bukkit.createInventory(null, 54, "§e§lトレード")

        val stack = ItemStack(Material.STAINED_GLASS_PANE, 1)
        val meta = stack.itemMeta
        meta.displayName = ""
        stack.itemMeta = meta

        val index = intArrayOf(5, 14, 23, 32, 41, 50)

        for (i in index) {
            inv.setItem(i, stack)
        }

        stack.type = Material.STAINED_GLASS_PANE
        stack.durability = 5.toShort()
        meta.displayName = "§a§l完了"
        stack.itemMeta = meta
        inv.setItem(46, stack)
        inv.setItem(47, stack)

        stack.durability = 14.toShort()
        meta.displayName = "§4§l取引中止"
        stack.itemMeta = meta
        inv.setItem(48, stack)
        inv.setItem(49, stack)

        stack.durability = 1.toShort()
        meta.displayName = "§l相手は取引完了していません"
        stack.itemMeta = meta
        inv.setItem(51, stack)
        inv.setItem(52, stack)
        inv.setItem(53, stack)
        inv.setItem(54, stack)


        stack.type = Material.BLAZE_POWDER
        meta.displayName = "§a§l+10,000$"
        stack.itemMeta = meta
        inv.setItem(40, stack)

        stack.type = Material.INK_SACK
        stack.durability = 10.toShort()
        meta.displayName = "§a§l+100,000$"
        stack.itemMeta = meta
        inv.setItem(39, stack)

        stack.type = Material.INK_SACK
        stack.durability = 7.toShort()
        meta.displayName = "§a§l+1,000,000$"
        stack.itemMeta = meta
        inv.setItem(38, stack)

        stack.type = Material.INK_SACK
        stack.durability = 8.toShort()
        meta.displayName = "§a§l+10,000,000$"
        stack.itemMeta = meta
        inv.setItem(37, stack)


        stack.type = Material.SIGN
        meta.displayName = "§lあなたが支払う金額:§e§l0$"
        stack.itemMeta = meta
        inv.setItem(43, stack)

        meta.displayName = "§l相手が支払う金額：§e§l0$"
        stack.itemMeta = meta
        inv.setItem(44, stack)

        meta.displayName = p2.name
        inv.setItem(45, stack)
        p1.openInventory(inv)

        meta.displayName = p1.name
        inv.setItem(45, stack)

        p2.openInventory(inv)

    }
}
