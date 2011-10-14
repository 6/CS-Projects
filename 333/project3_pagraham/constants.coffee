Token = require('./token').Token

# Constants
Tokens =
  Types: [new Token("Keyword", "int"), new Token("Keyword", "float"), new Token("Keyword", "bool")]
  Unary: [new Token("Operator", "-")]
  OpsAdd: [new Token("Operator", "+"), new Token("Operator", "-")]
  OpsMultiply: [new Token("Operator", "*"), new Token("Operator", "/"), new Token("Operator", "%")]
  OpsEquality: [new Token("Operator", "=="), new Token("Operator", "!=")]
  Literals: [new Token("Float"), new Token("Integer"), new Token("Boolean")]
  END: "END"

Tokens.Binary = Tokens.OpsAdd.concat(Tokens.OpsMultiply).concat Tokens.OpsEquality

exports.Tokens = Tokens
