# My Java API

This project is a basic Spring Boot application that serves as a template for building RESTful APIs in Java.

## Project Structure

```
my-java-api
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── App.java
│   │   │           └── controller
│   │   │               └── ApiController.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── AppTest.java
│       └── resources
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd my-java-api
   ```

2. **Build the project:**
   ```
   mvn clean install
   ```

3. **Run the application:**
   ```
   mvn spring-boot:run
   ```

## Usage

- The application exposes RESTful endpoints through the `ApiController` class.
- You can send GET and POST requests to interact with the API.

## Testing

- Unit tests are located in the `src/test/java/com/example/AppTest.java` file.
- Run the tests using:
  ```
  mvn test
  ```

## Dependencies

This project uses Maven for dependency management. Check the `pom.xml` file for the list of dependencies.