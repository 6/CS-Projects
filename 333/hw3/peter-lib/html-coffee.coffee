###
Peter Graham CS333

Generic CoffeeScript environment for showing CoffeeScript output in a
terminal-like view.
###
load_template = ->
  template_fn = haml.compileHaml('coffee-html-template')
  $("body").append template_fn({})

set_title = (title) ->
  $("title").text title

$ ->
  load_template()
  set_title "Peter Graham CS333 - Homework 3"