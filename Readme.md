# dry-cleaning-assignment

---

This project calculate the guaranty time for the customer who will drop the cloths and will get the the cleaned clothes after give guaranty time.

#Get Started
This application is based on maven project and using spring boot it can be run by maven command or directly invoking class "DryCleanGuarantyTimeApplication.java"
and it has Api interface which can be used to call the actual implementation.
Note:- Default start time and end time can be overwrite into application.yml file

#Testing Strategy
This application followed the principle of TDD, user will find all the necessary test cases into class "BusinessHoursCalculatorServiceTest.java"
and user can run test cases by mvn command or by directly invoking class.

#Dependencies
As mentioned above this application based on spring boot so main dependencies are spring artifacts, user need to build the application by using "mvn clean install"
before executing any test case or after importing and building from any java IDE.