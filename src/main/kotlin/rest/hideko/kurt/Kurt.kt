package rest.hideko.kurt

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import rest.hideko.kurt.text.KBossBar

class Kurt(private val plugin: JavaPlugin) {
    companion object {
        lateinit var plugin: JavaPlugin
    }
    init {
        Companion.plugin = plugin
        plugin.server.scheduler.runTaskTimer(plugin, {
            for (bars in KBossBar.players) {
                val get = Bukkit.getPlayer(bars)
                if(get != null) KBossBar.teleport(get)
            }
        }, 0, 20)
    }
    fun events(vararg listeners: Listener): Kurt {
        for (listener in listeners) {
            plugin.server.pluginManager.registerEvents(listener, plugin)
        }
        return this
    }
    fun commands(vararg commands: Pair<String, CommandExecutor>): Kurt {
        for (command in commands) {
            val pluginCommand = plugin.getCommand(command.first)
            pluginCommand.executor = command.second
        }
        return this
    }
}
