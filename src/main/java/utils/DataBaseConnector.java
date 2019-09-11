package utils;

import apiHelpers.PutApplication;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;


public class DataBaseConnector {

    final static Logger logger = Logger.getLogger(DataBaseConnector.class);

    public static String executeSQLQuery(String sqlQuery, String connectionURL) {
        String resultValue = null;
        try {
            System.out.println(Constants.DB_DRIVER);
            Class.forName(Constants.DB_DRIVER).newInstance();
            Connection connection = DriverManager.getConnection(connectionURL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
            assertNotEquals(connection, null);
            if (connection != null) {
                logger.info("Connected to the database...");
            } else {
                logger.info("Database connection failed to Environment");
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            rs.next();
            resultValue = rs.getString(1);
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultValue;
    }

    public static ArrayList<String> executeSQLQuery_List(String testEnv, String sqlQuery, String connectionURL)
            throws ClassNotFoundException, SQLException {
        ArrayList<String> resultValue = new ArrayList<String>();
        ResultSet resultSet;
        if (testEnv.equalsIgnoreCase("CI")) {
            Class.forName(Constants.DB_DRIVER);
            Connection connection = DriverManager.getConnection(connectionURL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
            if (connection != null) {
                logger.info("Connected to the database");
            } else {
                logger.info("Failed to connect to " + testEnv + " database");
            }

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                StringBuilder stringBuilder = new StringBuilder();
                for (int iCounter = 1; iCounter <= columnCount; iCounter++) {
                    stringBuilder.append(resultSet.getString(iCounter).trim() + " ");
                }
                String reqValue = stringBuilder.substring(0, stringBuilder.length() - 1);
                resultValue.add(reqValue);
            }
            connection.close();
        }

        return resultValue;
    }


    public static void executeSQLQueryToInsert(String sqlQuery, String connectionURL)
            throws SQLException, ClassNotFoundException {

        Class.forName(Constants.DB_DRIVER);
        Connection connection = DriverManager.getConnection(connectionURL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
        if (connection != null) {
            logger.info("Connected to the database...");
        } else {
            logger.info("Database connection failed to Environment");
        }
        Statement stmt = connection.createStatement();
        stmt.execute(sqlQuery);

        connection.close();

    }


}