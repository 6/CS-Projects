import symbols

a = 0
b = 1
c = 2
d = 3

def function1():
	a = 10 + d
	b = 11.0
	global c
	c = 3.3
	
	symbols.showST(locals(), "function01 locals")
	
	for i in range(1):
		a = 20 + b
		b = 21
		c = 23.0
		
		symbols.showST(locals(), "function1 loop")
		
	symbols.showST(locals(), "function1 locals")
	
def function2():
	a = 30
	b = 31
	c = 32
	d = 33
	
	symbols.showST(locals(), "function2 locals")
	
	def function3():
		a = 40 + c
		b = 41
		global d
		d = 43
		
		symbols.showST(locals(), "function3 locals")
	
	function3()
	symbols.showST(locals(), "function2 locals")
	
symbols.showST(globals(), "globals")
function1()
symbols.showST(globals(), "globals")
function2()
symbols.showST(globals(), "globals")
