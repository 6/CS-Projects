fs = require 'fs'
p = console.log
abstract = require './abstract_classes'

class Token
  # Represent tokens coming from lexical analysis. Has a type and optional value
  constructor: (@tokenType, @value) ->
  
  # Print out the Token in a readable manner
  toS: () -> if @value then "<#{@tokenType}:#{@value}>" else "<#{@tokenType}>"
  
  ###
  Compares type (and value if applicable) of param token and this token object.
  Returns true if they are equal, or false if they are not equal
  ###
  isEqual: (token) ->
    return @tokenType == token.tokenType unless @value? and token.value?
    @tokenType == token.tokenType and @value == token.value

class Lexer
  ###
  Turns the line-by-line token strings output by the Flex lexer into a 
  Token-type stream for the Parser
  ###
  constructor: (@tokenStrings, @delimiter) ->
    @currToken

  ###
  Reads the next token string, splits based on the delimiter character and
  builds a new Token object. Sets @currToken to this new Token object. If at the
  end of the file, create a special Token object
  ###
  nextToken: () ->
    if @tokenStrings.length == 0
      return @currToken = new Token Tokens.END
    [tokenType, value] = @tokenStrings.shift().split @delimiter
    @currToken = new Token tokenType, value

class Parser
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
    
  parse: () ->
    @lexer.nextToken() # advance to the first token
    this.program().printTree("")
    
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
    decs = decs.concat this.declaration() while this.anyOf(Tokens.Keywords)
    decs

  declaration: () ->
    tokenType = new abstract.Type this.match(new Token "Keyword")
    identifier = new abstract.Variable this.match(new Token "Identifier")
    this.match(new Token "Semicolon")
    new abstract.Declaration tokenType, identifier
  
  statements: () -> []

# Constants
Tokens =
  Keywords: [new Token "Keyword", "int", new Token "Keyword", "float"]
  END: "END"

# Read a file asynchronously and pass the contents to the callback function
readFileAsync = (filename, callback, callbackArgs...) ->
  fs.readFile filename, (error, contents) ->
    if contents?
      callback contents.toString().split("\n"), callbackArgs...
    else
      p "Error reading file: #{filename}. Exiting."
      process.exit 1

# Print out the AST of given a list of token strings and their delimiter
printTree = (tokenStrings, delimiter) ->
  lexer = new Lexer tokenStrings, delimiter
  parser = new Parser lexer
  parser.parse()

# Main method expects a filename to be passed in through command line
exports.main = (argv) ->
  [filename, delimiter] = [argv[2], argv[3]]
  unless filename? and delimiter
    p "Please specify a filename and delimiter. Exiting."
    process.exit 1
  readFileAsync filename, printTree, delimiter
