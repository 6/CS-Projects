"""
CS333 Task 3 - Sarah Harmon and Peter Graham

Semantic description of the meaning of the do statement and the conditional statement in CoffeeScript.
"""

def MBlock(block, state):
  if block.statements == []:
    return state
  return MStatementList(block.statements, state)

def MDo(d, state):
  if MBooleanExpression(d.booleanexpression, state) == False:
    return state
  state = MEmbeddedStatement(d.embeddedstatement, state)
  return MDo(d, state)
