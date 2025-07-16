# CarPlay List Reminder Backend

A Spring Boot backend application for managing family shopping lists with CarPlay integration. This application allows families to create shared shopping lists and receive notifications when connecting to CarPlay.

## Features

### Core Functionality
- **User Management**: Create and manage user accounts
- **Family Management**: Create families and invite members
- **Shopping Lists**: Create, update, and manage shopping lists
- **Shopping List Items**: Add items with quantities and completion status
- **CarPlay Integration**: Get notifications when connecting to CarPlay

### Family Sharing
- Create families with unique family codes
- Invite family members using family codes
- Share shopping lists among family members
- Role-based access (Admin/Member)

## API Endpoints

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `POST /api/users/register` - Register new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Family Management
- `GET /api/families` - Get all families
- `GET /api/families/user/{userId}` - Get families for a user
- `POST /api/families` - Create new family
- `POST /api/families/{familyCode}/join` - Join family by code
- `GET /api/families/{familyId}/members` - Get family members
- `DELETE /api/families/{familyId}/members/{userId}` - Remove member

### Shopping Lists
- `GET /api/shopping-lists` - Get all shopping lists
- `GET /api/shopping-lists/{id}` - Get specific list
- `GET /api/shopping-lists/incomplete` - Get incomplete lists
- `POST /api/shopping-lists` - Create new list
- `PUT /api/shopping-lists/{id}` - Update list
- `DELETE /api/shopping-lists/{id}` - Delete list

### Shopping List Items
- `GET /api/shopping-list-items/list/{listId}` - Get items for a list
- `POST /api/shopping-list-items/list/{listId}` - Add item to list
- `PUT /api/shopping-list-items/{id}/toggle` - Toggle item completion
- `DELETE /api/shopping-list-items/{id}` - Delete item

### CarPlay Integration
- `GET /api/carplay/notification` - Get notification for CarPlay
- `POST /api/carplay/connect` - Trigger when CarPlay connects
- `GET /api/carplay/list/{listId}/summary` - Get list summary

## Database Schema

The application uses PostgreSQL with the following main tables:
- `users` - User account information
- `families` - Family groups
- `family_members` - Family membership relationships
- `shopping_lists` - Shopping lists belonging to families
- `shopping_list_items` - Individual items in shopping lists

## Setup and Installation

### Environment Variables

Create a `.env` file in the root directory with the following content:

```
DB_URL=jdbc:postgresql://db:5432/postgres
DB_USER=your_db_user
DB_PASSWORD=your_db_password
```

### Docker Compose

The application is containerized using Docker. The database schema is automatically created on startup.

```sh
docker-compose up --build
```

This will start:
- PostgreSQL database on port 5432
- Spring Boot application on port 8080

### Manual Setup

If you prefer to run without Docker:

1. Set up PostgreSQL database
2. Update `application.properties` with your database credentials
3. Run the application:
   ```sh
   ./mvnw spring-boot:run
   ```

## Sample Data

The application includes sample data:
- Two users: john_doe and jane_smith
- One family: Smith Family
- Two shopping lists: Grocery Shopping and Hardware Store
- Sample items in each list

## Testing

### Example Usage Flow

1. **Create a user**:
   ```bash
   curl -X POST http://localhost:8080/api/users/register \
     -d "username=testuser&email=test@example.com&firstName=Test&lastName=User"
   ```

2. **Create a family**:
   ```bash
   curl -X POST http://localhost:8080/api/families \
     -d "name=My Family&creatorId=1"
   ```

3. **Join family using code**:
   ```bash
   curl -X POST http://localhost:8080/api/families/FAM001/join \
     -d "userId=2"
   ```

4. **Create shopping list**:
   ```bash
   curl -X POST http://localhost:8080/api/shopping-lists \
     -H "Content-Type: application/json" \
     -d '{"name":"Weekly Groceries","family":{"id":1},"createdBy":{"id":1}}'
   ```

5. **Get CarPlay notification**:
   ```bash
   curl http://localhost:8080/api/carplay/notification
   ```
