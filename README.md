QuickCars Rental System
A robust Spring Boot REST API for managing a vehicle rental service. This project demonstrates a complete backend architecture featuring secure authentication, role-based access control, and automated email notifications.

Features
User & Admin Authentication: Secure login and registration using JWT (JSON Web Tokens) and Google OAuth2.
Vehicle Management: Full CRUD operations for vehicles, including categories like Fuel Type, Drive Type, and Vehicle Type.
Rental Workflow: Seamless booking system for users with admin oversight for inquiries.
Automated Notifications: Email integration for rental confirmations and status updates.
Database Persistence: Data-driven design using MySQL and Spring Data JPA.

Tech Stack
Backend: Java 17+, Spring Boot 3.x
Security: Spring Security, JWT, OAuth2
Database: MySQL
ORM: Hibernate / Spring Data JPA
Tools: Maven, Lombok

Installation & Setup
Clone the repository
git clone https://github.com/AsanI2003/quick-cars.git
cd quick-cars

Configure Environment Variables(look for application.property files)
To keep the project secure, sensitive keys are not hardcoded. You must set the following environment variables in your IDE (IntelliJ/Eclipse) or System:

DB_USERNAME,Your MySQL username
DB_PASSWORD,Your MySQL password
JWT_SECRET,"A long, random string for token signing"
GOOGLE_CLIENT_ID,From Google Cloud Console
GOOGLE_CLIENT_SECRET,From Google Cloud Console
MAIL_USERNAME,Your Gmail address
MAIL_PASSWORD,Your Gmail App Password

Build and Run
mvn clean install
mvn spring-boot:run
The server will start at http://localhost:8080

Project Structure
src/main/java/com/project/quickcars2/
├── config/         # Security, CORS, and Mapper configurations
├── controller/     # REST API Endpoints (Admin, User, Vehicle, Rental)
├── dto/            # Data Transfer Objects for clean API requests
├── entity/         # JPA Entities (Database Schema)
├── repository/     # Spring Data JPA Repositories
├── service/        # Business Logic Interfaces
└── util/           # JWT and Security utilities

Security Implementation
This project follows modern security standards:
Stateless Auth: Uses JWT for internal API requests.
OAuth2: Allows users to log in using their Google accounts.
Password Hashing: (If applicable) BCrypt is used for storing local user passwords.
