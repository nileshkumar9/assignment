# Setup and Installation Guide

## Prerequisites

### 1. Install Java Development Kit (JDK) 11 or higher

#### Windows:
1. Download JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Install and set JAVA_HOME environment variable
3. Verify installation: `java -version`

#### macOS:
```bash
# Using Homebrew
brew install openjdk@11

# Or download from adoptium.net
```

#### Linux:
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk

# CentOS/RHEL
sudo yum install java-11-openjdk-devel
```

### 2. Install Apache Maven

#### Windows:
1. Download Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Extract to a directory (e.g., `C:\apache-maven-3.9.4`)
3. Add Maven bin directory to PATH environment variable
4. Set M2_HOME environment variable to Maven installation directory
5. Verify installation: `mvn -version`

#### macOS:
```bash
# Using Homebrew
brew install maven

# Verify installation
mvn -version
```

#### Linux:
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install maven

# CentOS/RHEL
sudo yum install maven

# Verify installation
mvn -version
```

## Quick Start

### 1. Clone or Download the Project
```bash
# If using git
git clone <repository-url>
cd assignment

# Or download and extract the ZIP file
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Run Tests
```bash
mvn test
```

### 4. Package the Application
```bash
mvn clean package
```

### 5. Run the Application
```bash
# Using the convenience scripts
./run.sh path/to/your/employees.csv    # Linux/macOS
run.bat path/to/your/employees.csv     # Windows

# Or directly with Java
java -cp target/employee-analysis-1.0.0.jar com.swissre.employee.EmployeeAnalysisApplication path/to/your/employees.csv
```

## IDE Setup

### IntelliJ IDEA
1. Open IntelliJ IDEA
2. Click "Open" and select the project folder
3. IntelliJ will automatically detect Maven and import the project
4. Wait for indexing to complete
5. Right-click on `EmployeeAnalysisApplication.java` and select "Run"

### Eclipse
1. Open Eclipse
2. File → Import → Existing Maven Projects
3. Browse to the project folder and click "Finish"
4. Wait for Maven to download dependencies
5. Right-click on `EmployeeAnalysisApplication.java` → Run As → Java Application

### VS Code
1. Open VS Code
2. Install "Extension Pack for Java" if not already installed
3. Open the project folder
4. VS Code will automatically detect Maven and set up the project
5. Use Ctrl+F5 to run the main class

## Troubleshooting

### Maven Not Found
- Ensure Maven is installed and added to PATH
- Check `mvn -version` works in terminal/command prompt
- Restart your IDE after installing Maven

### Java Version Issues
- Ensure Java 11 or higher is installed
- Check `java -version` and `javac -version`
- Set JAVA_HOME environment variable correctly

### Compilation Errors
- Run `mvn clean` to clean previous builds
- Check that all dependencies are downloaded: `mvn dependency:resolve`
- Verify internet connection for Maven to download dependencies

### File Path Issues
- Use absolute paths for CSV files
- Ensure CSV file exists and is readable
- Check file permissions

## Example CSV Files

### Basic Example (employees.csv)
```csv
id,firstName,lastName,salary,managerId
1,John,CEO,100000,
2,Jane,Manager,75000,1
3,Bob,Employee,50000,2
4,Alice,Employee,45000,2
```

### Complex Example (complex-employees.csv)
```csv
id,firstName,lastName,salary,managerId
100,CEO,BigBoss,150000,
200,VP,Sales,120000,100
300,VP,Engineering,125000,100
400,Director,SalesEast,90000,200
500,Director,SalesWest,85000,200
600,Director,Backend,95000,300
700,Director,Frontend,90000,300
800,Manager,Team1,70000,600
900,Manager,Team2,75000,600
1000,Senior,Dev1,60000,800
1100,Junior,Dev2,45000,800
1200,Intern,Dev3,25000,1100
```

## Running Examples

```bash
# Test with the provided sample data
java -cp target/employee-analysis-1.0.0.jar com.swissre.employee.EmployeeAnalysisApplication src/test/resources/sample-employees.csv

# Expected output will show salary and reporting line analysis
```

## Performance Considerations

- The application is optimized for files up to 10,000 employees
- For larger datasets, consider increasing JVM heap size:
  ```bash
  java -Xmx2g -cp target/employee-analysis-1.0.0.jar com.swissre.employee.EmployeeAnalysisApplication large-file.csv
  ```

## Configuration

Modify analysis parameters in `src/main/java/com/swissre/employee/config/AnalysisConfig.java`:

```java
public static final BigDecimal MINIMUM_SALARY_INCREASE_PERCENTAGE = new BigDecimal("0.20"); // 20%
public static final BigDecimal MAXIMUM_SALARY_INCREASE_PERCENTAGE = new BigDecimal("0.50"); // 50%
public static final int MAXIMUM_REPORTING_LINE_LENGTH = 4;
```

Rebuild after changes:
```bash
mvn clean compile package
```
