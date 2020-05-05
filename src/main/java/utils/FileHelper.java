package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileHelper {

    public Properties loadPropertiesFile(String filePath) {
        Properties properties = new Properties();
        File file = new File(filePath);

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getValueFromPropertiesFile(Properties prop, String key) {
        return prop.getProperty(key);
    }
}
