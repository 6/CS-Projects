import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Prompts for a digit until 0 is entered.
 */
public class Prompt
{
	public static void main(String[] args)
	{
		BufferedReader input = 
			new BufferedReader(
			new InputStreamReader(System.in));
		
		int val = -1;
		while (val != 0)
		{
			try
			{
				System.out.print("Enter a number 0..9: ");
				String line = input.readLine();
				val = Integer.parseInt(line);
				
				if (val < 0 || val > 9)
				{
					throw new Exception(String.format(
						"number %d is out of range (0..9)", val));
				}
				
				System.out.println(String.format(
					"\taccepted: %d", val));
			}
			catch (IOException ioe)
			{
				System.err.println(
					"Input not valid: " + ioe.getMessage());
			}
			catch (NumberFormatException nfe)
			{
				System.err.println(
					"Value is not a number: " + nfe.getMessage());
			}
			catch (Exception e)
			{
				System.err.println(
					"Error: " + e.getMessage());
			}
		}
		
		System.out.println("Terminating");
	}
}