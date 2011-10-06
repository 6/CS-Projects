fs = require 'fs'
p = console.log

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

  # Reads the next token string, splits based on the delimiter character and
  # builds a new Token object. Sets @currToken to this new Token object
  nextToken: () ->
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
    p "Expected #{@lexer.currToken.toS()}, found #{token.toS()}. Exiting."
    process.exit 1
  
  ###
  Accepts a list of Tokens and returns true if the list contains a Token of the 
  same type as the current token
  ###
  anyOf: (tokens) ->
    for token in tokens
      return true if token.tokenType == @lexer.currToken.tokenType
    false

# Read a file asynchronously and pass the contents to the callback function
readFileAsync = (filename, callback) ->
  fs.readFile filename, (error, contents) ->
    if contents?
      callback contents.toString().split("\n")
    else
      p "Error reading file: #{filename}. Exiting."
      process.exit 1

# Run the parser given a list of token strings that represent the token stream
runParser = (tokenStrings) ->
  lexer = new Lexer tokenStrings, "$"
  parser = new Parser lexer
  for i in [1..tokenStrings.length]
    lexer.nextToken()
    p lexer.currToken.toS()

# Main method expects a filename to be passed in through command line
exports.main = (argv) ->
  filename = argv[2]
  unless filename?
    p "Please specify a filename. Exiting."
    process.exit 1
  readFileAsync filename, runParser