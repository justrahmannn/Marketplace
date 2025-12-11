# PW#5: Containerization with Docker - Spring Boot Marketplace Application

## Overview
This document provides complete instructions for containerizing the Spring Boot Marketplace application using Docker. You will build a custom Docker image and run your application in an isolated container.

## What is Docker?
Docker is an open-source platform that automates the deployment of applications inside lightweight, portable containers. A container packages everything an application needs to run: source code, libraries, dependencies, and configuration files.

### Understanding Images vs Containers
- **Image**: A read-only template or blueprint with instructions for creating a container
- **Container**: A runnable, live instance of an image that actually runs your application

## Prerequisites
Before starting, ensure you have:
- Docker installed on your system ([Download Docker](https://www.docker.com/products/docker-desktop))
- Your Spring Boot Marketplace application (from previous practical works)
- The following project files:
  - `JavaProject/pom.xml` - Maven build configuration
  - `JavaProject/src/` - Application source code
  - `JavaProject/src/main/resources/application.properties` - Application configuration

## Project Structure
```
Marketplace/
├── Dockerfile              # Instructions for building the Docker image
├── .dockerignore          # Files to exclude from Docker context
├── JavaProject/
│   ├── pom.xml           # Maven dependencies and build configuration
│   └── src/
│       └── main/
│           ├── java/     # Java source code
│           └── resources/ # Configuration and templates
```

## Understanding the Dockerfile

Our Dockerfile uses a **multi-stage build** approach for efficiency:

### Stage 1: Build Stage
```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
```
- **FROM**: Specifies the base image (Maven 3.9 with OpenJDK 17 on Alpine Linux)
- This stage compiles and packages the application

```dockerfile
WORKDIR /app
```
- **WORKDIR**: Sets the default directory for all subsequent commands

```dockerfile
COPY JavaProject/pom.xml .
RUN mvn dependency:go-offline -B
```
- **COPY**: Copies the Maven configuration file into the image
- **RUN**: Executes the command to download dependencies
- This layer is cached, so dependencies won't be re-downloaded if `pom.xml` hasn't changed

```dockerfile
COPY JavaProject/src ./src
RUN mvn clean package -DskipTests
```
- Copies the source code and builds the application
- Creates a JAR file in the `target/` directory

### Stage 2: Runtime Stage
```dockerfile
FROM eclipse-temurin:17-jre-alpine
```
- Uses a lightweight Java Runtime Environment (JRE) image
- Much smaller than the full JDK, reducing final image size

```dockerfile
COPY --from=build /app/target/*.jar app.jar
```
- Copies only the compiled JAR file from the build stage
- Discards build tools and source code, keeping the final image small

```dockerfile
EXPOSE 8080
```
- **EXPOSE**: Documents that the container listens on port 8080
- This is informational; it doesn't actually publish the port

```dockerfile
CMD ["java", "-jar", "app.jar"]
```
- **CMD**: Defines the default command to run when the container starts
- Launches your Spring Boot application

## Step-by-Step Instructions

### Step 1: Verify Prerequisites
Ensure Docker is installed and running:
```bash
docker --version
```

You should see output like: `Docker version 24.x.x, build xxxxx`

### Step 2: Navigate to Project Directory
```bash
cd /home/admin123/Marketplace/Marketplace
```

### Step 3: Build the Docker Image
Build your custom image with a descriptive name and tag:

```bash
docker build -t marketplace-app:latest .
```

**Explanation:**
- `docker build`: Command to build an image
- `-t marketplace-app:latest`: Tags the image with name `marketplace-app` and version `latest`
- `.`: Uses the current directory as the build context

**What happens during the build:**
1. Docker reads the Dockerfile
2. Downloads the base images (Maven and JRE)
3. Copies your project files
4. Downloads Maven dependencies
5. Compiles and packages your Spring Boot application
6. Creates a final, optimized image

**Expected output:** You'll see each step executing and layers being created.

### Step 4: Verify the Image
List all Docker images to confirm your image was created:

```bash
docker image ls
```

You should see `marketplace-app` with the `latest` tag.

### Step 5: Run the Docker Container

#### Basic Run (Without Database Persistence)
```bash
docker run --rm -p 8080:8080 marketplace-app:latest
```

**Explanation:**
- `docker run`: Creates and starts a container
- `--rm`: Automatically removes the container when it stops
- `-p 8080:8080`: Maps port 8080 on your machine to port 8080 in the container
  - Format: `-p HOST_PORT:CONTAINER_PORT`
- `marketplace-app:latest`: The image to use

#### Run with Database Persistence (Recommended)
For production use, mount a volume to persist H2 database data:

```bash
docker run --rm -p 8080:8080 -v marketplace-data:/app/data marketplace-app:latest
```

**Additional flags:**
- `-v marketplace-data:/app/data`: Creates a named volume for database persistence

#### Run in Detached Mode (Background)
To run the container in the background:

```bash
docker run -d --name marketplace-container -p 8080:8080 -v marketplace-data:/app/data marketplace-app:latest
```

**Additional flags:**
- `-d`: Detached mode (runs in background)
- `--name marketplace-container`: Assigns a custom name to the container

### Step 6: Access Your Application
Once the container is running, open your web browser and navigate to:

```
http://localhost:8080
```

You should see your Marketplace application's home page!

### Step 7: View Running Containers
Check all active containers:

```bash
docker ps
```

To see all containers (including stopped ones):

```bash
docker ps -a
```

## Useful Docker Commands

### Container Management
```bash
# View container logs
docker logs marketplace-container

# Follow logs in real-time
docker logs -f marketplace-container

# Stop a running container
docker stop marketplace-container

# Start a stopped container
docker start marketplace-container

# Remove a container
docker rm marketplace-container

# Remove a running container (force)
docker rm -f marketplace-container
```

### Image Management
```bash
# List all images
docker image ls

# Remove an image
docker image rm marketplace-app:latest

# View image build history
docker history marketplace-app:latest

# Inspect image details
docker inspect marketplace-app:latest
```

### System Cleanup
```bash
# Remove all stopped containers
docker container prune

# Remove unused images
docker image prune

# Remove all unused resources (containers, images, networks, volumes)
docker system prune -a
```

## Advanced Usage

### Running with Environment Variables
Override application properties using environment variables:

```bash
docker run --rm -p 8080:8080 \
  -e SERVER_PORT=9000 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/customdb \
  marketplace-app:latest
```

### Running with Custom Network
Create a custom network for container communication:

```bash
# Create network
docker network create marketplace-network

# Run container in network
docker run -d --name marketplace-container \
  --network marketplace-network \
  -p 8080:8080 \
  marketplace-app:latest
```

### Building with Different Tags
Tag images for versioning:

```bash
# Build with version tag
docker build -t marketplace-app:v1.0.0 .

# Build with multiple tags
docker build -t marketplace-app:latest -t marketplace-app:v1.0.0 .
```

## Troubleshooting

### Container Exits Immediately
View logs to see what went wrong:
```bash
docker logs marketplace-container
```

### Port Already in Use
If port 8080 is already in use, map to a different port:
```bash
docker run --rm -p 9090:8080 marketplace-app:latest
```
Then access at `http://localhost:9090`

### Build Fails Due to Dependencies
Clean Maven cache and rebuild:
```bash
docker build --no-cache -t marketplace-app:latest .
```

### Cannot Connect to Database
Ensure the volume is properly mounted:
```bash
docker run --rm -p 8080:8080 -v $(pwd)/data:/app/data marketplace-app:latest
```

## Understanding Multi-Stage Builds

Our Dockerfile uses multi-stage builds for several advantages:

1. **Smaller Final Image**: The runtime image only contains the JRE and JAR file, not build tools
2. **Faster Builds**: Docker caches layers, so unchanged dependencies aren't re-downloaded
3. **Security**: Fewer components mean a smaller attack surface
4. **Efficiency**: Build artifacts don't bloat the final image

**Size Comparison:**
- With multi-stage build: ~250-300 MB
- Without multi-stage (including Maven): ~800-900 MB

## Docker Best Practices Applied

1. **Layer Caching**: Dependencies are downloaded before copying source code
2. **Multi-Stage Builds**: Separate build and runtime environments
3. **Lightweight Base Images**: Using Alpine Linux reduces image size
4. **Security**: Running as non-root user (spring:spring)
5. **.dockerignore**: Excludes unnecessary files from build context
6. **Specific Tags**: Using specific version tags instead of `latest` for base images

## Learning Outcomes

By completing this practical work, you have learned to:

✅ Understand the difference between Docker images and containers  
✅ Write a multi-stage Dockerfile for a Java/Spring Boot application  
✅ Use Docker instructions: FROM, WORKDIR, COPY, RUN, EXPOSE, CMD  
✅ Build a Docker image from a Dockerfile  
✅ Run an application inside a Docker container  
✅ Map container ports to host machine ports  
✅ Persist data using Docker volumes  
✅ Manage containers and images using Docker CLI commands  

## Next Steps

- Explore **Docker Compose** for multi-container applications
- Learn about **Docker Hub** for sharing images
- Investigate **Kubernetes** for container orchestration
- Set up **CI/CD pipelines** with Docker

## Resources

- [Docker Official Documentation](https://docs.docker.com/)
- [Spring Boot with Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Dockerfile Best Practices](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)

---

**Congratulations!** Your Spring Boot Marketplace application is now containerized and portable across any environment that supports Docker! 🐳
