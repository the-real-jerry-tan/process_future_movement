
# Process Future Movement Application

## Overview

The Process Future Movement Application is a Spring Batch-based solution designed to process financial transaction data efficiently. The application reads data from an input file, processes it using configurable processing strategies for optimal performance, and writes the output to a file named `Output.txt`. Additionally, the application exposes RESTful endpoints that can return data in both JSON and CSV formats, depending on the client-specified HTTP headers. This application also includes Angular and React frontends for interacting with the data.

## Features

- **Spring Batch Processing**: The core of the application leverages Spring Batch to handle large-scale data processing tasks. The input data is read from a fixed-width text file, processed, and then written to an output file.
- **Configurable Processors**: The application uses different processing strategies based on performance considerations. The available processors include:
  - ForkJoin Tasklet Processor
  - MapReduce Tasklet Processor
  - Parallel Streams Tasklet Processor
- **REST API**: Exposes endpoints for accessing processed data. Supports both JSON and CSV outputs based on the `Accept` HTTP header.
- **Frontend Applications**: The application includes both Angular and React frontends to interact with the processed data.
- **Observability**: The application includes observability metrics exposed via Prometheus, utilizing Micrometer registry and Spring Actuator.
- **Containerization and Deployment**: The application is containerized using Docker and can be deployed to a Kubernetes cluster. The deployment configuration is provided in the `k8s` directory.

## Application Architecture

### Spring Batch

The application processes input data in three main steps:
1. **Reading**: Data is read from an input file using custom ItemReader implementations.
2. **Processing**: Based on configuration, one of the following processors is selected for optimal performance:
   - **ForkJoin Tasklet Processor**: Utilizes Java's Fork/Join framework to process data in parallel.
   - **MapReduce Tasklet Processor**: Applies a MapReduce paradigm for distributed data processing.
   - **Parallel Streams Tasklet Processor**: Uses Java 8's parallel streams for processing.

3. **Writing**: Processed data is written to `Output.txt`.

### REST API

The REST API exposes endpoints that support both JSON and CSV formats. The response format is determined by the `Accept` HTTP header:
- `Accept: application/json` for JSON output.
- `Accept: text/csv` for CSV output.

The API integrates seamlessly with the Angular and React frontends, allowing for dynamic data display and interaction.

### Observability

Observability is integrated into the application using Micrometer and Spring Actuator. Key metrics are collected and exposed to Prometheus, providing insights into application performance and health. Metrics include:
- Processing time
- Throughput
- Error rates

### Containerization and Deployment

The application is containerized using Docker, ensuring consistent environments across development, testing, and production. The Dockerfile is included in the project, enabling easy creation of Docker images.

Deployment is managed using Kubernetes, with configuration files located in the `k8s` directory. This setup facilitates scalable deployment, orchestration, and management of the application in a cloud-native environment.

## Testing

The application includes comprehensive test coverage for all major components:
- **ProcessFutureMovementForkJoinTaskletProcessorTest**: Tests for the ForkJoin Tasklet Processor.
- **ProcessFutureMovementMapReduceTaskletProcessorTest**: Tests for the MapReduce Tasklet Processor.
- **ProcessFutureMovementParallelStreamTaskletProcessorTest**: Tests for the Parallel Streams Tasklet Processor.
- **ProcessFutureMovementProcessorTest**: General processor tests to ensure data integrity and processing accuracy.

These tests ensure the reliability and correctness of the application, covering edge cases and typical usage scenarios.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven
- Docker (for containerization)
- Kubernetes (for deployment)
- Prometheus (for observability)

### Build and Run

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```

2. **Build the application**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   java -jar target/process-future-movement-0.0.1-SNAPSHOT.jar
   ```

4. **Access the REST API**:
   - JSON format: `http://localhost:8080/api/data`
   - CSV format: `http://localhost:8080/api/data` (with `Accept: text/csv` header)

5. **Run the Docker container**:
   ```bash
   docker build -t process-future-movement .
   docker run -p 8080:8080 process-future-movement
   ```

6. **Deploy to Kubernetes**:
   ```bash
   kubectl apply -f k8s/
   ```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
