# Expense Tracker

## Author
**Rahul Kolli**

## Overview
This is a Spring Boot-based expense tracker application that helps users manage their expenses efficiently. The application provides functionalities to add, update, delete, and view expenses, along with category management. I have leveraged Spring Boot, Spring JPA, Hibernate, PostgreSQL, and Spring Security for this project.

## Technologies Used
- **Backend:** Spring Boot, Java
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Version Control:** Git
- **Testing:** JUnit

## Features
- User authentication and authorization
- Expense management (CRUD operations)
- Category management
- Search and filter expenses
- Detailed logging and error handling

## Setup Instructions
### Prerequisites
- Java 17+
- PostgreSQL installed and configured
- Maven installed
- Git installed

### Steps to Run the Application
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd expense-tracker
   ```
2. Configure the PostgreSQL database in `application.properties`.
3. Build the application:
   ```sh
   mvn clean install
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Contribution
Feel free to fork the repository and raise a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License.
