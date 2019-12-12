package utils;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class General{

    /**
     *
     * @param seconds denotes (seconds)time the current thread will sleep for
     */
    public void waitFor(long seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @returns Unique & random UUID
     */
    public String generateUniqueUUID(){
        return UUID.randomUUID().toString();

    }

      public static String getRandomName(){
        return RandomStringUtils.randomAlphabetic(10);
      }
}
