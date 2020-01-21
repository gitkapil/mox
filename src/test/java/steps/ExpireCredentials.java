package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class ExpireCredentials {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId ="c8420ade-6ab4-4c66-8fde-0d40dc8ab903";
        String date = "2020-04-20 01:02:02";

        // Future date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

         // Today's date
       // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);
       // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

         DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);
    }

}
