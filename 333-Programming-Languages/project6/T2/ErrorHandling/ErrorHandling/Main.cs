using System;

namespace ErrorHandling
{
	class MainClass
	{
		/**
		 * Demonstrates use of try-catch-finally for exception handling
		 */
		public static void error_handling(){
			try {
				int zero = 0;
				int this_will_break = 100 / zero;
			} catch (DivideByZeroException e) {
				Console.WriteLine("MESSAGE: "+ e.Message);
				Console.WriteLine("STACKTRACE: "+ e.StackTrace);
			} finally {
				Console.WriteLine("FINALLY, DO THIS");	
			}
			
		}
		
		public static void Main (string[] args)
		{
			error_handling();
		}
	}
}
