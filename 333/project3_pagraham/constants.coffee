Token = require('./token').Token

# Constants
Tokens =
  Keywords: [new Token("Keyword", "int"), new Token("Keyword", "float")]
  OpsAdd: [new Token("Operator", "+"), new Token("Operator", "-")]
  OpsMultiply: [new Token("Operator", "*"), new Token("Operator", "/")]
  OpsEquality: [new Token("Operator", "==")]
  Literals: [new Token("Float"), new Token("Integer")]
  END: "END"

Tokens.Binary = Tokens.OpsAdd.concat(Tokens.OpsMultiply).concat Tokens.OpsEquality

exports.Tokens = Tokens
