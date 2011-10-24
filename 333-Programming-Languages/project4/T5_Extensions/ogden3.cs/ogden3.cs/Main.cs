using System;

namespace ogden3.cs
{
	class MainClass
	{	
		public static void Main (string[] args)
		{
			string name, again;
			Console.Write (@"
************************************************
MAD LIMERICK GENERATOR
************************************************");
			Console.WriteLine("\nHello.  What is your name?");
			name = Console.ReadLine();
			
			Console.WriteLine ("Hello, {0}!", name);
		
			do {
				
				
				Console.WriteLine ("Again?");
				again = Console.ReadLine();
			} while (again.Equals("y") || again.Equals("Y"));
			
			//hippocampus: memory
			//amygdala: emotional memory
			//frontal lobe: personality
				
				//sim scale: nice/mean, 				--> compliments/insults
				//			 extravert/introvert, 		--> talks more, volunteers more info / keeps to self 
				//			 playful/serious			--> jokes/laughs more, informal vocab / formal vocab
			    //			
			
			//entus: http://monodevelop.com/Stetic_GUI_Designer
		}
	}
}
