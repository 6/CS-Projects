/*
 * Authors: Peter Graham, Sarah Harmon
 * Description: Project 4, Task 2
 * 
 */
using System;

namespace decNScope.cs
{
	class MainClass
	{
		public string sweet = "nice!";
		public static void Main (string[] args)
		{
			/*** Identifier Naming ***/
			// Useful reference: http://msdn.microsoft.com/en-us/library/aa664670(VS.71).aspx
			
			//identifiers can start with a letter an underscore
			string a;
			int[] _ia;
			
			//after the first character, you can use numbers, letters, or "Pc" class connectors
			//note: for Pc char ref: http://www.fileformat.info/info/unicode/category/Pc/list.htm
			double d20sys;
			float _\uFE4F;
			
			// you can use a keyword for your identifier name if you put "@" in front of it
			bool @bool;
			
			//note: $ signs are NOT valid.  
			
			/*** Variable Declarations ***/
			// Types include: string, bool, float, double, decimal, sbyte, byte, short, ushort, int, uint, long, ulong, and char
			
			// single declaration
			char mander;
			decimal eight = 8.8M;
			bool correct; correct=true;
			
			// multiple declarations
			string h, j="";
			int x = 1, y, z = x * 2;
			
			/*** Identifier Scoping ***/
			//variables defined within a class are available to the non-static methods of said class 
			//variables defined within a method are available throughout the method (including nested parts)
			//variables defined within a nested block are only available within that block
			
			//Thus...
			//calling getNum() doesn't mean we have access to variables defined in getNum()
			getNum();
			/*
			 * Note - subtle distinction:
			 * "In Java, the scope of all local variables is from their declarations to the ends of the blocks 
			 * in which those declarations appear. However, in C# the scope of any variable declared in a block 
			 * is the whole block, regardless of the position of the declaration in the block, as long as it is 
			 * not in a nested block. The same is true for methods. Note that C# still requires that all variables 
			 * be declared before they are used. Therefore, although the scope of a variable extends from the 
			 * declaration to the top of the block or subprograms in which that declaration appears, the variable 
			 * still cannot be used above its declaration."
			 */ 
		
		}
		
		// function that helps to demonstrate scoping in C#.
		public void getNum() {
			int num = 6;
			if (num == 3) {string response = sweet;}
			else {string response = "really?  I expected better of you.";}
			
			//if we tried to access the variable response here, we would obtain an error.
		}
		
		// This method fails because it is static and therefore cannot access the class variable "sweet".
		public static void failAtRetrievingVar() {
			Console.WriteLine(sweet);
		}
	}
	
	// this class cannot use the MainClass variable "sweet"
	/*public class OK {
		private string = sweet;
	}*/
}
