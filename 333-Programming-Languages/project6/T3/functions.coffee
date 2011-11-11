"""
Authors: Sarah Harmon, Peter Graham
Description: Project 6, Task 3
"""

# Parenthesis are optional if no parameters
funcA = -> console.log "Entered funcA"
funcB = () -> console.log "Entered funcB"
funcA()
funcB()

# Functions with parameters
funcC = (a, b, c) -> console.log a + b - c
funcC 2, 3, 4 

# "Splats" in CoffeeScript for a variable number of arguments
funcD = (a, variableArgs..., b) ->
  console.log "a", a
  console.log "variableArgs", variableArgs
  console.log "b", b
funcD 1, 2, 3, 4, 5, 6
funcD 1, 2

# Default values for arguments
funcE = (a, b = "defaultB") -> console.log "a: #{a}, b: #{b}"
funcE 123
funcE 123, 456

# Nesting functions
funcF = () ->
  funcG = -> console.log "in funcF"
  funcG()
funcF()

# Function overloading isn't supported
funcH = -> console.log "funcH no params version"
funcH = (a) -> console.log "funcH 1 param version", a
funcH()
funcH(1)
