using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Data;

namespace SWINGInteropEngine
{
	public class Invoker
	{
		private const int DEFAULT_SERVICE_PORT = 5656;

		private static int servicePort = DEFAULT_SERVICE_PORT;

		[STAThread]
		static void Main(String[] args) 
		{
			// manage the incoming parameters
			ManageParameters(args);

			try
			{
				// start the engine running
				Engine.Start(servicePort);

				// wait until receive an exit signal from the admin client
				while (Engine.Enabled)
				{
					System.Threading.Thread.Sleep(1000);
				}
			}
			catch (Exception)
			{
				// gracefully terminate
				MessageBox.Show("The SWING/Windows Forms Interop Engine could not be started.  You should consult the event log for more information.","Could not start engine");
			}
		}


		static void ManageParameters(String[] args)
		{
			String incomingEnginePort = "";

			// Check the incoming arguments
			foreach (String arg in args)
			{
				if (arg.ToUpper().StartsWith(@"/P:"))
				{
					incomingEnginePort = arg.Substring(3,arg.Length-3);
				}
			}

			if (incomingEnginePort != "")
			{
				try
				{
					servicePort = Int32.Parse(incomingEnginePort);
				}
				catch (Exception)
				{
					// malformed argument
				}
			}
		}
	}
}
