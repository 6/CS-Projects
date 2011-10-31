def main():
	'''
	File I/O in python.
	'''
	fp = open("sample.txt", "r")
	lines = fp.readlines()
	fp.close()
	
	for line in lines:
		print line.strip()

if __name__ == "__main__":
	main()