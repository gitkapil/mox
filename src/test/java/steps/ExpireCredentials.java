package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class ExpireCredentials {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId ="9d04cf5d-c9d1-4af8-af9e-bfecab34977d";

        // UAT merchant


// Sandbox
         //String date = "2020-01-24 02:22:22";   // today expired
        // String date = "2020-01-30 04:44:44";    // all notification
       // String date = "2020-04-23 07:17:17";    // 90 days expired
       // String date = "2020-02-27 08:27:27";    // 4 reminder + action required

// merchant
        //  String date = "2020-01-24 11:11:11";   // today expired
        // String date = "2020-01-30 03:33:33";    // all notification
       //    String date = "2020-04-23 06:55:55";    // 90 days expired
          String date = "2020-02-27 01:08:58";    // 4 reminder + action required





        // SIT Merchant --- set expiry date
      //   DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);


        // SIT Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);


        // SIT Sandbox --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        //SIT Merchant   --- Change credentials status to expired
      //   DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

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


      //UAT sandbox
       // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId,date, Constants.DB_USERNAME_ADMIN_UAT1_SANDBOX, Constants.DB_PASSWORD_ADMIN_UAT1_SANDBOX, Constants.DB_CONNECTION_URL_UAT1_SANDBOX);

        //UAT -merchant

        // UAT1 Merchant --- set expiry date
        // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_UAT1_MERCHANT, Constants.DB_PASSWORD_ADMIN_UAT1_MERCHANT, Constants.DB_CONNECTION_URL_UAT1_MERCHANT);

        // UAT1 Sandbox --- set expiry date
         DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, date, Constants.DB_USERNAME_ADMIN_UAT1_SANDBOX, Constants.DB_PASSWORD_ADMIN_UAT1_SANDBOX, Constants.DB_CONNECTION_URL_UAT1_SANDBOX);

    }


}

