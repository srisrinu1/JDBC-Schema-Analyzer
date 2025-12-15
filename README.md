# JDBC-Schema-Analyzer
A JDBC-powered tool to analyze database schemas, generate diffs, and support safe data migration

## Story 1: Database Connection Testing

This tool validates database connectivity before performing schema operations.

### Prerequisites
- Java 17 or higher
- Maven 3.9 or higher
- Access to source and target databases (MySQL, PostgreSQL, etc.)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/srisrinu1/JDBC-Schema-Analyzer.git
   cd JDBC-Schema-Analyzer
   ```

2. **Configure database connections**
   
   Copy the example configuration files:
   ```bash
   cp src/main/resources/db/source-db.properties.example src/main/resources/db/source-db.properties
   cp src/main/resources/db/target-db.properties.example src/main/resources/db/target-db.properties
   ```

3. **Edit configuration files** with your database credentials:
   
   `src/main/resources/db/source-db.properties`:
   ```properties
   jdbc.url=jdbc:mysql://localhost:3306/source_db
   jdbc.username=your_username
   jdbc.password=your_password
   jdbc.driverClassName=com.mysql.cj.jdbc.Driver
   jdbc.dialect=mysql
   ```

   `src/main/resources/db/target-db.properties`:
   ```properties
   jdbc.url=jdbc:postgresql://localhost:5432/target_db
   jdbc.username=your_username
   jdbc.password=your_password
   jdbc.driverClassName=org.postgresql.Driver
   jdbc.dialect=postgres
   ```

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/jdbc-inspector-0.0.1-SNAPSHOT.jar
```

### Expected Output

**Success scenario:**
```
Loading database configurations...
Configurations loaded successfully.

Testing SOURCE database connection...
✓ SUCCESS: Connected to SOURCE database
  URL: jdbc:mysql://localhost:3306/source_db
  User: your_username

Testing TARGET database connection...
✓ SUCCESS: Connected to TARGET database
  URL: jdbc:postgresql://localhost:5432/target_db
  User: your_username

=====================================
✓ All database connections successful
=====================================
```

**Failure scenario:**
The application exits with status code 1 and prints clear error messages indicating which step failed.

### Supported Databases
- MySQL (via `com.mysql.cj.jdbc.Driver`)
- PostgreSQL (via `org.postgresql.Driver`)

### Architecture

The project follows enterprise-grade design patterns:

- **Separation of Concerns**: Configuration loading, connection management, and application logic are separated
- **Resource Management**: Proper use of try-with-resources for connection handling
- **Error Handling**: Clear error messages with proper exception propagation
- **Security**: Password sanitization in logs and error messages
- **Validation**: Connection validation with configurable timeouts
