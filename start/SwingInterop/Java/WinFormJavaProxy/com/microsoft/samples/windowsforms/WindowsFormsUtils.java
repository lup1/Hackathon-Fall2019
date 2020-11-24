package com.microsoft.samples.windowsforms;

import java.util.ArrayList;
import java.util.Hashtable;

public class WindowsFormsUtils 
{
	/**
	 * Returns a list of keys from a passed hashtable
	 * @param ht Hashtable to derive the list of keys from
	 * @return
	 */
	public static ArrayList getKeys(Hashtable ht)
	{
		if (ht.size() == 0) return new ArrayList();
		return new ArrayList(ht.keySet());
	}
	
	/**
	 * Returns a list of values from a passed hashtable
	 * @param ht Hashtable to derive the list of values from
	 * @return
	 */
	public static ArrayList getValues(Hashtable ht)
	{
		if (ht.size() == 0) return new ArrayList();
		return new ArrayList(ht.values());
	}

}
