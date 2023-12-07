# Backend Test Automation Assignment

## Requirements
### Environment
* GitHub account
* Java 17 (JDK)
* Maven 3.6+
* Any IDE you comfortable with (eg. IntelliJ, VSCode)

### Skills
* Java 8+ (coding standards)
* Clean Code
* Maven
* Git, GitLab, GitHub

### Instructions
 - Clone this project
 - Create a solution as described under ```doc/QA_BE_Assignment.pdf``` in a feature branch
 - Create a Pull Request to merge your feature branch to main
 - Once PR is closed, kindly let us know

## Documentation
# Gitlab Issues API - Abn Amro Assignment
Rest assured testing framework for Gitlab Issues API https://docs.gitlab.com/ee/api/issues.html
## Getting Started
These instructions will get you a copy of the framework up and running up and running on your local machine for testing process.

## Setting up you local environment
An IDE like VSCode or IntelliJ IDEA is preferable
After cloning the framework you will need to run the following steps:
In your local working directory:

### Install maven dependencies
```
mvn clean install
```
### Create a secret.properties file in your local working directory
The secret file should contain the following parameters ( ou can follow the secret.properties.example file):
```
PRIVATE_ACCESS_TOKEN
PROJECT_ID
```
The private access token can be found in your personal gitlab account.
The project Id is the Id the appears next to the project name on gitlab.

## Run tests
After setting up your secrets file, now you are ready to run the tests.
In the terminal of you IDE run the following command:

```
mvn clean test
```

### Test report
After the test suite is fully executed in the terminal you can view the allure report generated
by running the following command in the terminal of your IDE:

```
allure serve target/allure-results/ 
```
## Notes:

- I pushed my local ```secret.properties``` file to the repo so the workflow can pass, as I didn't have any permissions to add a secret file to `Autograding workflow`.
- This secret file should be ignored as it contains ```private oauth2 token```.
- The user should be able to add his own secrets after cloning the repo.