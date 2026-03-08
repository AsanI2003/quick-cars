QuickCars Rental System <br>
A robust Spring Boot REST API for managing a vehicle rental service. This project demonstrates a complete backend architecture featuring secure authentication, role-based access control, and automated email notifications. <br> <br>

Features <br>
User & Admin Authentication: Secure login and registration using JWT (JSON Web Tokens) and Google OAuth2. <br>
Vehicle Management: Full CRUD operations for vehicles, including categories like Fuel Type, Drive Type, and Vehicle Type.<br>
Rental Workflow: Seamless booking system for users with admin oversight for inquiries.<br>
Automated Notifications: Email integration for rental confirmations and status updates.<br>
Database Persistence: Data-driven design using MySQL and Spring Data JPA.<br> <br>

Tech Stack <br>
Backend: Java 17+, Spring Boot 3.x <br>
Security: Spring Security, JWT, OAuth2<br>
Database: MySQL<br>
ORM: Hibernate / Spring Data JPA<br>
Tools: Maven, Lombok<br> <br>

Installation & Setup<br>
Clone the repository<br>
git clone https://github.com/AsanI2003/quick-cars.git<br>
cd quick-cars <br><br>

Configure Environment Variables(look for application.property files)<br>
To keep the project secure, sensitive keys are not hardcoded. You must set the following environment variables in your IDE (IntelliJ/Eclipse) or System:<br><br>

DB_USERNAME,Your MySQL username <br>
DB_PASSWORD,Your MySQL password<br>
JWT_SECRET,"A long, random string for token signing"<br>
GOOGLE_CLIENT_ID,From Google Cloud Console<br>
GOOGLE_CLIENT_SECRET,From Google Cloud Console<br>
MAIL_USERNAME,Your Gmail address<br>
MAIL_PASSWORD,Your Gmail App Password<br> <br>

Build and Run <br>
mvn clean install <br>
mvn spring-boot:run <br>
The server will start at http://localhost:8080 <br> <br>

Project Structure <br>
src/main/java/com/project/quickcars2/ <br>
├── config/         # Security, CORS, and Mapper configurations <br>
├── controller/     # REST API Endpoints (Admin, User, Vehicle, Rental)<br>
├── dto/            # Data Transfer Objects for clean API requests<br>
├── entity/         # JPA Entities (Database Schema)<br>
├── repository/     # Spring Data JPA Repositories<br>
├── service/        # Business Logic Interfaces <br>
└── util/           # JWT and Security utilities <br> <br>

Security Implementation <br>
This project follows modern security standards:<br>
Stateless Auth: Uses JWT for internal API requests. <br>
OAuth2: Allows users to log in using their Google accounts. <br>
Password Hashing: (If applicable) BCrypt is used for storing local user passwords.<br>
