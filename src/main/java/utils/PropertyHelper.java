package utils;

public class PropertyHelper {

    private static final class LazyLoader {
        static final PropertyHelper INSTANCE = new PropertyHelper();
    }

    private PropertyHelper() {}

    public static PropertyHelper getInstance() { return LazyLoader.INSTANCE; }

    /**
     *
     * Property will be looked up from System.getenv() first, then System.getProperty()
     *
     * @param key
     * @return
     *    property value as a String
     */
    public String getPropertyCascading(String key) {
        String val = System.getenv(key);
        if (val == null) {
            val = System.getProperty(key);
        }
        return val;
    }
}
