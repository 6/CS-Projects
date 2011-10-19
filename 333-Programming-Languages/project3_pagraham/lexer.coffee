p = console.log
Token = require('./token').Token
Tokens = require('./constants').Tokens

class exports.Lexer
  ###
  Turns the line-by-line token strings output by the Flex lexer into a 
  Token-type stream for the Parser
  ###
  constructor: (@tokenStrings, @delimiter) ->
    @currToken
    @lineCount = @tokenStrings.length

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
