import java.util.Arrays;

public class TestAssert
{
	public static boolean check()
	{
		System.out.println("TestAssert.check().");
		return true;
	}

	public static void swap(int[] data, int i, int j)
	{
		assert (data != null) : "Sort attempted on null reference.";
		assert (i >= 0 && i < data.length) : "Index i out of bounds: " + i;
		assert (j >= 0 && j < data.length) : "Index j out of bounds: " + j;
		
		int swap = data[i];
		data[i] = data[j];
		data[j] = swap;
		
		assert (TestAssert.check()) : "Testing something...";
	}

	public static void main(String[] args)
	{	
		int[] data = new int[] {23, 21, 9, 32, 8, 54, 2};
		TestAssert.swap(data, 0, 4);
		TestAssert.swap(data, 3, 5);
		
		TestAssert.swap(data, 2, 10);
	}
}