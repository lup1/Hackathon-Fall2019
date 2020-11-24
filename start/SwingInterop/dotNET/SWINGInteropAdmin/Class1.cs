using System;
using System.Collections;
using System.Diagnostics;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;

using SWINGInteropEngine;

namespace SWINGInteropAdmin
{
	class Class1
	{
		private static int DEFAULT_PORT = 5656;
		private static String DEFAULT_URI = "tcp://localhost:"+DEFAULT_PORT.ToString();
		private static Administration admin = null;

		static void Main(string[] args)
		{
			// Check to see if a URL is passed on the command line
			if (args.Length != 0)
			{
				foreach (String arg in args)
				{
					if (arg.ToUpper().StartsWith("/U:"))
					{
						DEFAULT_URI = arg.Substring(3,arg.Length-3);
					}

					if (arg.ToUpper().StartsWith("/P:"))
					{
						String port = arg.Substring(3,arg.Length-3);
						try
						{
							DEFAULT_PORT = Int32.Parse(port);
							DEFAULT_URI = "tcp://localhost:"+DEFAULT_PORT.ToString();
						}
						catch(Exception)
						{
							// malformed port string.  Ignoring.
						}
					}
				}
			}

			Console.Write("Trying to connect to engine...  ");
	
			try
			{
				RemotingConfiguration.RegisterActivatedClientType(typeof(Administration),DEFAULT_URI);
				
				admin = new Administration();
				if (admin == null)
				{
					throw new Exception("Remote call failed.");
				}
			}
			catch (Exception e)
			{
				Console.WriteLine("Failed.  Please ensure that the engine is running.");
				Console.WriteLine(e.ToString());
				return;
			}

			Console.WriteLine("Connected.");

			// check to see what action to run
			foreach(String arg in args)
			{
				if (arg.ToUpper().Equals("/KILL"))
				{
					KillEngine();
				}
				
				if (arg.ToUpper().Equals("/TRACE"))
				{
					Trace();
				}	
			}
		}

		private static void KillEngine()
		{
			admin.Terminate();
			Console.WriteLine("Kill signal sent to engine.");
		}

		private static EventLog swingInteropEventLog = new EventLog("application");
		private static String swingInteropEventLogSource = "";

		private static void Trace()
		{
			// obtain accurate event log source
			swingInteropEventLogSource = admin.LogSource();

			// register against the event log
			swingInteropEventLog.EnableRaisingEvents = true;
			swingInteropEventLog.EntryWritten +=new EntryWrittenEventHandler(swingInteropLog_EntryWritten);

			// display and hold
			Console.WriteLine("Listening for events...  Press Enter to exit.");

			Console.In.Read();
		}

		private static void swingInteropLog_EntryWritten(object sender, EntryWrittenEventArgs e)
		{
			int last = swingInteropEventLog.Entries.Count -1;
			String source = swingInteropEventLog.Entries[last].Source;
			
			if (source.Equals(swingInteropEventLogSource))
			{
				String time = swingInteropEventLog.Entries[last].TimeWritten.ToShortTimeString();
				String message = swingInteropEventLog.Entries[last].Message;

				Console.WriteLine(time+": "+message);
			}
		}
	}
}