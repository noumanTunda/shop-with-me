# Shop-With-Me: Spring Boot E-Commerce API

A robust, production-ready e-commerce backend API built with Spring Boot, featuring user authentication, product management, shopping cart, order processing, and Stripe payment integration.

## Features

- **User Authentication**: JWT-based authentication with role-based access control (USER/ADMIN)
- **Product Management**: CRUD operations for products with categories
- **Shopping Cart**: Add/update/remove items from cart
- **Order Processing**: Create and manage orders with payment integration
- **Payment Integration**: Stripe payment gateway with webhook handling
- **Admin Dashboard**: Admin-specific endpoints for management
- **Database Migrations**: Flyway for version-controlled database schema management
- **API Documentation**: OpenAPI/Swagger integration

## Tech Stack

- **Java**: 21
- **Spring Boot**: 3.4.1
- **Database**: MySQL 11.8
- **ORM**: Spring Data JPA with Hibernate
- **Migration**: Flyway
- **Security**: Spring Security with JWT (jjwt)
- **Payment**: Stripe API
- **Build Tool**: Maven
- **Mapping**: MapStruct
- **Validation**: Spring Validation

## Project Structure

```
src/main/java/com/tundalabs/store/
├── config/          # Security, JWT configuration
├── controllers/     # REST API endpoints
├── dtos/           # Data Transfer Objects
├── entities/       # JPA entities
├── exceptions/     # Custom exceptions
├── filters/        # JWT authentication filter
├── mappers/        # MapStruct mappers
├── payments/       # Stripe payment integration
├── repositories/   # Spring Data JPA repositories
├── services/       # Business logic layer
└── validation/     # Custom validators
```

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- MySQL 8.0+ (for local development)
- Stripe account (for payment features)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/noumanTunda/shop-with-me.git
cd shop-with-me
```

### 2. Configure environment variables

Copy the example environment file and configure it:

```bash
cp .env.example .env
```

Edit `.env` with your configuration:

```env
# JWT Configuration
JWT_SECRET=32-or-64-Characters-Long-String

# Stripe Configuration
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
STRIPE_WEBHOOK_SECRET_KEY=whsec_your_webhook_secret

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
MYSQLUSER=root
MYSQLPASSWORD=your_password
```

### 3. Database setup

The application uses Flyway for database migrations. To run migrations locally:

```bash
# Load environment variables and run migrations
./mvnw initialize flyway:migrate
```

For development, you can also check migration status:

```bash
./mvnw initialize flyway:info
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Main API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/refresh` - Refresh JWT token

### Users
- `POST /users` - Register new user
- `GET /users/{id}` - Get user by ID
- `PUT /users/{id}` - Update user
- `PUT /users/{id}/password` - Change password

### Products
- `GET /products` - List all products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create product (Admin)
- `PUT /products/{id}` - Update product (Admin)
- `DELETE /products/{id}` - Delete product (Admin)

### Cart
- `GET /carts/{userId}` - Get user's cart
- `POST /carts/items` - Add item to cart
- `PUT /carts/items` - Update cart item
- `DELETE /carts/items/{itemId}` - Remove item from cart

### Orders
- `POST /orders` - Create order from cart
- `GET /orders/{id}` - Get order by ID
- `GET /orders/user/{userId}` - Get user's orders

### Admin
- `GET /admin/users` - List all users (Admin)
- `GET /admin/orders` - List all orders (Admin)

### Payments
- `POST /checkout/create-checkout-session` - Create Stripe checkout session
- `POST /checkout/webhook` - Stripe webhook handler

## Deployment

### Railway Deployment

1. **Push to GitHub**: Ensure your code is pushed to a GitHub repository

2. **Create Railway Project**: 
   - Go to Railway dashboard
   - Click "New Project" → "Deploy from GitHub repo"
   - Select your repository

3. **Configure Environment Variables** in Railway:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://railway-host:3306/railway-database
   MYSQLUSER=railway_username
   MYSQLPASSWORD=railway_password
   JWT_SECRET=your_jwt_secret
   STRIPE_SECRET_KEY=your_stripe_secret_key
   STRIPE_WEBHOOK_SECRET_KEY=your_webhook_secret
   ```

4. **Set Build Command**:
   ```
   ./mvnw clean install -DskipTests
   ```

5. **Set Start Command**:
   ```
   java -jar target/store-1.0.0.jar
   ```

6. **Set Active Profile**: `prod`

## Development

### Running tests

```bash
./mvnw test
```

### Database migrations

Flyway migrations are located in `src/main/resources/db/migration/`:
- `V1__initial_migration.sql` - Initial schema
- `V4__create_catrs_table.sql` - Cart tables
- `V5__add_roles_to_users.sql` - User roles
- `V6__create_orders_table.sql` - Order tables

To create a new migration:
1. Create a new SQL file in `src/main/resources/db/migration/`
2. Follow Flyway naming convention: `V{version}__{description}.sql`
3. Run `./mvnw initialize flyway:migrate`

### Code Style

The project uses:
- Lombok for reducing boilerplate code
- MapStruct for entity-DTO mapping
- Spring Validation for input validation

## Security

- JWT tokens for authentication (15-minute access tokens, 7-day refresh tokens)
- BCrypt password encryption
- Role-based access control (USER, ADMIN)
- Stateless session management
- CSRF disabled for API

## License

This project is licensed under the MIT License.