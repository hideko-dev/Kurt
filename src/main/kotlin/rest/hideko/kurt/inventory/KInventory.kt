package rest.hideko.kurt.inventory

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import rest.hideko.kurt.Kurt

class KInventory(private val title: String, row: Int): Listener {
    private val inventory = Bukkit.createInventory(null, row*9, title)
    private val ignoredSlots = mutableSetOf<Int>()

    init {
        Kurt.plugin.server.pluginManager.registerEvents(this, Kurt.plugin)
    }

    fun inventory(setup: (Turn) -> Unit): KInventory {
        val turn = Turn(inventory)
        setup(turn)
        return this
    }

    inner class Turn(private val inv: Inventory) {
        fun set(slot: Int, item: ItemStack, onClick: (InventoryClickEvent) -> Unit) {
            inv.setItem(slot, item)
            registerClickListener(slot, onClick)
        }
        private fun registerClickListener(slot: Int, onClick: (InventoryClickEvent) -> Unit) {
            ignoredSlots.add(slot)
        }
    }

    fun ignore(vararg slots: Int): KInventory {
        ignoredSlots.addAll(slots.toSet())
        return this
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title == title) {
            if (ignoredSlots.contains(event.slot)) {
                return
            }
            event.isCancelled = true
        }
    }

    fun open(player: Player): KInventory {
        player.openInventory(inventory)
        return this
    }
}