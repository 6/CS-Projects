class exports.Token
  # Represent tokens coming from lexical analysis. Has a type and optional value
  constructor: (@tokenType, @value) ->
  
  # Print out the Token in a readable manner
  toS: () -> if @value then "#{@tokenType}: #{@value}" else @tokenType
  
  ###
  Compares type (and value if applicable) of param token and this token object.
  Returns true if they are equal, or false if they are not equal
  ###
  isEqual: (token) ->
    return @tokenType == token.tokenType unless @value? and token.value?
    @tokenType == token.tokenType and @value == token.value
