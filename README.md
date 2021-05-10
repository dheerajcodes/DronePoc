# Automated Testing POC Instructions

## Problem solved:

This test automation solution is a POC to implement the BDD automated testing of the JSON Web APIs.

## Technologies used:

1. Java 8
2. [Cucumber](https://cucumber.io/ "Cucumber home") v6.8.1 for writing the BDD features.
3. [Spring](https://spring.io "Spring home") java development framework.
4. [H2 Database](https://www.h2database.com/ "H2 Database home") for in-memory embedded database.
5. [Rest Assured](https://rest-assured.io/ "Rest Assured home") v4.3.3 for testing and validating REST services in java.
6. [Apache Log4j](https://logging.apache.org/log4j/2.x/ "Log4j home") v1.2.17 for creating logs
7. [Gradle](https://gradle.org/ "Gradle home") as build tool.

## Opening and Compiling Project:

1. [IntelliJ IDEA](https://www.jetbrains.com/idea/ "IntelliJ IDEA home") - Community edition used as IDE to develop this
   solution.
2. To open the project in IntelliJ, browse to 'build.gradle' file (located at '...\DronePoc' and choose option ‘Open as
   Project’). Make sure that project is opened as gradle project in IntelliJ.
3. In IntelliJ, go to File -> Project Structure....Under Project Settings -> Project, make sure that 'Project SDK:'
   selected for Java version is 1.8 or higher and 'Project language level' is 8 or higher.
4. In IntelliJ Project Explorer, verify that under 'External Libraries', JDK and libraries for Cucumber, Spring, H2
   Database, Rest Assured and Log4j are loaded via Gradle (live internet connection required to resolve these
   dependencies, else, if there is a local libraries' server in the organisation, go to File - > Settings...Build,
   Execution, Deployment -> Gradle - > Use Gradle from and choose '
   Specified location').
5. User should have 'write' permissions to the location where '...\DronePoc' directory is located. This is required only
   for creating and writing report and log files at the locations '...\DronePoc\reports' and '...\DronePoc\logs'
   respectively.
6. Ensure that 'Cucumber for Java' and 'Gherkin' plugins are installed in IntelliJ (from File -> Settings... -> Plugins)
   .

## Starting API Server

From IntelliJ :

* Click the 'Run' icon for 'DronePocApplication' class (at '
  ....\DronePoc\src\main\java\com.drone.poc.DronePocApplication).

## Running Tests:

From IntelliJ :

* To run all the tests, click the 'Run Test' icon for 'TestRunner' class (at '
  ....\DronePoc\src\test\java\com.drone.poc.runner.TestRunner).
* To run a particular feature or scenario, click the 'Run Test' icon at feature level or scenario level (feature files
  located at '....\DronePoc\src\test\resources\features\).

#### Note:

API server must me running before running any tests.

## Test Artifacts:

1. **Execution report**: Execution report file 'cucumber-tests.html' will be generated and located at '
   ....\DronePoc\reports'.
2. **Execution logs**: Log file 'cucumber-tests.log' will be generated and located at '....\DronePoc\logs'.