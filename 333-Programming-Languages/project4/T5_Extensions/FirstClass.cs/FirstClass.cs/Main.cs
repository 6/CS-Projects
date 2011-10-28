using System;

namespace FirstClass.cs
{
	
	class MainClass
	{	
		public static void Main (string[] args)
		{
			Console.WriteLine(useMethod(new Func<string, string, string>(makeFullName), "Mr.", "Bliss"));
			Console.WriteLine("...has a very tall hat.");
		}
		
		static object useMethod(Delegate method, params object[] args){
	    	return method.DynamicInvoke(args);
		}
		
		static string makeFullName(string firstName, string lastName){
		    return firstName + " " + lastName;
		}
		
	}
}
