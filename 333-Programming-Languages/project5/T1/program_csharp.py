"""
CS333 Task 1 - Sarah Harmon and Peter Graham

Semantic description of the Program function and the Initial State in C#.
"""

def MProgram(program):
  # Program is called "compilation-unit" in the C# grammar
  state = MInitialState(program.using_directives, program.global_attributes)
  return MNamespaceMemberDeclaration(program.body, state)

def MInitialState(opt_directives, opt_globals):
  # Directives and globals are optional and can modify initial state if present
  state = {}
  for dir in opt_directives:
    state = MUsingDirective(dir, state)
  for section in opt_globals:
    state = MGlobalAttributeSection(section, state)
  return state
