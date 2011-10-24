/*
 * Authors: Peter Graham, Sarah Harmon
 * Description: Project 4, Task 4 - Binary Search (C#)
 */

using System;
using System.Collections;

namespace binary.cs
{
	class MainClass
	{
		/*
		 * Main method.  
		 */ 
		public static void Main (string[] args)
		{
			string searchType = "", again = "";
			int[] customNums, defaultNums = new int[] {1,2,3,4,5,6,7,8,200,28273};
			int target2;
			
			Console.WriteLine(@"
************************************************
BINARY SEARCH IN C#
************************************************");
			
			do {
				//User must type "D" or "C" to indicate Default or Custom Search
				Console.WriteLine("\nSelect [D]efault or [C]ustom search.");
				searchType = Console.ReadLine();
				
				switch (searchType)
				{
					case "d": 
					case "D": Console.WriteLine("Default search selected.");
								//default target value	
								int target = 200;
								
								//print out default array
								Console.Write("Default array: ");
								printArray(defaultNums);
								
								//perform default binary search
								startSearch(defaultNums, target, 0, defaultNums.Length-1);								
							  	break;
					
					case "c": 
					case "C": Console.WriteLine("Custom search selected."); 
								
								//construct array from user
					 			// - custom array should be of form: "1,2,3,4"
								// - i.e., numbers should be sorted and no spaces allowed!
								Console.Write("Enter list of numbers: ");
								string a2 = Console.ReadLine();
								customNums = parseIntoArray(a2);			
								
								//print out custom array
								Console.Write("Custom array: ");
								printArray(customNums);
					
								//get target value from user
							  	Console.Write("\nEnter target value: ");
							  	string t2 = Console.ReadLine();
								
								//try to convert target value (string) into an int
								try
								{
								    target2 = Int32.Parse(t2);
									startSearch(customNums, target2, 0, customNums.Length-1);
								}
								catch (FormatException e)
								{
									// e.Message: "Input string was not in the correct format"
								    Console.WriteLine(e.Message);
								}
					
								break;
					
					default: Console.WriteLine("Unrecognizable command: {0}", searchType); break;
				}
				// user types "A" to continue binary search testing
				Console.WriteLine("\n[A]gain?");
				again = Console.ReadLine();
			} while(again.Equals("A") || again.Equals("a"));	// perform binary search as many times as user likes
				
			
		}
		
		/*
		 * Parses an input string of numbers (e.g., "1,2,3,4" sans quote)
		 * into an int[] array.
		 */ 
		public static int[] parseIntoArray(string input){
			char[] delimiters = {','};
			string[] chop = input.Split(delimiters);
			
			int[] array = new int[chop.Length];
			
			int index = 0;
			foreach(string element in chop) {
				//Console.WriteLine("{0}", element);
				int e = stringToInt(element);
				array[index] = e;
				index++;
			}
			return array;	
		}
		
		/* 
		 * Converts an input string into an integer, if it can.
		 */ 
		public static int stringToInt(string input){
			int i;
			try
			{
			    i = Int32.Parse(input);
				return i;
			}
			catch (FormatException e)
			{
				// e.Message: "Input string was not in the correct format"
			    Console.WriteLine(e.Message);
				return -999;
			}
			
		}
		
		/*
		 * Prints out an int[] array to the console.
		 */
		public static void printArray(int[] a) {
			foreach(int i in a)
			{
				Console.Write("{0} ",i);
			}
		}	
		
		/* 
		 * Calls binary search using given input parameters.
		 * Writes the result out to the console ("not found" or an index value).
		 * @param: int[] array, int target, int low, int high
		 * 
		 */ 
		public static void startSearch(int[] a, int t, int l, int h){
			int result = -1;
			Console.WriteLine("\nSearching for {0}...", t);
			result = BinarySearch(a, t, l, h);
			
			//Console.WriteLine(result);
			
			if (result==-1) {Console.WriteLine("Not found!");}
			else{Console.WriteLine("Found: index {0}", result);}
		}
		
		/*
		 * Recursive binary search implementation in C#.
		 */ 
		public static int BinarySearch(int[] a, int target, int low, int high) {
			if (high < low) {return -1;}
			int mid = low + (high - low) / 2;
			if (a[mid] > target) {return BinarySearch(a, target, low, mid-1);}
			else if (a[mid] < target) {return BinarySearch(a, target, mid+1, high);}
			else return mid;
		}
	}
}
