# TestsApp_Spring

Web application that supports the functionality according to the task

**Task:**

The student registers in the system and after registration can pass one or more Tests. There is a list of tests in the system. For the list it is necessary to implement:
- choice of tests on a particular subject;
- sorting tests by name;
- sorting tests by difficulty;
- sorting tests by number of queries.
The student chooses the test and passes it. A certain period of time is allocated for passing the test, which is set for each test separately. The student has a personal account, which displays registration information, as well as a list of passed tests with the results.
The system administrator:
- creates, deletes or edits tests;
- blocks, unblocks, edits the user.
When creating a test, the administrator:
- sets the test time;
- sets the complexity of the test;
- adds Questions to the test.
A question can have one or more correct answers. The result of the test is the percentage of questions that the student answered correctly in relation to the total number of questions (it is considered that the student answered the question correctly if his answer coincides exactly with the correct answers).

**Technical details:**

- The application is structured according to the **MVC** and **Spring Boot** architecture.
- To access data, **Hibernate** is used.
- Data is stored in the relational database (**MySQL**).
- Application supports **2 languages**: English and Ukrainian (it is possible to switch the interface language on every page). **Spring Resource Bundle** is used.
- **Post/Redirect/Get(PRG)** pattern is integrated.
- Authentication/authorization of users is implemented via **Spring Authorization**.
- **Password encryption** is implemented.
- Event Logging is implemented using the **log4j library**.
- Application is covered by unit tests and integration tests using **Mockito**.
- **Pagination** mechanism is implemented.
- All input fields have **data validation**.
- Application responds correctly to errors and exceptional situations of various kinds (**final user does not see the stack trace on the client side**).
- **Project Lombok** is used.
- **ThymeLeaf** is used.
