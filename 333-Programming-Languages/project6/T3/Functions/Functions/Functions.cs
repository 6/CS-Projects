using System;

namespace Functions
{
	public class Functions
	{
		public Functions ()
		{
			overload ();
			overload (123);
			overload ("abc");
			optargs (123, c: -257);
		}
		
		/**
		 * In order to override a function, you must include the override keyword
		 */
		public override string ToString ()
		{
			return string.Format ("this is a function");
		}
		
		
		/**
		 * Example of function overloading
		 */
		public void overload() { Console.WriteLine(1); }
		public void overload(int a) { Console.WriteLine(2); }
		public void overload(string a) { Console.WriteLine(3); }
		
		/**
		 * Optional arguments
		 */
		public void optargs(int a, int b = 234, int c = 100) {
			Console.WriteLine(a + b + c);
		}
		
	}
}

