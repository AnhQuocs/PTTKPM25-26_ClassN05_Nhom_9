param(
    [string]$Email = "anhquocs@gmail.com",
    [string]$Password = "12345678",
    [string]$JavaHome = "C:\Program Files\Android\Android Studio\jbr",
    [string]$AdbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools"
)

# User test account:
#   Email: anhquocs@gmail.com
#   Password: 12345678
#
# Admin/staff test account, for future admin/staff flow tests:
#   Email: anhquocadmin@gmail.com
#   Password: 12345678
#   Code: HBST001

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

$env:JAVA_HOME = $JavaHome
$env:Path = "$JavaHome\bin;$AdbPath;$env:Path"

Push-Location $projectRoot
try {
    adb devices

    $gradleArgs = @(
        "connectedDebugAndroidTest",
        "-Pandroid.testInstrumentationRunnerArguments.class=com.example.heartbeat.UserFlowAutomationTest",
        "-Pandroid.testInstrumentationRunnerArguments.test_email=$Email",
        "-Pandroid.testInstrumentationRunnerArguments.test_password=$Password"
    )

    & .\gradlew.bat @gradleArgs
}
finally {
    Pop-Location
}
