using System;
using System.Collections;
using System.Diagnostics;
using System.Reflection;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using System.Threading;

namespace SWINGInteropEngine
{
	public class Engine
	{
		private static System.Windows.Forms.Form instance;
		public static bool Enabled = true;

		public static void Start(int port)
		{
			try
			{
				Logging.Log("Configuring the service on port "+port);

				// Set up the channel - allow serialization regardless of filter types
				BinaryServerFormatterSinkProvider serverProvider = new BinaryServerFormatterSinkProvider();
				serverProvider.TypeFilterLevel = System.Runtime.Serialization.Formatters.TypeFilterLevel.Full;
				BinaryClientFormatterSinkProvider clientProvider = new BinaryClientFormatterSinkProvider();
				IDictionary props = new Hashtable();
				props["port"] = port;

				TcpChannel tcpChan = new TcpChannel(props,clientProvider,serverProvider);

				ChannelServices.RegisterChannel(tcpChan);				
				RemotingConfiguration.RegisterActivatedServiceType(new ActivatedServiceTypeEntry(typeof(NotificationRemoting)));
				RemotingConfiguration.RegisterActivatedServiceType(new ActivatedServiceTypeEntry(typeof(Administration)));

				Logging.Log("Service successfully configured.  Starting Forms Manager...");

				FormManager fm = new FormManager();

				// Configure the event handler for any updates from the Java client
				fm.NotificationInstance.Notified += new EventHandler(fm.IncomingNotification);
				fm.Updated +=new FormManager.UpdateEventHandler(Form_Notified);
			}
			catch (Exception e)
			{
				Logging.Error("Problems starting the service on port "+port+".  Exception: "+e.ToString());
				throw new Exception("Could not start Engine");
			}
		}

		public static void Stop(int port)
		{
			Logging.Log("Trying to stop the service on port "+port);

			try
			{
				ChannelServices.UnregisterChannel(new TcpChannel(port));
				Logging.Log("Service successfully stopped on port "+port);
			}
			catch (Exception e)
			{
				Logging.Error("Problems stopping the service on port "+port+".  Exception: "+e.ToString());
			}
		}

		public static void Form_Notified(object sender, EventArgs e)
		{
			FormEventArgs fe = null;

			try
			{
				fe = (FormEventArgs)e;
				

				try
				{
					// Try loading the form from the GAC
					Logging.Log("Trying to load Form ("+fe.FormName+") from Assembly in GAC");
					Assembly FormInGAC = Assembly.LoadWithPartialName(fe.Assembly);
					instance = (System.Windows.Forms.Form)FormInGAC.CreateInstance(fe.FormName);
				}
				catch (Exception)
				{
					try
					{
						// Not in GAC - try loading from file
						Logging.Log("Could not load Form ("+fe.FormName+") from Assembly in GAC.  Will try local path instead.");
						ObjectHandle objectHandle = Activator.CreateInstance(fe.Assembly,fe.FormName);
						instance = (System.Windows.Forms.Form)objectHandle.Unwrap();
					}
					catch (Exception e2)
					{
						// can't do this either - throw
						Logging.Error("Nested: "+e2.ToString());
						return;
					}
				}
            
				// Reflect against the properties
				Type formType = instance.GetType();
				ArrayList keys = new ArrayList(fe.Properties.Keys);
				ArrayList values = new ArrayList(fe.Properties.Values);

				for (int f=0; f<keys.Count; f++)
				{
					try
					{
						PropertyInfo myPropInfo = formType.GetProperty((String)keys[f]);
						myPropInfo.SetValue(instance, (String)values[f], null);
					}
					catch (Exception e2)
					{
						Logging.Error("Tried and failed to set property:  "+e2.ToString());
					}
				}

				// Show in dialog and wait for return
				if (fe.Dialog)
				{
					instance.ShowDialog(); 
					
					// Work out the result to return to the calling SWING app
					String result = "";

					try
					{
						PropertyInfo myPropInfo = formType.GetProperty(fe.ReturnProperty);
						result = (String)myPropInfo.GetValue(instance,null);
					}
					catch (Exception)
					{
						// property does not exist in type or is not suitable for return - ignored
					}

					// set the result to return to the caller
					((FormEventArgs)e).Result = result;
				}
				else
				{
					// we do not support reflection on the return type for non-dialog windows

					ThreadStart ts = new ThreadStart(Invoke);
					Thread t = new Thread(ts);
					t.Start();		
				}
			}
			catch (Exception ove)
			{
				Logging.Error("A request to launch a form ("+fe.FormName+") was received but could not be fulfilled.  Exception: "+ove.ToString());
			}
		}

		public static void Invoke()
		{
			System.Windows.Forms.Application.Run(instance);
		}
	}

	public class FormManager
	{
		public event UpdateEventHandler Updated;
		public delegate void UpdateEventHandler(object sender, EventArgs e);
		public Notification NotificationInstance = NotificationFactory.Instance;

		public FormManager()
		{
		}

		public void IncomingNotification(object o, EventArgs e)
		{
			OnUpdated((FormEventArgs)e);
		}

		public virtual void OnUpdated(FormEventArgs e)
		{
			if (Updated != null)
			{
				Updated(this,e);
			}
		}
	}

	public class FormEventArgs : EventArgs
	{
		public String Assembly="";
		public String FormName="";
		public String ReturnProperty="";
		public bool Dialog = true;

		private String result="";
        
        
		public Hashtable Properties = new Hashtable();

		public FormEventArgs(String assembly, String formName, Hashtable properties, String returnProperty, bool dialog)
		{
			this.FormName = formName;
			this.Assembly = assembly;
			this.Properties = properties;
			this.ReturnProperty = returnProperty;
			this.Dialog = dialog;
		}

		public String Result
		{
			get
			{
				return result;
			}
			set
			{
				result = value;
			}
		}
	}

	/// <summary>
	/// This is the actual Notification object itself which contains the event handler
	/// </summary>
	public class Notification 
	{
		public event EventHandler Notified;

		public  Notification()
		{
		}

		public virtual void OnNotified(ref FormEventArgs e)
		{
			if (Notified != null)
			{
				Notified(this,e);
			}
		} 

		public void Update(ref FormEventArgs fea)
		{
			OnNotified(ref fea);
		}
	}

	/// <summary>
	/// This is a factory that allows creation of a singleton of the Notification object
	/// </summary>
	public class NotificationFactory
	{
		private static Notification instance;

		public static Notification Instance
		{
			get
			{
				if (instance == null)
				{
					if (instance == null)
					{
						instance = (Notification)Activator.CreateInstance(typeof(Notification));
					}
				}
				return instance;
			}
		}
	}

	/// <summary>
	/// This is the object that is exposed to the Java SWING Client
	/// </summary>
	public class NotificationRemoting : MarshalByRefObject
	{
		public NotificationRemoting()
		{}

		public String DisplayForm(String assembly, String form, ArrayList keys, ArrayList values, String returnProperty, bool dialog)
		{
			Notification n = NotificationFactory.Instance;
			Hashtable props = new Hashtable();

			if (keys != null)
			{
				// reconstruct the props hashtable
				for (int f=0; f<keys.Count; f++)
				{
					props.Add((String)keys[f],(String)values[f]);
				}
			}

			FormEventArgs fea = new FormEventArgs(assembly,form,props,returnProperty,dialog);
			n.Update(ref fea);

			return fea.Result;
		}
	}

	public class Administration : MarshalByRefObject
	{
		public void Terminate()
		{
			Logging.Log("Sending terminate signal to engine...");
			Engine.Enabled = false;
		}

		public String LogSource()
		{
			return Logging.LogSource;
		}
	}
			
}
