#include <iostream>
using namespace std;

/**
 * Prompts for a digit until 0 is entered.
 */
int main(int argc, char **argv)
{
	int val = -1;
	while (val != 0)
	{
		try 
		{
			cout << "Enter a number 0..9: ";
			cin >> val;
			
			if (!cin.good())
			{
				throw "value is not a number";
			}
		
			if( val < 0 || val > 9 ) 
			{
				throw val;
			}
			
			cout << "\taccepted: " << val << endl;
		}
		catch( const char *s )
		{
			cerr << "Error: " << s << endl;
			cin.clear();
			char buf[80];
			cin >> buf;
		}
		catch( int v )
		{
			cerr << "Error: number " << v 
				<< " is out of range (0..9)" << endl;
		}
	}
		
	cout << "Terminating\n";

	return(0);
}