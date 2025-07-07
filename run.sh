#!/bin/bash
# Employee Analysis Application Runner for Linux/macOS
# Usage: ./run.sh <csv-file-path>

if [ $# -ne 1 ]; then
    echo "Usage: ./run.sh <csv-file-path>"
    echo "Example: ./run.sh employees.csv"
    exit 1
fi

echo "Running Employee Analysis Application..."
java -cp "target/classes:target/dependency/*" com.swissre.employee.EmployeeAnalysisApplication "$1"