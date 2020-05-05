package utils;

public class PropertyHelper {

    private static final class LazyLoader {
        static final PropertyHelper INSTANCE = new PropertyHelper();
    }

    private PropertyHelper() {}

    public static PropertyHelper getInstance() { return LazyLoader.INSTANCE; }

    public String getPropertyCascading(String key) {
        String val = System.getenv(key);
        if (val == null) {
            val = System.getProperty(key);
        }
        return val;
    }
}
