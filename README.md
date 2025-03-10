
# Receipt Processor API

A Spring Boot application designed to process receipts and calculate reward points based on specific business rules. The API allows users to submit receipts and retrieve the calculated points.


## Features

**Submit Receipts**: Accepts receipt details via a REST API.

**Calculate Points**: Computes reward points based on:

- Retailer name.

- Total amount.

- Items purchased.

- Purchase date and time.

**Retrieve Points**: Fetch the calculated points for a processed receipt.



## API Endpoints
POST /receipts/process - Submits a receipt for processing.

GET /receipts/{id}/points - Retrieves the points associated with a processed receipt.

## Getting Started
### Prerequisites
- **Java 17**: Ensure Java 17 is installed.
- **Maven**: For dependency management and project build.
- **Docker**: Used for containerizing the application.
### Installation
1. git fork
2. git clone

3. Create and download a project using Spring Initializr on https://start.spring.io/

4. Create different classes and build the project.
```bash
mvn clean package
```

5. Run the application:
```bash
java -jar target/challenge-0.0.1-SNAPSHOT.jar
```

The application will be available at http://localhost:8080.

### Docker
Create a Dockerfile:
```bash
# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/challenge-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build the Docker image:
```bash
docker build -t receipt-processor .
```

Run the container:
```bash
docker run -p 8080:8080 receipt-processor
```

Access the API at http://localhost:8080.

### API Documentation

1. Submit Receipt

- **Endpoint**: POST /receipts/process
- **Description**: Submits a receipt for processing.
- **Request Body**:
```bash
{
  "retailer": "WalmartStore",
  "purchaseDate": "2025-01-27",
  "purchaseTime": "14:30",
  "total": 4.50,
  "items": [
    {
      "shortDescription": "Milk",
      "price": 2.50
    },
    {
      "shortDescription": "Cookie",
      "price": 4.00
    }
  ]
}
```
**Response**:
```bash
{
  "id": "e9c24685-5f39-4b3f-a70f-8c4031bc0c65"
}
```


2. Retrieve Points

- **Endpoint**: GET /receipts/{id}/points
- **Description**: Retrieves the points for a processed receipt.
- **Path Parameter**:
  - `id`: UUID of the receipt.
- **Response**:
  ```json
  {
    "points": 21
  }
  ```

### Tests

- **Unit Tests**: Tests business logic using JUnit and Mockito.
- **Error Handling Tests**: Ensures proper handling of invalid receipts and missing data.
- **To run all tests**: 
```bash
mvn test
```

### Configuration

Application properties are located in src/main/resources/application.properties:
```bash
server.port=8080
```
### Contributing
- Fork the repository.
- Commit your changes:
```bash
git commit -m "message"
```
- Push to the branch:
```bash
git push origin feature-branch
```
- Submit a pull request.
