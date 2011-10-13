###
Collection of abstract syntax classes for Clite, each of which has a printTree method that takes a parameter representing the current indentation level.
###
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
  
  printTree: (indent) -> p "#{indent}|-Type: #{@keyword}"

class exports.Variable
  constructor: (@identifier) ->
  
  printTree: (indent) -> p "#{indent}|-Identifier: #{@identifier}"

class exports.Assignment
  constructor: (@identifier, @expression) ->
  
  printTree: (indent) ->
    p "#{indent}|-Assignment"
    p "#{indent}|  |-Variable: #{@identifier}"
    @expression.printTree("#{indent}|  ")     

class exports.Value
  constructor: (@value) ->
  
  printTree: (indent) -> p "#{indent}|-Value: #{@value}"
  
class exports.IfStatement
  constructor: (@expression, @stmtIf, @stmtElse) ->
  
  printTree: (indent) ->
    p "#{indent}|-If"
    @expression.printTree("#{indent}|  ")
    @stmtIf.printTree("#{indent}|  |  ")
    if @stmtElse?
      p "#{indent}|-Else"
      @stmtElse.printTree("#{indent}|  ")

class exports.Operator
  constructor: (@op) ->
  
  printTree: (indent) ->
    p "#{indent}|-Operator: #{@op}"

class exports.Binary
  constructor: (@op, @term1, @term2) ->
  
  printTree: (indent) ->
    p "#{indent}|-Binary"
    @op.printTree("#{indent}|  ")
    @term1.printTree("#{indent}|  ")
    @term2.printTree("#{indent}|  ")

class exports.Unary
  constructor: (@op, @term) ->

  printTree: (indent) ->
    p "#{indent}|-Unary"
    @op.printTree(indent)
    @term.printTree(indent)
    
class exports.Term
  constructor: (@factor, @mulOp, @factor2) ->
    
  printTree: (indent) ->
    if @mulOp?
      p "#{indent}|-Operator: #{@mulOp}"
      @factor.printTree("#{indent}|  ")
      @factor2.printTree("#{indent}|  ")
    else
      @factor.printTree(indent)
  
class exports.Factor
  constructor: (@primary, @unary) ->
  
  printTree: (indent) ->
    @unary.printTree(indent) if @unary?
    @primary.printTree("#{indent}|  ")
