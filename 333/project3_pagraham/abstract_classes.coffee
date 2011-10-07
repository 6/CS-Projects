p = console.log

class exports.Program
  constructor: (@decpart, @body) ->

  printTree: (indent) ->
    p "#{indent}Program"
    p "#{indent}  |-Declarations"
    decl.printTree("#{indent}  |  ") for decl in @decpart
    p "#{indent}  |-Statements"
    stmt.printTree("#{indent}  |  ") for stmt in @body

class exports.Declaration
  constructor: (@variable, @vartype) ->

  printTree: (indent) ->
    print "#{indent}|-Declaration"
    print @variable.printTree("#{indent}|  ")
    print @vartype.printTree("#{indent}|  ")
