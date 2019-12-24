package steps;

import utils.Constants;
import utils.DataBaseConnector;

import java.sql.SQLException;

public class SampleClass {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String sqlQuery = "update merchant_admin_db.orgn_cust_crdtl c SET C.CRDTL_STAT_CDE = 'E' , C.REC_UPD_DT_TM = CURRENT_TIMESTAMP(), C.CRDTL_EFF_END_DT_TM = CURRENT_TIMESTAMP() , C.USER_REC_UPD_ID ='Script-Testing' Where C.ORGN_CUST_CRDTL_ID = unhex('2EE3E4A5EF454FE2A37DD5FCFC6ADB22')" ;
        DataBaseConnector.executeSQLQueryToInsert(sqlQuery, Constants.DB_CONNECTION_URL);
    }
}
