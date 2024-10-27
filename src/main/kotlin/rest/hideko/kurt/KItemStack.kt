package rest.hideko.kurt

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class KItemStack(private val kmaterial: KMaterial) {
    private val itemStack = kmaterial.item()
    private val itemMeta = itemStack.itemMeta
    init {
        itemStack.itemMeta = itemMeta
    }
    fun displayName(name: String): KItemStack {
        itemMeta?.setDisplayName(name)
        itemStack.itemMeta = itemMeta
        return this
    }
    fun lore(vararg lore: String): KItemStack {
        itemMeta?.lore = lore.toList()
        itemStack.itemMeta = itemMeta
        return this
    }
    fun isGlowing(glowing: Boolean): KItemStack {
        if (glowing) {
            itemMeta?.addEnchant(org.bukkit.enchantments.Enchantment.LUCK, 1, true)
            itemMeta?.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
        } else {
            itemMeta?.removeEnchant(org.bukkit.enchantments.Enchantment.LUCK)
            itemMeta?.removeItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS)
        }
        itemStack.itemMeta = itemMeta
        return this
    }
    fun amount(amount: Int): KItemStack {
        itemStack.amount = amount
        return this
    }
    fun enchantments(vararg enchantments: Pair<org.bukkit.enchantments.Enchantment, Int>): KItemStack {
        for ((enchantment, level) in enchantments) {
            itemMeta?.addEnchant(enchantment, level, true)
        }
        itemStack.itemMeta = itemMeta
        return this
    }
    fun build(): ItemStack {
        return itemStack
    }
}
