# Habit Tracker Backend

This is the backend for the Habit Tracker web application, built using Spring Boot.

## Overview

The backend provides RESTful APIs for managing habits, user data, and statistics. It is designed to work seamlessly with the Angular frontend.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Spring Boot 3.3 or later

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/crraay/habit-tracker-springboot.git
   cd habit-tracker-springboot
   ```
2. Install dependencies:
   ```bash
   mvn install
   ```

## Development

Run the application using Maven:

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`.

## Build

Build the project using Maven:

```bash
mvn clean package
```

The build artifacts will be stored in the `target/` directory.

## Testing

Run the tests using Maven:

```bash
mvn test
```

## API Documentation

API documentation is available at `http://localhost:8080/swagger-ui/index.html` when the server is running.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
