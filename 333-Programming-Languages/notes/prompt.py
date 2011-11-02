import sys

def main():
	'''
	Prompts for a digit until 0 is entered.
	'''
	val = -1;
	while val != 0:
		try:
			val = int(raw_input("Enter a number 0..9: "))
			
			if not (0 <= val <= 9):
				raise Exception(
					"Error number {0} is out of range (0..9)".
					format(val))
			
			print "\taccepted: {0}".format(val);
				
		except ValueError:
			sys.stderr.write("Error: value is not a number\n")
		
		except Exception as e:
			sys.stderr.write("Error: {0}\n".format(e))
		
	print "Terminating"
	
if __name__ == "__main__":
	main()