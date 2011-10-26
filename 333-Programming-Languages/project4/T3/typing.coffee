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
    
# TODO: classes    

# TODO: operators
