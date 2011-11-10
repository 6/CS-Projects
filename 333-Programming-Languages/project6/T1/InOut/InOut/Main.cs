using System;
using System.IO;

namespace InOut
{
	class MainClass
	{
		/**
		 * Terminal I/O is done through the Console class of the System library
		 */
		public static void terminal_io() {
			string s = Console.ReadLine();
			if ( s == null || s.Equals("") )
				Console.Error.WriteLine("No input");
			else {
				Console.WriteLine("Input: "+s);	
			}
		}
		
		/**
		 * File I/O can be accomplished using the System.IO.File class or the 
		 * System.IO.StreamReader/StreamWriter classes
		 */
		public static void file_io() {
			// using File class
			string[] lines = System.IO.File.ReadAllLines(@"/Users/pete/test.txt");
			Console.WriteLine ("Number of lines: "+lines.Length);
			
			File.WriteAllText("test123", "some\ntest\nhello");
			
			// using streams
			StreamReader sr = new StreamReader(@"/Users/pete/test.txt");
			string wholeFile = sr.ReadToEnd();
			sr.Close();
			Console.WriteLine(wholeFile);
			
			FileStream fs = new FileStream("test234", FileMode.OpenOrCreate, FileAccess.Write);
			StreamWriter sw = new StreamWriter(fs);
			sw.WriteLine("some\nother\ntext");
		}
		
		public static void Main (string[] args)
		{
			terminal_io();
			file_io();
		}
	}
}
