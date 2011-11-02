def main():
	'''
	File I/O in python.
	'''
	# files are a primitive data structure in Python
	fp = open("sample.txt", "r")
	# read everything in at once -> memory being used is different from files.c
	lines = fp.readlines()
	fp.close()
	
	for line in lines:
		print line.strip()

if __name__ == "__main__":
	main()