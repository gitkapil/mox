package utils;

import apiHelpers.PutApplication;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseConnector {

	final static Logger logger = Logger.getLogger(PutApplication.class);

	public static String executeSQLQuery(String sqlQuery, String connectionURL)
			throws SQLException, ClassNotFoundException {

		Class.forName(Constants.DB_DRIVER);
		Connection connection = DriverManager.getConnection(connectionURL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
		if (connection != null) {
			logger.info("Connected to the database...");
		} else {
			logger.info("Database connection failed to Environment");
		}
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sqlQuery);
		rs.next();
		String resultValue = rs.getString(1).toString();
		connection.close();
		return resultValue;
	}

	public static ArrayList<String> executeSQLQuery_List(String testEnv, String sqlQuery, String connectionURL)
			throws ClassNotFoundException, SQLException {
		ArrayList<String> resultValue = new ArrayList<String>();
		ResultSet resultSet;
		if (testEnv.equalsIgnoreCase("QA"))
		{
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