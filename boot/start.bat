set WORKDIR=%cd%
cd %WORKDIR%\target
java -Xms256m -Xmx1024m -jar asset.manage-0.0.1-SNAPSHOT.jar
pause