using System;

namespace cs333_p7t2
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			//create an array of strings
			string[] letters = new string[26];
			
			// assign values to chars
		    for (int ii = 65; ii < 91; ii++)
			{
		    	letters[ii] = (char)ii;
			}
				
			// upon declaring the array to be null, 
			// chars[] and its associated instances are ripe for collection
		    chars = null;
		}
	}
}
