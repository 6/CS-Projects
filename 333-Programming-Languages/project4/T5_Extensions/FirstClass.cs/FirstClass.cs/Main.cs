using System;

namespace FirstClass.cs
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			//Method 1 of Function Declaration
			Func<string,string> first = delegate(string s)
            {
                return "I'm number one!";
            };
			
			string one = first("This param doesn't actually matter!");
			Console.WriteLine(one);
			
			//Method 2 of Function Declaration
			Func<string,string> second = s => "Luigi almost win!";
			
			string two = second("Neither does this one!");
			Console.WriteLine(two);
		}
	}
}
