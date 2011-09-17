###
Peter Graham CS333

Generic CoffeeScript environment for showing CoffeeScript output in a
terminal-like view.
###
load_template = ->
  template_fn = haml.compileHaml('coffee-html-template')
  $("body").append template_fn({})
  
load_css = (fname) -> $("head").append ich.stylesheet(href: fname)
  
set_title = (title) ->
  $("title").text title

$ ->
  load_template()
  load_css "normalize.css"
  set_title "Peter Graham CS333 - Homework 3"