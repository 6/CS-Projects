class Token
  attr_accessor :type, :val

  # type and value are both strings
  def initialize(type, val=nil)
    @type = type
    @val = val
  end

  def to_s
    @val.nil? ? "#{@type}" : "#{@type}:#{@val}"
  end

  def equals?(token)
    equal = token.type == @type
    equal = equal && (token.val == @val) unless token.val.nil? or @val.nil?
    equal
  end
end

class Lexer
  def initialize(token_file)
  end

  def next_token
  end

  def current_token
  end
end

class Parser
  def initialize(lexer)
    @lexer = lexer
  end

  def match(token)
  end

  def error(token)
  end

  def any_of?(tokens)
  end
end
