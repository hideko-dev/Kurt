package rest.hideko.kurt.text

import net.minecraft.server.v1_8_R3.*
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

object KBossBar {
    private val dragons: MutableMap<String, EntityEnderDragon> = ConcurrentHashMap()
    fun send(player: Player, text: String, healthPercent: Float = 100f) {
        val craftPlayer = player as CraftPlayer
        val location = player.location
        val world = (player.location.world as CraftWorld).handle
        val dragon = EntityEnderDragon(world)
        dragon.setLocation(location.x, location.y - 100, location.z, 0f, 0f)
        val packet = PacketPlayOutSpawnEntityLiving(dragon)
        val watcher = DataWatcher(null)
        watcher.a(0, 0x20.toByte())
        watcher.a(6, (healthPercent * 200) / 100)
        watcher.a(10, text)
        watcher.a(2, text)
        watcher.a(11, 1.toByte())
        watcher.a(3, 1.toByte())
        try {
            val t = PacketPlayOutSpawnEntityLiving::class.java.getDeclaredField("l")
            t.isAccessible = true
            t[packet] = watcher
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        dragons[player.name] = dragon
        craftPlayer.handle.playerConnection.sendPacket(packet)
    }
    fun clear(player: Player) {
        val craftPlayer = player as CraftPlayer
        if (dragons.containsKey(player.name)) {
            val packet = PacketPlayOutEntityDestroy(dragons[player.name]!!.id)
            dragons.remove(player.name)
            craftPlayer.handle.playerConnection.sendPacket(packet)
        }
    }
    fun teleport(player: Player) {
        val craftPlayer = player as CraftPlayer
        if (dragons.containsKey(player.name)) {
            val location = player.location
            val packet = PacketPlayOutEntityTeleport(
                dragons[player.name]!!.id,
                location.x.toInt() * 32, (location.y - 100).toInt() * 32,
                location.z.toInt() * 32, (location.yaw.toInt() * 256 / 360).toByte(),
                (location.pitch.toInt() * 256 / 360).toByte(),
                false
            )
            craftPlayer.handle.playerConnection.sendPacket(packet)
        }
    }
    fun updateText(player: Player, text: String?) {
        updateBar(player, text, -1f)
    }
    fun updateHealth(player: Player, healthPercent: Float) {
        updateBar(player, null, healthPercent)
    }
    fun updateBar(player: Player, text: String?, healthPercent: Float) {
        val craftPlayer = player as CraftPlayer
        if (dragons.containsKey(player.name)) {
            val watcher = DataWatcher(null)
            watcher.a(0, 0x20.toByte())
            if (healthPercent != -1f) watcher.a(6, (healthPercent * 200) / 100)
            if (text != null) {
                watcher.a(10, text)
                watcher.a(2, text)
            }
            watcher.a(11, 1.toByte())
            watcher.a(3, 1.toByte())
            val packet = PacketPlayOutEntityMetadata(dragons[player.name]!!.id, watcher, true)
            craftPlayer.handle.playerConnection.sendPacket(packet)
        }
    }
    val players: Set<String> get() {
        val set: MutableSet<String> = HashSet()
        for ((key) in dragons) set.add(key)
        return set
    }
}