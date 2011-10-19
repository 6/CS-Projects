"""
CS333 Homework 4 - Peter Graham

Tests the associativity and precedence rules used in Python for the arithmetic
operators, {+ - * / **}.
"""

def print_precedence(op1, op2, op_test_string, value_if_op1_greater_precedence):
  if eval(op_test_string) == value_if_op1_greater_precedence:
    print op1,"takes precedence over",op2,"because",op_test_string,"==",\
      value_if_op1_greater_precedence
  else:
    print op1,"doesn't take precedence over",op2,"because",op_test_string,"!=",\
      value_if_op1_greater_precedence

if __name__ == "__main__":
  # associativity rule
  print ("left" if (12/3*2) == 8 else "right")+"-associative"

  # precedence rules
  print_precedence("**", "*", "2*3**2", 18)
  print_precedence("**", "/", "18/3**2", 2)
  print_precedence("*", "+", "1+3*2", 7)
  print_precedence("*", "-", "6-3*2", 0)
  print_precedence("/", "+", "6+3/3", 7)
  print_precedence("/", "-", "6-3/3", 5)
  # Note: the precedence between ** and +- can be inferred from the above tests