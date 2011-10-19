def showST(table, message=None):
	'''
	Prints a formatted symbol table given a dictionary.
	'''
	print "\n"
	print "-"*80
	message = "Symbol Table" if message is None else message
	print "{0:^80}\n{1:^80}".format(message, "-"*len(message))
	print "{0:>20} | {1:<20} {2:<20}".format("name", "type", "value")
	print "-"*80
	for ident in sorted(table):
		binding = table[ident]
		print "{0:>20} | {1:<20} {2:<20}".format(ident, type(binding).__name__, binding)
		
if __name__ == "__main__":
	showST(globals(), "globals")

	# build a dictionary for builtins
	bdict = dict((name, getattr(__builtins__, name)) for name in dir(__builtins__))
	showST(bdict, "builtins")