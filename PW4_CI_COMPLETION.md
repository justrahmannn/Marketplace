# PW#4: Continuous Integration with GitHub Actions - Completion Guide

## Overview
This document describes the Continuous Integration (CI) setup for the Marketplace Java/Spring Boot project using GitHub Actions.

## ✅ Completed Requirements

### Step 1: Workflow Directory Structure
Created the required `.github/workflows/` directory structure:
```
Marketplace/
├── .github/
│   └── workflows/
│       └── ci.yml
├── JavaProject/
│   ├── pom.xml
│   └── src/
└── ...
```

### Step 2: Workflow File (ci.yml)
Created a comprehensive `ci.yml` workflow file specifically configured for Java/Spring Boot projects using Maven.

### Step 3: Workflow Components Explained

#### Workflow Name
```yaml
name: Java CI with Maven
```
- **Purpose**: Descriptive name that appears in the GitHub Actions tab
- **Visibility**: Shows up in pull requests and commit statuses

#### Trigger Events
```yaml
on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]
```
- **push**: Triggers on every push to the main branch
- **pull_request**: Triggers when pull requests are created or updated targeting main
- **Benefit**: Ensures code is tested before and after merging

#### Job Configuration
```yaml
jobs:
  build-and-test:
    runs-on: ubuntu-latest
```
- **build-and-test**: Job name that describes its purpose
- **runs-on**: Specifies Ubuntu Linux virtual machine (stable and widely used)

#### Workflow Steps

1. **Checkout Code**
   ```yaml
   - name: Checkout code
     uses: actions/checkout@v4
   ```
   - Downloads repository code to the runner
   - Essential first step for all workflows

2. **Set Up JDK 17**
   ```yaml
   - name: Set up JDK 17
     uses: actions/setup-java@v4
     with:
       java-version: '17'
       distribution: 'temurin'
       cache: 'maven'
   ```
   - Installs Java 17 (matches project requirement)
   - Uses Temurin distribution (reliable, open-source)
   - Caches Maven dependencies for faster builds

3. **Install Dependencies**
   ```yaml
   - name: Install dependencies
     run: mvn clean install -DskipTests
     working-directory: JavaProject
   ```
   - Cleans previous builds
   - Downloads and installs all project dependencies
   - Skips tests in this step (tests run separately)

4. **Run Tests**
   ```yaml
   - name: Run tests
     run: mvn test
     working-directory: JavaProject
   ```
   - Executes all unit tests
   - **Critical Step**: Ensures new code doesn't break existing functionality
   - Build fails if any test fails

5. **Build Package**
   ```yaml
   - name: Build package
     run: mvn package -DskipTests
     working-directory: JavaProject
   ```
   - Creates deployable JAR file
   - Skips tests (already run in step 4)

6. **Upload Test Results**
   ```yaml
   - name: Upload test results
     if: always()
     uses: actions/upload-artifact@v4
     with:
       name: test-results
       path: JavaProject/target/surefire-reports/
   ```
   - Uploads test reports as artifacts
   - Runs even if tests fail (`if: always()`)
   - Useful for debugging test failures

7. **Upload Build Artifact**
   ```yaml
   - name: Upload build artifact
     uses: actions/upload-artifact@v4
     with:
       name: marketplace-jar
       path: JavaProject/target/*.jar
   ```
   - Uploads the built JAR file
   - Can be downloaded from GitHub Actions interface

## Step 4: Testing the CI Pipeline

### How to See the Workflow in Action

1. **Commit and Push the Workflow**
   ```bash
   git add .github/workflows/ci.yml
   git commit -m "Add GitHub Actions CI workflow"
   git push origin main
   ```

2. **Create a Feature Branch and Pull Request**
   ```bash
   # Create a new branch
   git checkout -b feature/test-ci-pipeline
   
   # Make a small change (e.g., add a comment to a Java file)
   echo "// Testing CI pipeline" >> JavaProject/src/main/java/com/marketplace/MarketplaceApplication.java
   
   # Commit and push
   git add .
   git commit -m "Test: Add comment to trigger CI"
   git push origin feature/test-ci-pipeline
   ```

3. **Open Pull Request on GitHub**
   - Go to your repository on GitHub
   - Click "Pull requests" tab
   - Click "New pull request"
   - Select `feature/test-ci-pipeline` as the source branch
   - Select `main` as the target branch
   - Click "Create pull request"

4. **View CI Results**
   - GitHub automatically triggers the workflow
   - Go to the "Actions" tab to see real-time progress
   - On the pull request page, you'll see the CI status:
     - ✅ Green checkmark = All tests passed (safe to merge)
     - ❌ Red X = Tests failed (needs fixing before merge)

## Benefits of This CI Setup

### Automated Quality Assurance
- Every code change is automatically tested
- Prevents broken code from being merged into main
- Catches bugs early in the development process

### Fast Feedback Loop
- Developers get immediate feedback on their changes
- No need to manually run tests locally for every change
- Reduces integration issues

### Team Collaboration
- Pull requests show clear pass/fail status
- Reviewers can see test results before approving
- Maintains code quality standards across the team

### Build Artifacts
- JAR files are automatically generated and stored
- Test reports are preserved for analysis
- Easy to download and deploy successful builds

## Technology Stack Specifics

### For Java/Spring Boot Projects
- **Build Tool**: Maven
- **Java Version**: 17
- **Testing Framework**: JUnit (included in Spring Boot Starter Test)
- **Key Commands**:
  - `mvn clean install`: Build and install dependencies
  - `mvn test`: Run unit tests
  - `mvn package`: Create JAR file

### Working Directory
All Maven commands specify `working-directory: JavaProject` because the project structure has:
```
Marketplace/
└── JavaProject/  ← Maven project root (contains pom.xml)
```

## Troubleshooting

### If the Workflow Fails

1. **Check the Actions Tab**
   - Click on the failed workflow run
   - Expand each step to see error messages
   - Look for test failures or compilation errors

2. **Common Issues**
   - **Compilation errors**: Fix syntax errors in Java code
   - **Test failures**: Fix failing tests or update test expectations
   - **Dependency issues**: Ensure all dependencies are in pom.xml

3. **Run Locally First**
   ```bash
   cd JavaProject
   mvn clean test
   ```
   - Fix issues locally before pushing

## Next Steps

### Enhancement Opportunities
1. **Add Code Coverage Reports**
   - Integrate JaCoCo for coverage metrics
   - Show coverage percentage in pull requests

2. **Add Code Quality Checks**
   - Integrate SonarQube or Checkstyle
   - Enforce coding standards automatically

3. **Add Continuous Deployment**
   - Deploy to staging environment on successful builds
   - Automate releases to production

4. **Add Security Scanning**
   - Scan dependencies for vulnerabilities
   - Check for security issues in code

## Learning Outcomes Achieved

✅ **Understanding CI Purpose**: Automated testing prevents integration issues  
✅ **GitHub Actions Setup**: Created proper `.github/workflows/` structure  
✅ **Workflow Definition**: Configured triggers for push and pull_request events  
✅ **Job Configuration**: Set up Java 17 environment, installed dependencies, ran tests  
✅ **Verification**: Ready to verify automatic workflow execution on pull requests  

## References
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)

---
**Last Updated**: December 10, 2025  
**Project**: Marketplace (Java/Spring Boot)  
**CI Tool**: GitHub Actions  
