package se.utility;

import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

public class CaseFormatUtil {

    private static final CaseFormat CAMEL_CASE = CaseFormat.UPPER_CAMEL;
    private static final CaseFormat SNAKE_CASE = CaseFormat.LOWER_UNDERSCORE;

    public static @NotNull String convertCamelToSnakeCase(String camelCaseString) {
        return CAMEL_CASE.to(SNAKE_CASE, camelCaseString);
    }
}
