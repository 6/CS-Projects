"""
CS333 Task 1 - Sarah Harmon and Peter Graham

Semantic description of the Program function and the Initial State in
CoffeeScript.
"""

def MProgram(program):
  # Program is called "Root" in the CoffeeScript grammar
  if program.type == "body":
    return MBody(program.body, MInitialState())
  else:
    return MBlock(program.block, MInitialState())

def MInitialState():
  # CoffeeScript does not have an explicit declarations section like CLite.
  # Variables can be declared anywhere in the program. Therefore, return an
  # empty symbol table.
  return {}
