/*
 * Authors: Peter Graham, Sarah Harmon
 * Description: Project 4, Task 3
 */ 

using System;

namespace types.cs
{
	// This is an example of a struct.
	struct @Class { 
  	public string professor; 
	public string topic; 
	public string[] studentNames; 	
	public int[] studentGrades; 
	public bool overEnrolled;
	 
	public @Class(string p, string t, string[] sN, int[] sG, bool oE) { 
	    professor = p; 
	    topic = t; 
		studentNames = sN;
		studentGrades = sG;
		overEnrolled = oE;
	  } 
	} 
	
	// This is an example interface.
	public interface @Interface {
		void methodToOverride();
	}
	
	/* Example for using an interface:
	 *  class myClass : @Interface { 
	 * 		public static void Main() { 
	 * 			myClass mC = new myClass();  
	 * 			mc.methodToOverride(); 
	 * 		}  
	 * 
	 * 		public void methodToOverride() { 
	 * 			Console.WriteLine("Override engaged!"); 
	 * 		} 
	 * 	}
	 */
	
	// This is an example enum
	public enum Switch {
		On,
		Off
	}
	
	/* Example for using an enum:
	 *  class myClass { 
	 * 		public static void Main() { 
	 * 			Switch s = Switch.On;
	 * 		} 
	 * 	}
	 */
	
	// This is a class!
	class MainClass
	{
		public static void Main (string[] args)
		{
			// C# is strongly typed.
			// Types include: string, bool, float, double, decimal, sbyte, byte, short, ushort, int, uint, long, ulong, and char
			// Standard suite of operators: + - / * %
			
			// Only one operator works for strings: '+'
			// STRINGS: + -> concat
			string a = "one", b = "two", concat = a+b;
			Console.WriteLine("{0}", concat);
			
			// no other operations work with strings, not even with type-casting
			
			// To use our operators on types such as CHAR, BYTE, SBYTE, SHORT and USHORT, we must cast.
			// The items being "operated" on are assumed to be ints and evaluated as such.
			
			char @char1 = 'a', @char2 = 'z', @char3 = (char) (@char1 + @char2);
			Console.WriteLine("{0}", @char3);
			
			char @char4 = 'a', @char5 = 'z', @char6 = (char) (@char4 - @char5);
			Console.WriteLine("{0}", @char6);
			
			byte @byte1 = 40, @byte2 = 2, @byte3 = (byte) (@byte1 / @byte2);
			Console.WriteLine("{0}", @byte3);
			
			sbyte @sbyte1 = 7, @sbyte2 = 6, @sbyte3 = (sbyte) (@sbyte1 * @sbyte2);
			Console.WriteLine("{0}", @sbyte3);
			
			short @short1 = 40, @short2 = 4, @short3 = (short) (@short1 % @short2);
			Console.WriteLine("{0}", @short3);
			
			ushort @ushort1 = 2, @ushort2 = 4, @ushort3 = (ushort) (@ushort1 + @ushort2);
			Console.WriteLine("{0}", @ushort3);
			
			// Our operators do not work with BOOLS at all.
				//"Operator '+' cannot be applied to operands of type 'bool' and 'bool'
				//bool @bool1 = true, @bool2 = true, @bool3 = (bool)(@bool1 + @bool2); 
				//Console.WriteLine("{0}", @bool3);
			
			// Operators work as expected with FLOATS, DECIMALS, DOUBLES, LONGS, ULONGS, INTS, and UINTS.
			// i.e., 1+1=2
			//		 7-4=3
			//		 1*4=4
			//		 12/4=3
			//		 40%2=0
			
			float @float1 = 1.1F, @float2 = 2.2f, @float3 = @float1 + @float2;
			Console.WriteLine("{0}", @float3);
			
			decimal @decimal1 = 7.1M, @decimal2 = 6.2m, @decimal3 = @decimal1 - @decimal2;
			Console.WriteLine("{0}", @decimal3);
			
			double @double1 = 13.2, @double2 = 3827.6d, @double3 = @double1 * @double2;
			Console.WriteLine("{0}", @double3);
			
			long @long1 = 24, @long2 = 12, @long3 = @long1 / @long2;
			Console.WriteLine("{0}", @long3);
			
			ulong @ulong1 = 24, @ulong2 = 12, @ulong3 = @ulong1 % @ulong2;
			Console.WriteLine("{0}", @ulong3);
			
			int @int1 = 72, @int2 = 12, @int3 = @int1 / @int2;
			Console.WriteLine("{0}", @int3);
			
			uint @uint1 = 72, @uint2 = 12, @uint3 = @uint1 % @uint2;
			Console.WriteLine("{0}", @uint3);
		}
		
		
	}
}
