using System;

namespace SWINGInteropEngine
{
	public class Logging
	{
		private const String LOG_SOURCE = "Java SWING(tm)/Windows Forms Interoperability";
		private static System.Diagnostics.EventLog swingInteropEv = new System.Diagnostics.EventLog("application",".",LOG_SOURCE);

		public static void Log(String message)
		{
			swingInteropEv.WriteEntry(message);
		}

		public static void Error(String message)
		{
			swingInteropEv.WriteEntry(message,System.Diagnostics.EventLogEntryType.Error);
		}

		public static String LogSource
		{
			get
			{
				return LOG_SOURCE;
			}
		}

	}
}
