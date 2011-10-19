p = console.log
fs = require 'fs'
Lexer = require('./lexer').Lexer
Parser = require('./parser').Parser

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
  parser.lexer.nextToken() # advance to the first token
  parser.program().printTree("")

# Main method expects a filename to be passed in through command line
exports.main = (argv) ->
  [filename, delimiter] = [argv[2], argv[3]]
  unless filename? and delimiter
    p "Please specify a filename and delimiter. Exiting."
    process.exit 1
  readFileAsync filename, printTree, delimiter