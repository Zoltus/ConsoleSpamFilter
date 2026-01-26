package fi.sulku.hytale.consolespamfilter

import com.hypixel.hytale.logger.backend.HytaleLogFormatter
import java.util.function.BooleanSupplier
import java.util.logging.LogRecord

class HytaleLogFilteredFormatter(val config: Conf, ansi: BooleanSupplier) : HytaleLogFormatter(ansi) {

    override fun format(record: LogRecord): String {
        for (blacklistItem in config.blacklist) {
            if (record.message.contains(blacklistItem)) {
                return ""
            }
        }
        return super.format(record)
    }
}