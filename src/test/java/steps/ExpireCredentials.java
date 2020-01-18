package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;
import java.util.Date;

public class ExpireCredentials {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String credentialId ="c350eb17-ecef-47b3-ad5f-f26ca28ba90c";
        Date expireAt =null;
        DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);
       // DataBaseConnector.expireCredentialsWithCredentialIDAndDate(credentialId, expireAt, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);
       // DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);
    }
}
