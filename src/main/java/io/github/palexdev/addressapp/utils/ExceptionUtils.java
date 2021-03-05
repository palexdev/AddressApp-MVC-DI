package io.github.palexdev.addressapp.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    private static final StringWriter sw = new StringWriter();

    private ExceptionUtils() {}

    public static String getStackTraceString(Throwable ex) {
        sw.flush();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
