"""
CS333 Task 2 - Sarah Harmon and Peter Graham

Develop a semantic description of the meaning of the assignment statement in
CoffeeScript.
"""
def MAssignment(assign, state):
  # This function covers assignment of a variable, property, or index to a value
  state[MAssignable(assign.assignable)] = MExpression(assign.expr, state)
  return state
