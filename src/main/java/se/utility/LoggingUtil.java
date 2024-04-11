package se.utility;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public final class LoggingUtil {

    public static final Supplier<Logger> LOGGER = () -> LogManager.getLogger(LoggingUtil.class);
    public static final ThreadLocal<Logger> TL_LOGGER = ThreadLocal.withInitial(LOGGER);
}