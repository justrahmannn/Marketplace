# Quick Start Guide - Docker Commands

## Build the Image
```bash
cd /home/admin123/Marketplace/Marketplace
docker build -t marketplace-app:latest .
```

## Run the Container

### Option 1: Basic Run (Temporary)
```bash
docker run --rm -p 8080:8080 marketplace-app:latest
```

### Option 2: With Database Persistence
```bash
docker run --rm -p 8080:8080 -v marketplace-data:/app/data marketplace-app:latest
```

### Option 3: Background Mode with Persistence
```bash
docker run -d --name marketplace-container -p 8080:8080 -v marketplace-data:/app/data marketplace-app:latest
```

## Access the Application
Open your browser: **http://localhost:8080**

## Verify Everything Works

### Check if image was created
```bash
docker image ls
```
Look for `marketplace-app` with tag `latest`

### Check if container is running
```bash
docker ps
```

### View container logs
```bash
docker logs marketplace-container
```

### View logs in real-time
```bash
docker logs -f marketplace-container
```

## Stop and Clean Up

### Stop the container
```bash
docker stop marketplace-container
```

### Remove the container
```bash
docker rm marketplace-container
```

### Remove the image
```bash
docker image rm marketplace-app:latest
```

## Common Issues & Solutions

### Issue: Port 8080 already in use
**Solution:** Use a different port on your machine
```bash
docker run --rm -p 9090:8080 marketplace-app:latest
```
Access at: http://localhost:9090

### Issue: Build fails
**Solution:** Clean build without cache
```bash
docker build --no-cache -t marketplace-app:latest .
```

### Issue: Container stops immediately
**Solution:** Check logs for errors
```bash
docker logs marketplace-container
```

## File Checklist
✅ Dockerfile - Contains build instructions  
✅ .dockerignore - Excludes unnecessary files from build  
✅ DOCKER_GUIDE.md - Complete documentation  
✅ QUICK_START.md - This quick reference  

## PW#5 Requirements Fulfilled
✅ Created Dockerfile with clear instructions  
✅ Used appropriate base image (Eclipse Temurin 17)  
✅ Set working directory (WORKDIR /app)  
✅ Copied dependencies and source code (COPY)  
✅ Installed dependencies and built application (RUN)  
✅ Exposed application port (EXPOSE 8080)  
✅ Defined startup command (CMD)  
✅ Documented all steps and commands  

---
**Note:** This is adapted for Java/Spring Boot from the original Python/JavaScript examples in PW#5.
