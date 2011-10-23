using System;
using Gtk;

public partial class MainWindow: Gtk.Window
{	
	private Gdk.Pixbuf logo;
	
	public MainWindow (): base (Gtk.WindowType.Toplevel)
	{
		Build ();
	}
	
	protected void OnDeleteEvent (object sender, DeleteEventArgs a)
	{
		Application.Quit ();
		a.RetVal = true;
	}

	protected void OnQuitActionActivated (object sender, System.EventArgs e)
	{
		Application.Quit ();
	}
	
	protected void OnOpenActionActivated (object sender, System.EventArgs e)
	{
		ShowOpenDialog ();
	}
	
	protected void OnSaveActionActivated (object sender, System.EventArgs e)
	{
		ShowSaveDialog ();
	}
	
	
	
	public string ShowSaveDialog()
	{
		string result = null;
		Gtk.FileChooserDialog saveDialog = new Gtk.FileChooserDialog("Save as", null, Gtk.FileChooserAction.Save, "Cancel", Gtk.ResponseType.Cancel, "Save", Gtk.ResponseType.Accept);

		if (saveDialog.Run() == (int)Gtk.ResponseType.Accept)
		{
			result = saveDialog.Filename;
		}

		saveDialog.Destroy();
		return result;
	}
	
	public string ShowOpenDialog()
	{
			string result = null;
			Gtk.FileChooserDialog openDialog = new Gtk.FileChooserDialog("Open", null, Gtk.FileChooserAction.Open, "Cancel", Gtk.ResponseType.Cancel, "Open", Gtk.ResponseType.Accept);

			if (openDialog.Run() == (int)Gtk.ResponseType.Accept)
			{
				result = openDialog.Filename;
			}

			openDialog.Destroy();
			return result;
	}
	
	public void Copy (string data)
	{
		Clipboard cb = Clipboard.Get(Gdk.Selection.Clipboard);
		cb.Text = data.Replace("\r\n", "\n");
	}
	
	public string Paste ()
	{
		Clipboard cb = Clipboard.Get(Gdk.Selection.Clipboard);
		return cb.WaitForText();
	}

	protected void OnCopyActionActivated (object sender, System.EventArgs e)
	{
		Copy ("");
	}

	protected void OnPasteActionActivated (object sender, System.EventArgs e)
	{
		Paste ();
	}
	
	protected virtual void OnAbout(object sender, System.EventArgs e)
  	{	
    	AboutDialog about = new AboutDialog();
	    about.ProgramName = "JaxBox";
		string[] authors = {"Sarah Harmon: Technical Director", "Katharine Harmon: UI Designer", "Peter Graham: Programmer"};
	 	about.Authors = authors;
		about.Version = "1.0.0";
     	about.Copyright = "© 2011-2012";
		
		logo = new Gdk.Pixbuf ("/Users/sarahharmon/Desktop/Current School Work/CS-Projects/333-Programming-Languages/project4/JaxBox/JaxBox/img/JB_logo.png"); 
		about.Logo = logo;
		
     	// Show the Dialog and pass it control
     	about.Run();
     
     	// Destroy the dialog
     	about.Destroy();
  	} 
}
