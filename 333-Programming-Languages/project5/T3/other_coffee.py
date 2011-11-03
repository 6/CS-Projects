"""
CS333 Task 3 - Sarah Harmon and Peter Graham

Semantic description of the meaning of the while statement and the conditional statement in CoffeeScript.
"""

def MWhile(loop, state):
  # There are two possible types of while loops:
  # 1. Normal, with a block of expressions to execute
  # 2. Postfix, with a single expression
  if loop.type == "normal":
    return MWhileNormal(loop.block, state)
  else
    return MWhilePostfix(loop.expr, state)
  
def MIf(c, state):
  # If, else, and unless conditionals. Includes postfix one-liners, e.g.
  # "a = 123 unless something"
  if c.type == "ifblock":
    # an IfBlock can optionally contain "else if" statements
    return MIfBlock(c.block, state)
  elif c.type == "ifelseblock":
    return MIfElseBlock(c.block, state)
  elif c.type == "statement" and MExpression(c.expr, state):
    return MStatement(c.statement, state)
  elif c.type == "expression" and MExpression(c.expr, state)
    return MExpression(c.expr, state)
  return state
