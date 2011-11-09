# Writing to the terminal is accomplished using builtin console.log method.
console.log "Hello world!"

# CoffeeScript does not have File I/O built in, and it is not part of a standard
# set of libraries either. However, it does support text I/O through the "alert"
# and "prompt" dialogs of browsers.
# Note: the below portion of code must be executed in a browsers to work
alert("This will display an alert dialog box when executed by a browser")
input = prompt("Please enter some text:")

# You can also write directly to the HTML of the page:
document.getElementsByTagName("body")[0].innerHTML = "replace all text w/ this"
