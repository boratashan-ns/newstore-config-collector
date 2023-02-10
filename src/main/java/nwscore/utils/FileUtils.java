package nwscore.utils;

import java.io.File;


public class FileUtils {
    public static boolean isFileExisted(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static String addPathToFileNameIfNeeded(String path, String fileName) {
        if(!(isFileNameWithoutPath(fileName)))
            return fileName;
        else {
            if(path.endsWith("/")||path.endsWith("\\"))
                return path.concat(fileName);
            else
                return path.concat("/").concat(fileName);
        }
    }

    public static boolean isFileNameWithoutPath(String fileName) {
        return (!(fileName.contains("\\") || fileName.contains("/")));
    }
}
