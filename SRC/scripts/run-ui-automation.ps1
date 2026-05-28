param(
    [string]$Email = "ui.test@heartbeat.local",
    [string]$Password = "UITest@123456"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

Push-Location $projectRoot
try {
    $gradleArgs = @(
        "connectedDebugAndroidTest",
        "-Pandroid.testInstrumentationRunnerArguments.test_email=$Email",
        "-Pandroid.testInstrumentationRunnerArguments.test_password=$Password"
    )

    & .\gradlew.bat @gradleArgs
}
finally {
    Pop-Location
}
