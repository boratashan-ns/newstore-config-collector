package nwscore.utils;

import java.io.File;
import java.net.URISyntaxException;


public class ApplicationUtils {
    public static String getJarRunningPath() throws URISyntaxException {
        File f = new File(ApplicationUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        if (f.getPath().endsWith(".jar"))
            return f.getParent();
        else
            return f.getPath();
    }

    public static String getFileNameUnderPath(String path, String nameOfFile) {
        if (path.endsWith("\\") || path.endsWith("/"))
            return path.concat(nameOfFile);
        else
            return path.concat("/").concat(nameOfFile);

    }
}
