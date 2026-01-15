package fi.sulku.hytale

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.logger.backend.HytaleConsole
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.hypixel.hytale.server.core.util.Config


class ConsoleSpamFilter(init: JavaPluginInit) : JavaPlugin(init) {

    private val _config: Config<Conf> = withConfig<Conf>(Conf.CODEC)

    val config: Conf
        get() = _config.get()

    override fun setup() {
        super.setup()
        _config.save()

        if (config.isEnabled) {
            replaceFormatter()
        }
    }

    private fun replaceFormatter() {
        val console = HytaleConsole.INSTANCE
        val clazz = console.javaClass

        val shouldPrintAnsi: Boolean = clazz.getDeclaredMethod("shouldPrintAnsi").apply {
            isAccessible = true
        }.invoke(console) as Boolean

        clazz.getDeclaredField("formatter").apply {
            isAccessible = true
            set(console, HytaleLogFilteredFormatter(config) { shouldPrintAnsi })
        }
    }
}

class Conf {
    var isEnabled: Boolean = true
    var blacklist: Array<String> =
        arrayOf("This is example and this will not be seen on console", "You can put your own messages here")

    companion object {
        val CODEC: BuilderCodec<Conf> =
            BuilderCodec.builder(Conf::class.java) { Conf() }
                .append(
                    KeyedCodec("IsEnabled", Codec.BOOLEAN),
                    { config: Conf, isChatColorEnabled: Boolean -> config.isEnabled = isChatColorEnabled },
                    { config: Conf -> config.isEnabled }).add()
                .append(
                    KeyedCodec("Blacklist", Codec.STRING_ARRAY),
                    { config: Conf, blacklist: Array<String> -> config.blacklist = blacklist },
                    { config: Conf -> config.blacklist }).add().build()
    }
}

