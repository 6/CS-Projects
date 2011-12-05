using System;
using System.Drawing;
using MonoMac.Foundation;
using MonoMac.AppKit;
using MonoMac.ObjCRuntime;

namespace CheckIt
{
	public partial class AppDelegate : NSApplicationDelegate
	{
		MainWindowController mainWindowController;
		
		public AppDelegate ()
		{
		}

		public override void FinishedLaunching (NSObject notification)
		{	
			NSMenu menu = new NSMenu("");
			
			NSStatusItem statusItem = NSStatusBar.SystemStatusBar.CreateStatusItem(32f);
			statusItem.Image = NSImage.ImageNamed("chk_icon.png");
			statusItem.ToolTip = "CheckIt";
			
			NSMenuItem menuItem = new NSMenuItem("Add/Edit Tasks...", showEditWindow);
			//menuItem.ToolTip = "Edit My To-Do List";
			
			menu.AddItem(menuItem);
     		statusItem.Menu = menu;
		}
		
		public void showEditWindow(object sender, EventArgs e)
		{
			if (mainWindowController == null)
			{
				mainWindowController = new MainWindowController ();
				mainWindowController.Window.MakeKeyAndOrderFront (this);
				mainWindowController.Window.Title = "Edit My Task List";
				mainWindowController.Window.Level = NSWindowLevel.PopUpMenu;
				
				//NSToolbar tb = 
				//mainWindowController.Window.Toolbar = new NSToolbar();
				//mainWindowController.Window.Toolbar.DisplayMode = NSToolbarDisplayMode.Icon;
				
				//mainWindowController.Window.Toolbar.InsertItem("Add", 0);
				
				/*NSToolbar tb = new NSToolbar("");
				tb.DisplayMode = NSToolbarDisplayMode.IconAndLabel;
				mainWindowController.Window.Toolbar = tb;
				
				NSToolbarItem adder = new NSToolbarItem("Add");
				adder.Label = "Add";
				adder.PaletteLabel = "Add";
				adder.Image = NSImage.ImageNamed("add.png");
				adder.ToolTip = "Insert New Task";
				tb.InsertItem("Add", 0);
				//adder.Toolbar = tb;
				adder.Tag = 433;*/
				
				NSToolbarItem adder = new NSToolbarItem();
				adder.Tag = 433;
				//adder.Action = Selector(addItem:);
				//adder.
				//NSToolbarItem adder2 = mainWindowController.Window.Toolbar.It
				//adder.Action = Selector(addItem);
				
				//adder.Action = addItem();
			}
  		}
		
		/*public MonoMac.ObjCRuntime.Selector addItem()
		{
			return new MonoMac.ObjCRuntime.Selector("Add");
		}*/
			
		public override bool ApplicationShouldTerminateAfterLastWindowClosed (NSApplication sender)
		{
			mainWindowController = null;
     		return false;
		}
	}
}

