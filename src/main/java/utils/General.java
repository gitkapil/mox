package utils;


public class General implements BaseStep{

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


}
