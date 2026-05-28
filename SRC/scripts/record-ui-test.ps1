param(
    [string]$Email = "ui.test@heartbeat.local",
    [string]$Password = "UITest@123456",
    [string]$Output = "heartbeat-ui-automation.mp4"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$deviceVideo = "/sdcard/heartbeat-ui-automation.mp4"
$localVideo = Join-Path $projectRoot $Output

adb shell rm -f $deviceVideo
$recordProcess = Start-Process -FilePath "adb" -ArgumentList @("shell", "screenrecord", $deviceVideo) -PassThru -WindowStyle Hidden

try {
    & (Join-Path $PSScriptRoot "run-ui-automation.ps1") -Email $Email -Password $Password
}
finally {
    if (!$recordProcess.HasExited) {
        Stop-Process -Id $recordProcess.Id -Force
        Start-Sleep -Seconds 2
    }
}

adb pull $deviceVideo $localVideo
Write-Host "Saved video: $localVideo"
