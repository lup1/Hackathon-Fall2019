package com.microsoft.samples.windowsforms;

public class WindowsFormsException extends Exception 
{
	/**
	 * Exception generated from an error when trying to open a Windows Form
	 *
	 */
	public WindowsFormsException()
	{
		super();
	}
	
	/**
	 * Exception generated from an error when trying to open a Windows Form
	 * @param message Message of the Exception
	 */
	public WindowsFormsException(String message)
	{
		super(message);
	}

}
