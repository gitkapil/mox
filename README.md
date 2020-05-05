# MOX TEST HARNESS
=======================

## How to Run ##
------------------
mvn clean verify -Pregression -Dcucumber.options="--tags @test"


## Report ##
----------
Cucumber-Maven-Reporting is used to generate reports
moxAssignment/target/cucumber-reports/cucumber-html-reports


## PreRequisites ##
--------------------

- Java 8
- Every env should have a config file under test/ resources
- All the values explained below should be present within the config file
- All the test cases need to be tagged with relevant profiles/ tags



