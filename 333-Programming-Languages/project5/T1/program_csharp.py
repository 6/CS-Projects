"""
CS333 Task 1 - Sarah Harmon and Peter Graham

Semantic description of the Program function and the Initial State in C#.
"""

def MProgram(program):
  # Program is called "compilation-unit" in the C# grammar
  state = MInitialState(program.using_directives, program.global_attributes)
  return MNamespaceMemberDeclaration(program.body, state)

def MInitialState(opt_directives, opt_globals):
  # Directives and globals are optional, but modify the initial state if present
  state = {}
  for dir in opt_directives:
    if dir.type == "using-alias-directive":
      state[dir.identifier] = MNamespaceOrTypeName(dir.namespace_or_type_name)
    # otherwise, using-namespace-directive, so doesn't modify state
  for section in opt_globals:
    for attr in section:
      state[MAttributeTarget(attr.target)] = MAttributeList(attr.list)
  return state
