
// This file has been generated by the GUI designer. Do not modify.

public partial class MainWindow
{
	private global::Gtk.VBox vbox1;
	private global::Gtk.HBox hbox1;
	private global::Gtk.Button button1;
	private global::Gtk.Button button2;
	private global::Gtk.Button button3;
	private global::Gtk.Button button4;
	private global::Gtk.TextView textview1;
	
	protected virtual void Build ()
	{
		global::Stetic.Gui.Initialize (this);
		// Widget MainWindow
		this.Name = "MainWindow";
		this.Title = global::Mono.Unix.Catalog.GetString ("MainWindow");
		this.WindowPosition = ((global::Gtk.WindowPosition)(4));
		// Container child MainWindow.Gtk.Container+ContainerChild
		this.vbox1 = new global::Gtk.VBox ();
		this.vbox1.Name = "vbox1";
		this.vbox1.Spacing = 6;
		// Container child vbox1.Gtk.Box+BoxChild
		this.hbox1 = new global::Gtk.HBox ();
		this.hbox1.Name = "hbox1";
		this.hbox1.Spacing = 6;
		// Container child hbox1.Gtk.Box+BoxChild
		this.button1 = new global::Gtk.Button ();
		this.button1.CanFocus = true;
		this.button1.Name = "button1";
		this.button1.UseUnderline = true;
		this.button1.Label = global::Mono.Unix.Catalog.GetString ("Clear");
		this.hbox1.Add (this.button1);
		global::Gtk.Box.BoxChild w1 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.button1]));
		w1.Position = 0;
		w1.Expand = false;
		w1.Fill = false;
		// Container child hbox1.Gtk.Box+BoxChild
		this.button2 = new global::Gtk.Button ();
		this.button2.CanFocus = true;
		this.button2.Name = "button2";
		this.button2.UseUnderline = true;
		this.button2.Label = global::Mono.Unix.Catalog.GetString ("Upper Case");
		this.hbox1.Add (this.button2);
		global::Gtk.Box.BoxChild w2 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.button2]));
		w2.Position = 1;
		w2.Expand = false;
		w2.Fill = false;
		// Container child hbox1.Gtk.Box+BoxChild
		this.button3 = new global::Gtk.Button ();
		this.button3.CanFocus = true;
		this.button3.Name = "button3";
		this.button3.UseUnderline = true;
		this.button3.Label = global::Mono.Unix.Catalog.GetString ("Lower Case");
		this.hbox1.Add (this.button3);
		global::Gtk.Box.BoxChild w3 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.button3]));
		w3.Position = 2;
		w3.Expand = false;
		w3.Fill = false;
		// Container child hbox1.Gtk.Box+BoxChild
		this.button4 = new global::Gtk.Button ();
		this.button4.CanFocus = true;
		this.button4.Name = "button4";
		this.button4.UseUnderline = true;
		this.button4.Label = global::Mono.Unix.Catalog.GetString ("Save");
		this.hbox1.Add (this.button4);
		global::Gtk.Box.BoxChild w4 = ((global::Gtk.Box.BoxChild)(this.hbox1 [this.button4]));
		w4.Position = 3;
		w4.Expand = false;
		w4.Fill = false;
		this.vbox1.Add (this.hbox1);
		global::Gtk.Box.BoxChild w5 = ((global::Gtk.Box.BoxChild)(this.vbox1 [this.hbox1]));
		w5.Position = 0;
		w5.Expand = false;
		w5.Fill = false;
		// Container child vbox1.Gtk.Box+BoxChild
		this.textview1 = new global::Gtk.TextView ();
		this.textview1.CanFocus = true;
		this.textview1.Name = "textview1";
		this.vbox1.Add (this.textview1);
		global::Gtk.Box.BoxChild w6 = ((global::Gtk.Box.BoxChild)(this.vbox1 [this.textview1]));
		w6.Position = 1;
		this.Add (this.vbox1);
		if ((this.Child != null)) {
			this.Child.ShowAll ();
		}
		this.DefaultWidth = 400;
		this.DefaultHeight = 300;
		this.Show ();
		this.DeleteEvent += new global::Gtk.DeleteEventHandler (this.OnDeleteEvent);
		this.button1.Clicked += new global::System.EventHandler (this.OnButton1Clicked);
		this.button2.Clicked += new global::System.EventHandler (this.OnButton2Clicked);
		this.button3.Clicked += new global::System.EventHandler (this.OnButton3Clicked);
		this.button4.Clicked += new global::System.EventHandler (this.OnButton4Clicked);
	}
}
