###
Peter Graham CS333

Generic CoffeeScript environment for showing CoffeeScript output in a
terminal-like view.
###

# output to the right-side output window
window.p = ->
	args = Array.prototype.slice.call(arguments)
	$("#output").append "#{args.join(", ")}<br>"

run_code = ->
  try
    compiled_js = CoffeeScript.compile $("#code").val(), bare: on
    $("#output").text ""
    eval compiled_js
  catch error
    has_error = true
    $("#output").text error.message
    $("#output").addClass "error"
  $("#output").removeClass "error" unless has_error

load_template = ->
  template_fn = haml.compileHaml('coffee-html-template')
  $("body").append template_fn({})

set_title = (title) ->
  $("title").text title
  $("#title").text title

$ ->
  load_template()
  set_title "CS333 - Homework 3"
  $("#code").val ""
  $.fn.tabOverride.setTabSize 2
  $("#code").tabOverride()
  $("#code").focus()
  $("#code").keydown (e) -> run_code() if e.which == 13 and e.metaKey
  $("#run-code-button").click run_code
