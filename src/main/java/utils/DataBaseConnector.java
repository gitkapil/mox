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


    public static void executeSQLQueryToInsert(String sqlQuery, String userName, String password, String connectionURL)
            throws SQLException, ClassNotFoundException {

        Class.forName(Constants.DB_DRIVER);
        Connection connection = DriverManager.getConnection(connectionURL, userName, password);
        if (connection != null) {
            logger.info("Connected to the database...");
        } else {
            logger.info("Database connection failed to Environment");
        }
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sqlQuery);
        connection.close();

    }

    public static void expireCredentialsWithCredentialID(String credentialId, String userName, String password, String connectionURL) throws SQLException, ClassNotFoundException {
        String sqlQuery = "update merchant_management.orgn_cust_crdtl c SET C.CRDTL_STAT_CDE = 'E' , C.REC_UPD_DT_TM = CURRENT_TIMESTAMP(), C.CRDTL_EFF_END_DT_TM = CURRENT_TIMESTAMP() , C.USER_REC_UPD_ID ='Script-Testing' Where C.ORGN_CUST_CRDTL_ID = UNHEX(REPLACE(\""+credentialId+"\", \"-\",\"\"))" ;
        DataBaseConnector.executeSQLQueryToInsert(sqlQuery,userName, password,connectionURL);
    }
}