package com.microsoft.samples.windowsforms;

import java.util.ArrayList;
import java.util.Hashtable;

import SWINGInteropEngine.NotificationRemoting;

public class WindowsForms 
{
	private static NotificationRemoting nr = null;
	private static String _assemblyName = "";
	private static String _namespace = "";
	
	/**
	 * Sets assembly and namespace details for allowing forms to be called with shortname only
	 * @param assemblyName The name of the assembly
	 * @param namespace The name of the namespace
	 */
	public static void SetAssemblyDetails(String assemblyName, String namespace)
	{
		_assemblyName = assemblyName;
		_namespace = namespace;
	}
	
	/**
	 * Opens a channel to the Windows Forms Engine
	 * @throws WindowsFormsException
	 */
	public static void OpenChannel() throws WindowsFormsException
	{
		System.out.println("Establishing UI channel...");
		try
		{
			nr = new NotificationRemoting();
		}
		catch (Exception e)
		{
			System.out.println("Could not establish UI channel.  "+e.toString());
			throw new WindowsFormsException("Could not establish UI channel");
		}
	}
	
	/**
	 * Closes the channel to the Windows Forms Engine
	 *
	 */
	public static void CloseChannel()
	{
		nr = null;
	}
	
	/**
	 * Displays a Windows Form based on the short name
	 * @param formName Name of the form to open
	 * @param dialog Determines whether the form should be dialog (modal)
	 * @return
	 * @throws WindowsFormsException
	 */
	public static String DisplayForm(String formName, boolean dialog)
	  throws WindowsFormsException
	{
		CheckAssemblySettings();
		return DisplayForm(_assemblyName, _namespace+"."+formName,dialog);
	}
	
	/**
	 * Displays a Windows Form based on the short name and allows properties to be sent/returned
	 * @param formName Name of the form to open
	 * @param props Properties to set on the Windows Form
	 * @param reflectedReturn Property to reflect against and return when the form is closed
	 * @param dialog Determines whether the form should be dialog (modal)
	 * @return
	 * @throws WindowsFormsException
	 */
	public static String DisplayForm(String formName,  Hashtable props, String reflectedReturn, boolean dialog)
	  throws WindowsFormsException
	  {
	  	CheckAssemblySettings();
	  	return DisplayForm(_assemblyName, _namespace+"."+formName,props,reflectedReturn,dialog);
	  }
	
	/**
	 * Displays a Windows Form based on the assembly name and long form name
	 * @param assemblyName Name of the assembly in the GAC
	 * @param longFormName Fully qualified name of the form
	 * @param dialog Determines whether the form should be dialog (modal)
	 * @return
	 * @throws WindowsFormsException
	 */
	public static String DisplayForm(String assemblyName, String longFormName, boolean dialog)
	  throws WindowsFormsException
	  {
	  	return DisplayForm(assemblyName,longFormName,new Hashtable(),"",dialog);
	  }
	
	/**
	 * Displays a Windows Form based on the assembly name and long form name and allows properties to be sent / returned
	 * @param assemblyName Name of the assembly in the GAC
	 * @param longFormName Fully qualified name of the form
	 * @param props Properties to set on the Windows Form
	 * @param reflectedReturn Property to reflect against and return when the form is closed
	 * @param dialog Determines whether the form should be dialog (modal)
	 * @return
	 * @throws WindowsFormsException
	 */
	public static String DisplayForm(String assemblyName, String longFormName, Hashtable props, String reflectedReturn, boolean dialog)
	  throws WindowsFormsException
	{
		try
		{
			// Convert the hashtable into keys and values for the remoting call
			ArrayList keys = WindowsFormsUtils.getKeys(props);
			ArrayList values = WindowsFormsUtils.getValues(props);
			
			String result = nr.DisplayForm(assemblyName,longFormName,keys,values,reflectedReturn,dialog);
			return result;
		}
		catch (com.intrinsyc.janet.RemoteException re)
		{
			throw new WindowsFormsException("Form could not be opened.  "+re.toString());
		}
	}

	
	private static void CheckAssemblySettings()
	  throws WindowsFormsException
	  {
		if ((_assemblyName == "") && (_namespace == ""))
		{
			throw new WindowsFormsException("You must set the assembly name and namespace before you can call forms using an unqualified name.");
		}
	  }

}
