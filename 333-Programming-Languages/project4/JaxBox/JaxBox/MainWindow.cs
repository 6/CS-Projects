using System;
using Gtk;

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
}
