package managers;
import apiHelpers.CreateEmployee;
import apiHelpers.DeleteEmployee;
import apiHelpers.GetEmployeeDetails;
import apiHelpers.UpdateEmployeeDetails;

public class ApiManager {

    GetEmployeeDetails getEmployeeDetails;
    CreateEmployee createEmployee;
    UpdateEmployeeDetails updateEmployeeDetails;
    DeleteEmployee deleteEmployee;

    public ApiManager() {
        getEmployeeDetails = new GetEmployeeDetails();
        createEmployee = new CreateEmployee();
        updateEmployeeDetails = new UpdateEmployeeDetails();
        deleteEmployee = new DeleteEmployee();
    }

    public GetEmployeeDetails getEmployeeDetails() {
        return getEmployeeDetails;
    }

    public CreateEmployee createEmployee() {
        return createEmployee;
    }

    public UpdateEmployeeDetails updateEmployeeDetails() {
        return updateEmployeeDetails;
    }

    public DeleteEmployee deleteEmployee(){
        return deleteEmployee;
    }
}

