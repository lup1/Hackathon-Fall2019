import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.microsoft.samples.windowsforms.WindowsForms;
import com.microsoft.samples.windowsforms.WindowsFormsException;

/**
 * Here is a sample SWING app that uses the WindowsForms proxy to communicate with the engine
 * in order to display Windows Forms as part of a SWING app.
 * 
 * @author sguest
 *
 */
public class MainForm extends JFrame 
{
	// Some SWING controls
	private javax.swing.JMenuBar jJMenuBar = null;
	private javax.swing.JPanel jPanel = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JButton jButton = null;
	private javax.swing.JTextField jTextField = null;
		
	private javax.swing.JButton jButton1 = null;
	public static void main(String[] args) 
	{
		MainForm mf = new MainForm();
		mf.show();
	}

	public MainForm() 
	{		
		// Initialize the SWING controls
		super();				
		initialize();
		
		// try opening the channel to windows forms engine
		try
		{
			WindowsForms.OpenChannel();
			
			// Setting the assembly details is not mandatory, but allows us to use short names
			// for forms moving forward
			WindowsForms.SetAssemblyDetails("SwingInterop","SwingInterop");
		}
		catch (WindowsFormsException wfe)
		{
			JOptionPane.showMessageDialog(jPanel,"Could not open a channel to the Windows Forms Engine.  This SWING form will still run, but cannot communicate with any Windows Forms.  "+wfe.toString());
		}

	}

	/**
	 * Initializer for the SWING form
	 *
	 */
	private void initialize() 
	{
		// Set the look and feel so that this window is like the others that are opened through
		// Windows Forms
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception ulnfe)
		{
			// Ignored - we probably are not running on a non-windows box
		}
		
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setSize(512, 148);
		this.setTitle("A SWING Form calling Windows Forms");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() { 
			public void windowClosing(java.awt.event.WindowEvent e) {    
				System.out.println("Window is closing...");
				System.exit(0);
			}
		});
	}


	private javax.swing.JMenuBar getJJMenuBar() {
		if(jJMenuBar == null) {
			jJMenuBar = new javax.swing.JMenuBar();
		}
		return jJMenuBar;
	}

	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setBounds(18, 18, 115, 15);
			jLabel.setText("Type in a message:");
			jLabel.setName("jLabel");
		}
		return jLabel;
	}

	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJLabel(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJTextField(), null);
			jContentPane.add(getJButton1(), null);
		}
		return jContentPane;
	}

	private javax.swing.JButton getJButton() {
		if(jButton == null) {
			jButton = new javax.swing.JButton();
			jButton.setBounds(272, 63, 101, 32);
			jButton.setText("Open Form1");
			jButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					    				
					try
					{
						// We are going to pass some properties to the Windows Form
						Hashtable props = new Hashtable();
						props.put("Message",jTextField.getText());

						// Open the form in modal mode
						String result = WindowsForms.DisplayForm("Form1",props,"Message", true);
						
						// If we get something back, display the result
						if (result != null)
						{					
							JOptionPane.showMessageDialog(jTextField,"'"+result+"' was returned from the Windows form.");
						}
						
					}
					catch (Exception e2)
					{
						System.out.println(e2.toString());
					}
				}
			});
		}
		return jButton;
	}

	private javax.swing.JTextField getJTextField() {
		if(jTextField == null) {
			jTextField = new javax.swing.JTextField();
			jTextField.setBounds(163, 18, 320, 19);
		}
		return jTextField;
	}

	private javax.swing.JButton getJButton1() {
		if(jButton1 == null) {
			jButton1 = new javax.swing.JButton();
			jButton1.setBounds(376, 63, 101, 32);
			jButton1.setText("Open Form2");
			jButton1.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
					try
					{
						// Open the second form, but not in modal mode
						String result = WindowsForms.DisplayForm("Form2", false);
					}
					catch (Exception e2)
					{
						System.out.println(e2.toString());
					}
				}
			});
		}
		return jButton1;
	}
}  
