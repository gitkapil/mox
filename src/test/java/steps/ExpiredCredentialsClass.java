package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class ExpiredCredentialsClass {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId = "dca6b1a2-527b-486b-a441-d62c48f6b454";
        DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);
    }
}
