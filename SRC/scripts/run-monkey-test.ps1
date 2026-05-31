param(
    [int]$Events = 5000,
    [int]$ThrottleMs = 50,
    [string]$PackageName = "com.example.heartbeat",
    [string]$JavaHome = "C:\Program Files\Android\Android Studio\jbr",
    [string]$AdbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools",
    [switch]$SkipInstall
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$reportDir = Join-Path $projectRoot "build\reports\monkey"
$logFile = Join-Path $reportDir "monkey.log"
$gradlew = Join-Path $projectRoot "gradlew.bat"

$env:JAVA_HOME = $JavaHome
$env:Path = "$JavaHome\bin;$AdbPath;$env:Path"

New-Item -ItemType Directory -Force -Path $reportDir | Out-Null
adb devices

if (-not $SkipInstall) {
    if (-not (Test-Path $gradlew)) {
        Write-Error "Gradle wrapper not found at $gradlew"
    }

    & $gradlew ":app:installDebug"
}

$installedPackage = adb shell pm path $PackageName 2>&1
if ($LASTEXITCODE -ne 0 -or ($installedPackage -match "not found|Unknown package|Failure")) {
    Write-Error "Package $PackageName is not installed on the connected device. Check installDebug output above."
}

try {
    $previousErrorActionPreference = $ErrorActionPreference
    $ErrorActionPreference = "Continue"

    $monkeyOutput = adb shell monkey `
        -p $PackageName `
        --throttle $ThrottleMs `
        --pct-syskeys 0 `
        --ignore-crashes `
        --ignore-timeouts `
        --ignore-security-exceptions `
        --monitor-native-crashes `
        -v `
        $Events 2>&1 | ForEach-Object { $_.ToString() }
    $monkeyExitCode = $LASTEXITCODE
}
finally {
    $ErrorActionPreference = $previousErrorActionPreference
}

$monkeyOutput | Tee-Object -FilePath $logFile

$log = Get-Content $logFile -Raw
$targetPackagePattern = [regex]::Escape($PackageName)
$targetCrashPattern = "// CRASH:\s+$targetPackagePattern\b|Process:\s+$targetPackagePattern\b|// NOT RESPONDING:\s+$targetPackagePattern\b"

if ($log -match "No activities found to run") {
    Write-Error "Monkey could not find a launchable activity for $PackageName."
}

if ($log -cmatch $targetCrashPattern -or $log -cmatch "FATAL EXCEPTION") {
    Write-Error "Monkey test found a crash or ANR in $PackageName. See $logFile"
}

if ($monkeyExitCode -ne 0) {
    Write-Error "Monkey exited with code $monkeyExitCode. See $logFile"
}

Write-Host "Monkey test completed without crash markers. Log: $logFile"
