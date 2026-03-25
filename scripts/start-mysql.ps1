$binDir = "C:\Program Files\MySQL\MySQL Server 8.4\bin"
$config = "C:\Users\Sai Kiran\Desktop\nt\mysql\my.ini"

if (Get-Process mysqld -ErrorAction SilentlyContinue) {
    Write-Host "MySQL is already running."
    exit 0
}

Start-Process -FilePath "$binDir\mysqld.exe" -ArgumentList "--defaults-file=$config","--console" -WindowStyle Hidden
Start-Sleep -Seconds 8
& "$binDir\mysql.exe" -u root -proot -e "SELECT VERSION();" 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "MySQL started successfully."
} else {
    Write-Host "MySQL did not start correctly."
    exit 1
}
