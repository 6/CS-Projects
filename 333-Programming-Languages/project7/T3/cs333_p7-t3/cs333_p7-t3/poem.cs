using System;
using System.Threading;

namespace cs333_p7t3
{
	class PoemClass
	{
		public static void Main (string[] args)
		{
			theMatrix("UNSEEN COLLECTOR");
			theMatrix("OF THINGS TO BE FORGOTTEN");
			theMatrix("DO THEY DESERVE IT");
		}
		
		public static void theMatrix(string s) {
			
			Random random = new Random();
		
			int counter = 0;
			int finalIndex = 500;
			
			//make new array
			int[] trash = new int[finalIndex];
        	for (int ii = 0; ii < finalIndex; ii++)
			{
            	// set to a random integer
				trash[ii] = random.Next(65, 123);
				counter++;
				
				if (counter < 30 && ii <= finalIndex - 30)
				{
					// print out character based on the random integer
					Console.Out.Write("{0}", (char)trash[ii]);
				}
				else
				{
					counter = 0;	
//					Thread.Sleep(30);
					Thread.Sleep(random.Next(29,49));
					Console.Clear();
					if (ii >= finalIndex - 40)
					{
						Console.Out.WriteLine(s);		
					}
				}	
			}
			
		}	
	}
}
