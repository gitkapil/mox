package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class ExpireCredentials {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId = "9d7ca24a-b13b-41ad-915c-6f4b8306d5c8";
        String date = "2020-01-29 03:03:03";

        // SIT Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        // SIT Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        //SIT Merchant   --- Change credentials status to expired
        // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        //SIT Sandbox ---- Change credentials status to expired
        // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        // CI Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_CI_MERCHANT, Constants.DB_PASSWORD_ADMIN_CI_MERCHANT, Constants.DB_CONNECTION_URL_CI_MERCHANT);

        // CI Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_CI_SANDBOX, Constants.DB_PASSWORD_ADMIN_CI_SANDBOX, Constants.DB_CONNECTION_URL_CI_SANDBOX);

        // PRE Prod Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_PRE_MERCHANT, Constants.DB_PASSWORD_ADMIN_PRE_MERCHANT, Constants.DB_CONNECTION_PRE_CI_MERCHANT);

        // PRE Prod Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_PRE_SANDBOX, Constants.DB_PASSWORD_ADMIN_PRE_SANDBOX, Constants.DB_CONNECTION_URL_PRE_SANDBOX);

        // UAT1 Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_UAT1_MERCHANT, Constants.DB_PASSWORD_ADMIN_UAT1_MERCHANT, Constants.DB_CONNECTION_URL_UAT1_MERCHANT);

        // UAT1 Sandbox --- set expiry date
         DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_UAT1_SANDBOX, Constants.DB_PASSWORD_ADMIN_UAT1_SANDBOX, Constants.DB_CONNECTION_URL_UAT1_SANDBOX);
    }


}

