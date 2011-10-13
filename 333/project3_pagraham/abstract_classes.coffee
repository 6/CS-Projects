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
  constructor: (@type, @identifier) ->

  printTree: (indent) ->
    p "#{indent}|-Declaration"
    @identifier.printTree("#{indent}|  ")
    @type.printTree("#{indent}|  ")

class exports.Type
  constructor: (@keyword) ->
  
  printTree: (indent) ->
    p "#{indent}|-Type: #{@keyword.value}"

class exports.Variable
  constructor: (@identifier) ->
  
  printTree: (indent) ->
    p "#{indent}|-#{@identifier.toS()}"

class exports.Assignment
  constructor: (@identifier, @value) ->
  
  printTree: (indent) ->
    p "#{indent}|-Assignment"
    p "#{indent}|  |-Variable: #{@identifier.toS()}"
    #TODO
