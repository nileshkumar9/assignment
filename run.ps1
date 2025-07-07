# Employee Analysis Application Runner for PowerShell
# Usage: .\run.ps1 <csv-file-path>

param(
    [Parameter(Mandatory=$true)]
    [string]$CsvFilePath
)

if (-not (Test-Path $CsvFilePath)) {
    Write-Host "Error: File '$CsvFilePath' not found!" -ForegroundColor Red
    exit 1
}

Write-Host "Running Employee Analysis Application..." -ForegroundColor Green
java -cp "target/classes;target/dependency/*" com.swissre.employee.EmployeeAnalysisApplication "$CsvFilePath"
