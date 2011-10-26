"""
Authors: Sarah Harmon, Peter Graham
Description: Project 4, Task 2

Demonstrates the rules for identifier naming, variable declarations, and 
identifier scoping. Focuses on special rules or capabilities of CoffeeScript.
"""

# Identifiers start with letter, underscore, or dollar sign.
# Subsequent characters can be letter, underscore, dollar sign, and digit.
a = 0
A = 1
$ = 2
_ = 3
_$_123_$_ = 4

# Compiled JavaScript will have variable declarations at the top of the closest
# scope. For example, the following code will insert "var b, c;" at the top of
# the function. However, since variable a is already declared in the parent
# scope (see above), compiled JavaScript will have "var a;" at the top of this
# file instead.
a_func = () ->
  a = 1
  b = 2
  c = 3
  
# TODO: this keyword and how "=>" fat arrow fixes dynamic scoping issue

# The compiled JavaScript is wrapped in an anonymous function, so all code in
# this file will be wrapped with a (function(){ ... }).call(this);. Thus, to
# create variables of global scope you can use the "exports" object for CommonJS
# or the "window" object for web browsers.
exports.a = 123
