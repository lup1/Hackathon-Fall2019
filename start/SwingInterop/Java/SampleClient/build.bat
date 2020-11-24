cd ..\WinFormJavaProxy
call build.bat
cd ..\SampleClient
javac -classpath ;C:\Interoperability\tools\glue\lib\glue-all.jar;..\WinFormJavaProxy\WinFormJavaProxy.jar;..\lib\janet.jar MainForm.java 
