package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class ExpireCredentials {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId ="e6d1a35c-db8f-436a-8d6d-dda890c74c52";
        String date = "2020-04-20 01:02:02";

        // SIT Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        // SIT Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        //SIT Merchant   --- Change credentials status to expired
        // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        //SIT Sandbox ---- Change credentials status to expired
         DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        // CI Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_CI_MERCHANT, Constants.DB_PASSWORD_ADMIN_CI_MERCHANT, Constants.DB_CONNECTION_URL_CI_MERCHANT);

        // CI Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_CI_SANDBOX, Constants.DB_PASSWORD_ADMIN_CI_SANDBOX, Constants.DB_CONNECTION_URL_CI_SANDBOX);

        // PRE Prod Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_PRE_MERCHANT, Constants.DB_PASSWORD_ADMIN_PRE_MERCHANT, Constants.DB_CONNECTION_PRE_CI_MERCHANT);

        // PRE Prod Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_PRE_SANDBOX, Constants.DB_PASSWORD_ADMIN_PRE_SANDBOX, Constants.DB_CONNECTION_URL_PRE_SANDBOX);

    }


}

