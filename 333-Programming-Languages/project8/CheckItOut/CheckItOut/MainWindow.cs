using System;
using System.Collections.Generic;
using System.Linq;
using MonoMac.Foundation;
using MonoMac.AppKit;

namespace CheckItOut
{
	public partial class MainWindow : MonoMac.AppKit.NSWindow
	{
		#region Constructors
		
		// Called when created from unmanaged code
		public MainWindow (IntPtr handle) : base (handle)
		{
			Initialize ();
		}
		
		// Called when created directly from a XIB file
		[Export ("initWithCoder:")]
		public MainWindow (NSCoder coder) : base (coder)
		{
			Initialize ();
		}
		
		// Shared initialization code
		void Initialize ()
		{
			/*
			NSToolbar toolbar = new NSToolbar("");
			
			
			NSToolbarItem toolItem = new NSToolbarItem("Add");
			//toolbar.InsertItem("+New Task", 0);
			toolItem.Label = "+New Task";
			toolItem.PaletteLabel = "Add New Task";
			toolbar.InsertItem("Add",0);			
			
			*/
		}
	
		#endregion
	}
}

