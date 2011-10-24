/**
 * Demonstrates a polymorphic sorting operation using
 * using interfaces for classes that implement the 
 * Comparable interface.
 */
public class Sorter
{
	public static void sort(Comparable[] a)
	{
		// sorts elements of a in place
		// at each iteration of the outer loop
		// the element at a[i] is properly ordered
		for (int i = 0; i < a.length; i++)
		{
			for (int j = i+1; j < a.length; j++)
			{
				if (a[j].compareTo(a[i]) < 0)
				{
					Comparable swap = a[j];
					a[j] = a[i];
					a[i] = swap;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		for (String str : args)
			System.out.println(str);
		System.out.println("---");
		sort(args);
		for (String str : args)
			System.out.println(str);
	}
}