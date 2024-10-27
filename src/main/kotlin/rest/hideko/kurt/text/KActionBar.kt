package rest.hideko.kurt.text

import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class KActionBar {
    companion object {
        fun send(player: Player, message: String) {
            val craftPlayer = player as CraftPlayer
            val packet = PacketPlayOutChat(ChatComponentText(message), 2.toByte())
            craftPlayer.handle.playerConnection.sendPacket(packet)
        }
        fun clear(player: Player) {
            send(player, "")
        }
    }
}