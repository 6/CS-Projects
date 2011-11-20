using System;
using System.Diagnostics;
using System.IO;

namespace cs333_p7t3
{
	class MainClass
	{
		// public static void Main (string[] args)
		public static void go()
		{
			// Open file for writing
			FileStream fs = new FileStream("timed_trials2", FileMode.OpenOrCreate, FileAccess.Write);
			StreamWriter sw = new StreamWriter(fs);
			
			for (int jj = 0; jj < 100; jj++)
			{				
				// begin timing
				var stopwatch = System.Diagnostics.Stopwatch.StartNew();
				
				// allocate and deallocate memory
				callTheGarbageMan();
				
				// stop timer
				stopwatch.Stop();
				
				// add time to file
				try {
					//sw.WriteLine("Time elapsed: {0}", stopwatch.Elapsed);
					sw.WriteLine("{0}", stopwatch.Elapsed.TotalMilliseconds);
				}
				catch { Console.Out.WriteLine("Streamwriter error.");}
			}
			
			if(sw != null)
			{
				sw.Close();
			}	
		}
		
		public static void callTheGarbageMan() {
			
			Random random = new Random();
		
			int[] trash = new int[10000000];
        	for (int ii = 0; ii < 10000000; ii++)
            	// set to a random integer
				trash[ii] = random.Next(0, 100);
		}	
	}
	
		
}
