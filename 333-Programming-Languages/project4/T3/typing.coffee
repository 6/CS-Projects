"""
Authors: Sarah Harmon, Peter Graham
Description: Project 4, Task 3

Demonstrates all of the basic built-in types and how to construct aggregate
types. Includes demonstrations of which of the standard suite of operators 
manipulate which types and what the resulting type of each operation is.
"""

# NUMERIC TYPE
negative_one_thousand = -10e2
pi = 3.14

# BOOLEAN TYPE
all_true = yes and on and true
all_false = no and off and false

# STRING TYPE
abc = "def"

# Strings can use interpolation (must be surrounded by double quotes)
hello = "Hello #{abc}"

# String can be multiline
hello = 'Hello
world!!!
!!!'

# Strings can be indentation-sensitive using heredocs
python_tabbed = """
def hello(n):
  if n:
    print "hello"
"""

# EMBEDDED JAVASCRIPT TYPE
js = `function() { 
  alert("This is JavaScript");
}`

# NULL TYPE
a = undefined
b = null

# ARRAY TYPE
# The following three assignments result in the same array values.
onetosix = [1, 2, 3, 4, 5, 6]

onetosix = [
  1, 2, 3
  4, 5, 6
]

onetosix = [1..6]

# Arrays can contain different types
abc123 = ["a", "b", "c", 1, 2, 3]

# OBJECT TYPE
# The following three assignments result in the same object values.
grades = {90: ["A", "A-"], 80: {83: "B-", 87: "B+"}}

grades = {
  '90': ["A", "A-"]
  , '80': {
    '83': "B-"
    , '87': "B+"
  }
}

grades =
  90: ["A", "A-"]
  80:
    83: "B-"
    87: "B+"
    
# CLASSES
# Classes are compiled into a JavaScript object.
class Shape
  constructor: (@color) ->
    @color ?= "blue"

# Classes can be extended
class Circle extends Shape
  constructor: (@color, @radius) ->
    super @color

# Initializing a class
c = new Circle "red", 3.5

# OPERATORS - How the standard suite of operators (+ -  / * %) manipulate types
# Standard arithmetic on numbers works (for the most part) as expected
a = 1.0 + 3 - 2 # yields 2
a = 10 % 3 # yields 1
a = 12 / 6.0 * 2 # yields 4
a = 1 / 2 # yields 0.5, rather than floor the division like Python does

# Booleans and null are converted to integers with arithmetic. 
# true = 1, false = 0, and null = 0
two = true + true
zero = false + false
zero = true * false
negative_one = null - true

# For strings, the + operator is used for concatenation
abc = "a" + "b" + "c"

# Numbers will be converted to strings when being concatenated with strings
abc = "abc" + 123 + 456 # The following will yield the string "abc123456"
abc = 123 + 456 + "abc" # The other way around will yield string "579abc"

# Operators can be used with embedded JavaScript
a = `5 + 2` * `10` # yields 25, not 70
b = `"hello"`+`" world"` # yields "hello world"
