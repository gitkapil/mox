package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileHelper implements BaseStep{


    /**
     *
     * @param filePath: path of the properties file to be read
     */
    public Properties loadPropertiesFile(String filePath){
        Properties properties= new Properties();
        File file = new File(filePath);

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //load properties file
        try {
            properties.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    /**
     *
     * @param prop Properties refernce
     * @param key  the value to be retrieved from the file
     * @returns the value retrieved
     */
    public String getValueFromPropertiesFile(Properties prop, String key){

        return prop.getProperty(key);
    }
}
