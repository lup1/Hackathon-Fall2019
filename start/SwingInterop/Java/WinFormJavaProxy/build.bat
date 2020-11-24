javac -classpath %classpath%;..\lib\janet.jar SWINGInteropEngine\*.java
jar -cvf SWINGInteropEngine.jar SWINGInteropEngine\*.class
javac -classpath %classpath%;SWINGInteropEngine.jar;..\lib\janet.jar com\microsoft\samples\windowsforms\*.java
del SWINGInteropEngine.jar
jar -cvf WinFormJavaProxy.jar .\