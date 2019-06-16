[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
Invoke-Expression (
    Invoke-WebRequest https://github.com/shyiko/jabba/raw/master/install.ps1 -UseBasicParsing
).Content
. $profile
jabba install zulu@1.10.0-2
jabba use zulu@1.10.0-2
Write-Host "##vso[task.setvariable variable=JAVA_HOME]$(jabba which $(jabba current))"
