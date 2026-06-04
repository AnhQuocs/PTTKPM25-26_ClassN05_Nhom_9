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
    [string]$Output = "heartbeat-ui-automation.mp4",
    [string]$JavaHome = "C:\Program Files\Android\Android Studio\jbr",
    [string]$AdbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$deviceVideo = "/sdcard/heartbeat-ui-automation.mp4"
$localVideo = Join-Path $projectRoot $Output

$env:JAVA_HOME = $JavaHome
$env:Path = "$JavaHome\bin;$AdbPath;$env:Path"

adb devices
adb shell rm -f $deviceVideo
$recordProcess = Start-Process -FilePath "adb" -ArgumentList @("shell", "screenrecord", $deviceVideo) -PassThru -WindowStyle Hidden

try {
    & (Join-Path $PSScriptRoot "run-ui-automation.ps1") -Email $Email -Password $Password -Code $Code -LoginAsEmployee:$LoginAsEmployee.IsPresent -User:$User.IsPresent -Admin:$Admin.IsPresent -AllAccounts:$AllAccounts.IsPresent -UserFeatures:$UserFeatures.IsPresent -AdminFeatures:$AdminFeatures.IsPresent -AllFeatures:$AllFeatures.IsPresent -JavaHome $JavaHome -AdbPath $AdbPath
}
finally {
    if (!$recordProcess.HasExited) {
        Stop-Process -Id $recordProcess.Id -Force
        Start-Sleep -Seconds 2
    }
}

adb pull $deviceVideo $localVideo
Write-Host "Saved video: $localVideo"
