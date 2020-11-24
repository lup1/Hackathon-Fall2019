@echo off
echo Building the sample forms...
cd SampleForms
md bin\Debug
csc /res:form1.resx,form2.resx /out:bin\Debug\SwingInterop.dll /target:library *.cs
echo Registering in GAC
gacutil /if bin\Debug\SwingInterop.dll
cd..

echo Building the SWING Interop Engine Library
cd SWINGInteropEngineLibrary
md bin\Debug
csc /target:library /out:bin\Debug\SWINGInteropEngineLibrary.dll *.cs
cd ..

echo Building the SWING Interop Engine
cd SWINGInteropInvoker
md bin\Debug
csc /target:winexe /out:bin\Debug\SWINGInteropEngine.exe /reference:..\SWINGInteropEngineLibrary\bin\Debug\SWINGInteropEngineLibrary.dll *.cs
copy ..\SWINGInteropEngineLibrary\bin\Debug\SWINGInteropEngineLibrary.dll bin\Debug
cd ..

echo Building the Admin Utility
cd SWINGInteropAdmin
md bin\Debug
csc /out:bin\Debug\SWINGInteropAdmin.exe /reference:..\SWINGInteropEngineLibrary\bin\Debug\SWINGInteropEngineLibrary.dll *.cs
copy ..\SWINGInteropEngineLibrary\bin\Debug\SWINGInteropEngineLibrary.dll bin\Debug
cd..

echo Build Complete



