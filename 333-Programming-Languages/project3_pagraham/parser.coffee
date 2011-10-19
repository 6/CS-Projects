p = console.log
abstract = require './abstract_classes'
Token = require('./token').Token
Tokens = require('./constants').Tokens

class exports.Parser
  # Ties together all the components of lexical analyis
  constructor: (@lexer) ->
  
  ###
  Compares param token to lexer's currToken. If match, advance to nextToken and 
  return the value of the matched token. Otherwise, it will call error()
  ###
  match: (token) ->
    if token.isEqual @lexer.currToken
      toReturn = @lexer.currToken.value
      @lexer.nextToken()
      return toReturn
    this.error token
  
  ###
  Accepts a Token object parameter and prints error indicating that the Parser
  expected one token but saw the Lexer's current token instead
  ###
  error: (token) ->
    p "Line #{@lexer.lineCount - @lexer.tokenStrings.length}"
    p "Expected #{token.toS()}, found #{@lexer.currToken.toS()}. Exiting."
    process.exit 1
  
  ###
  Accepts a list of Tokens and returns true if the list contains a Token of the 
  same type as the current token
  ###
  anyOf: (tokens) ->
    for token in tokens
      return true if token.isEqual @lexer.currToken
    false
    
  ###
  The following are parse methods for various parts of the Clite grammar.
  ###
  # Program -> int main (){ Declarations Statements }
  program: () ->
    this.match(new Token "Keyword", "int")
    this.match(new Token "Identifier", "main")
    this.match(new Token "Open(")
    this.match(new Token "Close)")
    this.match(new Token "Open{")
    decpart = this.declarations()
    body = this.statements()
    this.match(new Token "Close}")
    this.match(new Token Tokens.END)
    new abstract.Program decpart, body
  
  # Declarations -> {Declaration}
  declarations: () ->
    decs = []
    while this.anyOf(Tokens.Types)
      decs = decs.concat(this.declaration())
    decs
  
  # Declaration -> Type Identifier {, Identifier};
  declaration: () ->
    decs = []
    tokenType = new abstract.Type this.match(new Token "Keyword")
    identifier = new abstract.Variable this.match(new Token "Identifier")
    decs = decs.concat new abstract.Declaration(tokenType, identifier)
    # Check for multiple assignment
    while this.anyOf([new Token "Comma"])
      this.match(new Token "Comma")
      identifier = new abstract.Variable this.match(new Token "Identifier")
      decs = decs.concat new abstract.Declaration(tokenType, identifier)
    this.match(new Token "Semicolon")
    decs
  
  # Statements -> {Statement}
  statements: () ->
    stmts = []
    # Keep track of the number of open brackets, so know when to stop parsing
    # statements.
    numOpenBrackets = 1
    while numOpenBrackets >= 1
      if this.anyOf([new Token "Open{"])
        numOpenBrackets += 1
      else if this.anyOf([new Token "Close}"])
        numOpenBrackets -= 1
      else
        stmts = stmts.concat this.statement()
    stmts
  
  # Statement -> Assignment | IfStatement
  statement: () ->
    if this.anyOf([new Token "Keyword", "if"])
      return this.ifStatement()
    else if this.anyOf([new Token "Keyword", "while"])
      return this.whileStatement()
    else
      return this.assignment()
  
  # IfStatement -> if (Expression) Statement [else Statement]
  ifStatement: () ->
    this.match(new Token "Keyword", "if")
    this.match(new Token "Open(")
    expr = this.expression()
    this.match(new Token "Close)")
    stmtIf = this.statement()
    # Optional else conditional with statement
    if this.anyOf([new Token "Keyword", "else"])
      @lexer.nextToken()
      stmtElse = this.statement()
    new abstract.IfStatement expr, stmtIf, stmtElse
  
  # WhileStatement -> while (Expression) Statement
  whileStatement: () ->
    this.match(new Token "Keyword", "while")
    this.match(new Token "Open(")
    expr = this.expression()
    this.match(new Token "Close)")
    stmt = this.statement()
    new abstract.WhileStatement expr, stmt

  # Assignment -> Identifier = Expression;
  assignment: () ->
    identifier = this.match(new Token "Identifier")
    this.match(new Token "Assignment")
    expr = this.expression()
    this.match(new Token "Semicolon")
    new abstract.Assignment identifier, expr

  # Expression -> Addition [EqOp Addition]
  expression: () ->
    add = this.addition()
    if this.anyOf(Tokens.OpsEquality)
      eqOp = new abstract.Operator this.match(new Token "Operator")
      return new abstract.Binary add, eqOp, this.addition()
    else
      return add

  # Term -> Factor {MulOp Factor}
  term: () ->
    fct = this.factor()
    # Optional multiplication or division
    while this.anyOf(Tokens.OpsMultiply)
      mulOp = new abstract.Operator this.match(new Token "Operator")
      fct = new abstract.Binary fct, mulOp, this.factor()
    fct
  
  # Factor -> [UnaryOp] Factor
  factor: () ->
    # Optional unary operator
    if this.anyOf(Tokens.Unary)
      unary = new abstract.Operator this.match(new Token "Operator")
      return new abstract.Unary unary, this.primary()
    else
      return this.primary()
  
  # Addition -> Term {AddOp Term}
  addition: () ->
    trm = this.term()
    while this.anyOf(Tokens.OpsAdd)
      addOp = new abstract.Operator this.match(new Token "Operator")
      trm = new abstract.Binary trm, addOp, this.term()
    trm
  
  # Primary -> Identifier | Literal | (Expression)
  primary: () ->
    if this.anyOf(Tokens.Literals)
      prim = new abstract.Value @lexer.currToken.value
      @lexer.nextToken()
    else if this.anyOf([new Token "Identifier"])
      prim = new abstract.Variable this.match(new Token "Identifier")
    else
      this.match(new Token "Open(")
      prim = this.expression()
      this.match(new Token "Close)")
    prim
