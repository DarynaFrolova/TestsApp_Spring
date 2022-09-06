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

**Preview:**

<img width="300" src="https://user-images.githubusercontent.com/95927190/188604648-6a268a44-6b40-45c4-bc00-83f52cb2f4be.png"> <img width="300" src="https://user-images.githubusercontent.com/95927190/188604892-a013b237-11da-416d-a42e-9bbe17965921.png"> <img width="400" src="https://user-images.githubusercontent.com/95927190/188605240-ea876ddc-43c4-4820-82ec-4499f4671278.png"> <img width="500" src="https://user-images.githubusercontent.com/95927190/188605435-32bfa110-4da5-43ed-80c6-d3f1adec36be.png"> <img width="200" src="https://user-images.githubusercontent.com/95927190/188606626-21f7e55f-9bef-4ce1-958c-8c44cff7b69e.png"> <img width="200" alt="Снимок экрана 2022-09-06 в 11 59 40" src="https://user-images.githubusercontent.com/95927190/188606857-a8126d17-2248-4500-ac25-26acb1eb662d.png">
