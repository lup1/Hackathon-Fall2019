@echo off
echo Unregistering the Sample Forms from the GAC
gacutil /uf SwingInterop

echo Cleaning SampleForms directory
cd SampleForms
rmdir /s /q bin
rmdir /s /q obj
cd..
echo Cleaning SWINGInteropEngineLibrary directory
cd SWINGInteropEngineLibrary
rmdir /s /q bin
rmdir /s /q obj
cd ..
echo Cleaning SWINGInteropInvoker directory
cd SWINGInteropInvoker
rmdir /s /q bin
rmdir /s /q obj
cd ..
echo Cleaning SWINGInteropAdmin directory
cd SWINGInteropAdmin
rmdir /s /q bin
rmdir /s /q obj
cd ..
echo Clean complete
