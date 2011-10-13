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
      toReturn = @lexer.currToken
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
      return true if token.tokenType == @lexer.currToken.tokenType
    false
    
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
  
  declarations: () ->
    decs = []
    while this.anyOf(Tokens.Keywords)
      tokenType = new abstract.Type this.match(new Token "Keyword")
      identifier = new abstract.Variable this.match(new Token "Identifier")
      this.match(new Token "Semicolon")
      decs = decs.concat(new abstract.Declaration tokenType, identifier)
    decs
  
  statements: () ->
    stmts = []
    numOpenBrackets = 1
    while numOpenBrackets >= 1
      if @lexer.currToken.isEqual(new Token "Open{")
        numOpenBrackets += 1
      else if @lexer.currToken.isEqual(new Token "Close}")
        numOpenBrackets -= 1
      else
        stmts = stmts.concat this.statement()
    stmts
  
  statement: () ->
    if this.anyOf([new Token "Keyword", "if"])
      return this.ifStatement()
    else
      return this.assignment()
  
  ifStatement: () ->
    this.match(new Token "Keyword", "if")
    this.match(new Token "Open(")
    expr = this.expression()
    this.match(new Token "Close)")
    stmtIf = this.statement()
    # optional else conditional with statement
    stmtElse = null
    if this.anyOf([new Token "Keyword", "else"])
      @lexer.nextToken()
      stmtElse = this.statement()
    new abstract.IfStatement expr, stmtIf, stmtElse

  assignment: () ->
    identifier = this.match(new Token "Identifier")
    this.match(new Token "Assignment")
    expression = this.expression()
    this.match(new Token "Semicolon")
    new abstract.Assignment identifier, expression

  expression: () ->
    expr = null
    if this.anyOf(Tokens.Literals)
      expr = new abstract.Value @lexer.currToken
    else if this.anyOf(Tokens.Unary) or this.anyOf(Tokens.Binary)
      expr = "TODO"
    else #variableRef
      expr = "TODO"
    @lexer.nextToken()
    expr
