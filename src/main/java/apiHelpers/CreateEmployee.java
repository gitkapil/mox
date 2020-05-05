package apiHelpers;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import managers.UtilManager;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.HashMap;


public class CreateEmployee extends UtilManager {
    final static Logger logger = Logger.getLogger(CreateEmployee.class);
    private Response response = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private String salary;
    private String age;
    private RequestSpecification request = null;
    private HashMap requestBody = new HashMap();
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeRequest(String url) {
        returnRequestBody();
        response = getRestHelper().postRequestWithBody(url, requestBody );
        logger.info("********** get response *********** \n" + response.prettyPrint());
    }

    private HashMap<String, HashMap> returnRequestBody() {
        requestBody.clear();
        populateRequestBody("name", getName());
        populateRequestBody("salary", getSalary());
        populateRequestBody("age", getAge());
        return requestBody;

    }

    private void populateRequestBody(String field, String value) {
        if (!"".equals(value)) {
            if (!"no_value".equals(value)) {
                requestBody.put(field, value);
            } else {
                requestBody.put(field, "");
            }
        }
    }
}

