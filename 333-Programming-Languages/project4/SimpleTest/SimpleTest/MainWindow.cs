using System;
using Gtk;
using System.IO;

public partial class MainWindow: Gtk.Window
{	
	public MainWindow (): base (Gtk.WindowType.Toplevel)
	{
		Build ();
	}
	
	protected void OnDeleteEvent (object sender, DeleteEventArgs a)
	{
		Application.Quit ();
		a.RetVal = true;
	}

	protected void OnButton1Clicked (object sender, System.EventArgs e)
	{
		textview1.Buffer.Text="";//clears the buffer displayed by the TextView  
	}

	protected void OnButton2Clicked (object sender, System.EventArgs e)
	{
		textview1.Buffer.Text=textview1.Buffer.Text.ToUpper();
	}

	protected void OnButton3Clicked (object sender, System.EventArgs e)
	{
		textview1.Buffer.Text=textview1.Buffer.Text.ToLower();
	}

	protected void OnButton4Clicked (object sender, System.EventArgs e)
	{
		StreamWriter sw=new StreamWriter("Test.txt");
        sw.Write(textview1.Buffer.Text); //Write textview1 text to file
        textview1.Buffer.Text="Saved to file !"; //Notify user
        sw.Close(); 
	}
}
