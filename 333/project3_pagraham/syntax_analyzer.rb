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

t = Token.new "what"
t.val = "hoo"
t2 = Token.new "what", "foo"
puts t
puts t2
puts t.equals?(t2)
