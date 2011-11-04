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

def MIf(c, state):
  if c.type == "ifelse":
    if MBooleanExpression(c.booleanexpr, state):
      return MEmbeddedStatement(c.embeddedstatement1, state)
    else:
      return MEmbeddedStatement(c.embeddedstatement2, state)
  else: # "if" statement without an "else"
    if MBooleanExpression(c.booleanexpr, state):
      return MEmbeddedStatement(c.embeddedstatement, state)
    else:
      return state
      
def MForEach(f, state):
  for identifier in MExpression(f.expr):
    state = MEmbeddedStatement(f.embeddedstatement, state)
  return state
  
def MWhile(w, state):
  if not MBooleanExpression(w.booleanexpr, state):
    return state
  return MWhile(w, MEmbeddedStatement(w.embeddedstatement, state))
