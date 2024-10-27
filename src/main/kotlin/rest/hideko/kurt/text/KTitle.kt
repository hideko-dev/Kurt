package rest.hideko.kurt.text

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class KTitle {
    companion object {
        fun send(player: Player, title: String, subtitle: String, fadeIn: Int = 5, stay: Int = 30, fadeOut: Int = 5) {
            title(player, title, fadeIn, stay, fadeOut)
            subtitle(player, subtitle, fadeIn, stay, fadeOut)
        }
        fun title(player: Player, message: String, fadeIn: Int = 5, stay: Int = 30, fadeOut: Int = 5) {
            val craftPlayer = player as CraftPlayer
            val title = PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"$message\"}"), fadeIn, stay, fadeOut)
            craftPlayer.handle.playerConnection.sendPacket(title)
        }
        fun subtitle(player: Player, message: String, fadeIn: Int = 5, stay: Int = 30, fadeOut: Int = 5) {
            val craftPlayer = player as CraftPlayer
            val title = PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\": \"$message\"}"), fadeIn, stay, fadeOut)
            craftPlayer.handle.playerConnection.sendPacket(title)
        }
        fun clear(player: Player) {
            title(player, "", 10, 10, 10)
        }
    }
}