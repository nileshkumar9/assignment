@echo off
REM Employee Analysis Application Runner for Windows
REM Usage: run.bat <csv-file-path>

if "%1"=="" (
    echo Usage: run.bat ^<csv-file-path^>
    echo Example: run.bat employees.csv
    exit /b 1
)

echo Running Employee Analysis Application...
java -cp "target/classes;target/dependency/*" com.swissre.employee.EmployeeAnalysisApplication "%1"