param(
    [string]$Email = "anhquocs@gmail.com",
    [string]$Password = "12345678",
    [string]$Code = "",
    [switch]$LoginAsEmployee,
    [switch]$User,
    [switch]$Admin,
    [switch]$AllAccounts,
    [switch]$UserFeatures,
    [switch]$AdminFeatures,
    [switch]$AllFeatures,
    [string]$PackageName = "com.example.heartbeat",
    [string]$JavaHome = "C:\Program Files\Android\Android Studio\jbr",
    [string]$AdbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools"
)

# User test account:
#   Email: anhquocs@gmail.com
#   Password: 12345678
#
# Admin test account. This account must use employee login:
#   Email: staff@gmail.com
#   Password: 12345678
#   Code: HBST001
#
# Example:
#   .\scripts\run-ui-automation.ps1 -User
#   .\scripts\run-ui-automation.ps1 -Admin
#   .\scripts\run-ui-automation.ps1 -AllAccounts
#   .\scripts\run-ui-automation.ps1 -UserFeatures
#   .\scripts\run-ui-automation.ps1 -AdminFeatures
#   .\scripts\run-ui-automation.ps1 -AllFeatures

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

$env:JAVA_HOME = $JavaHome
$env:Path = "$JavaHome\bin;$AdbPath;$env:Path"

function Invoke-UiAutomation {
    param(
        [string]$AccountName,
        [string]$TestEmail,
        [string]$TestPassword,
        [string]$TestCode,
        [bool]$TestLoginAsEmployee,
        [string]$TestClass = "com.example.heartbeat.UserFlowAutomationTest"
    )

    Write-Host "Running UI automation for $AccountName ($TestEmail)..."
    if ($TestLoginAsEmployee) {
        Write-Host "Login path: click LOGIN AS AN EMPLOYEE below the user Login button, then enter email/password/code."
    }
    else {
        Write-Host "Login path: normal user login."
    }

    $previousErrorActionPreference = $ErrorActionPreference
    $ErrorActionPreference = "Continue"
    $clearOutput = adb shell pm clear $PackageName 2>&1
    $ErrorActionPreference = $previousErrorActionPreference
    if (($clearOutput -join "`n") -match "Success") {
        $clearOutput | Out-Host
    }
    else {
        Write-Host "App data clear skipped: $($clearOutput -join ' ')"
    }

    $loginAsEmployeeArg = $TestLoginAsEmployee.ToString().ToLowerInvariant()

    $gradleArgs = @(
        "connectedDebugAndroidTest",
        "-Pandroid.testInstrumentationRunnerArguments.class=$TestClass",
        "-Pandroid.testInstrumentationRunnerArguments.test_email=$TestEmail",
        "-Pandroid.testInstrumentationRunnerArguments.test_password=$TestPassword",
        "-Pandroid.testInstrumentationRunnerArguments.test_code=$TestCode",
        "-Pandroid.testInstrumentationRunnerArguments.test_login_as_employee=$loginAsEmployeeArg"
    )

    & .\gradlew.bat @gradleArgs
}

Push-Location $projectRoot
try {
    adb devices

    if ($AllAccounts.IsPresent) {
        Invoke-UiAutomation -AccountName "user" -TestEmail "anhquocs@gmail.com" -TestPassword "12345678" -TestCode "" -TestLoginAsEmployee $false
        Invoke-UiAutomation -AccountName "admin" -TestEmail "staff@gmail.com" -TestPassword "12345678" -TestCode "HBST001" -TestLoginAsEmployee $true -TestClass "com.example.heartbeat.UserFlowAutomationTest#loginFlow_entersCredentialsAndOpensHome"
    }
    elseif ($AllFeatures.IsPresent) {
        Invoke-UiAutomation -AccountName "user feature flows" -TestEmail "anhquocs@gmail.com" -TestPassword "12345678" -TestCode "" -TestLoginAsEmployee $false -TestClass "com.example.heartbeat.FeatureFlowAutomationTest#userSearchFlow_entersQueryOnSearchScreen,com.example.heartbeat.FeatureFlowAutomationTest#userDonationFlow_opensUpcomingEventsForRegistration"
        Invoke-UiAutomation -AccountName "admin feature flows" -TestEmail "staff@gmail.com" -TestPassword "12345678" -TestCode "HBST001" -TestLoginAsEmployee $true -TestClass "com.example.heartbeat.FeatureFlowAutomationTest#adminCreateEventFlow_opensFormAndEntersCoreFields,com.example.heartbeat.FeatureFlowAutomationTest#adminApproveMemberFlow_opensPendingRequestsAndApprovesWhenAvailable"
    }
    elseif ($Admin.IsPresent) {
        Invoke-UiAutomation -AccountName "admin" -TestEmail "staff@gmail.com" -TestPassword "12345678" -TestCode "HBST001" -TestLoginAsEmployee $true -TestClass "com.example.heartbeat.UserFlowAutomationTest#loginFlow_entersCredentialsAndOpensHome"
    }
    elseif ($User.IsPresent) {
        Invoke-UiAutomation -AccountName "user" -TestEmail "anhquocs@gmail.com" -TestPassword "12345678" -TestCode "" -TestLoginAsEmployee $false
    }
    elseif ($AdminFeatures.IsPresent) {
        Invoke-UiAutomation -AccountName "admin feature flows" -TestEmail "staff@gmail.com" -TestPassword "12345678" -TestCode "HBST001" -TestLoginAsEmployee $true -TestClass "com.example.heartbeat.FeatureFlowAutomationTest#adminCreateEventFlow_opensFormAndEntersCoreFields,com.example.heartbeat.FeatureFlowAutomationTest#adminApproveMemberFlow_opensPendingRequestsAndApprovesWhenAvailable"
    }
    elseif ($UserFeatures.IsPresent) {
        Invoke-UiAutomation -AccountName "user feature flows" -TestEmail "anhquocs@gmail.com" -TestPassword "12345678" -TestCode "" -TestLoginAsEmployee $false -TestClass "com.example.heartbeat.FeatureFlowAutomationTest#userSearchFlow_entersQueryOnSearchScreen,com.example.heartbeat.FeatureFlowAutomationTest#userDonationFlow_opensUpcomingEventsForRegistration"
    }
    else {
        $loginAsEmployeeValue = $LoginAsEmployee.IsPresent -or ![string]::IsNullOrWhiteSpace($Code)
        $accountName = if ($loginAsEmployeeValue) { "custom employee/admin" } else { "custom user" }
        $testClass = if ($loginAsEmployeeValue) { "com.example.heartbeat.UserFlowAutomationTest#loginFlow_entersCredentialsAndOpensHome" } else { "com.example.heartbeat.UserFlowAutomationTest" }
        Invoke-UiAutomation -AccountName $accountName -TestEmail $Email -TestPassword $Password -TestCode $Code -TestLoginAsEmployee $loginAsEmployeeValue -TestClass $testClass
    }
}
finally {
    Pop-Location
}
