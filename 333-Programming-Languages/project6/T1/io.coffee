# CoffeeScript does not have File I/O built in, and it is not part of a standard
# set of libraries either. However, it does support text I/O through the "alert"
# and "prompt" dialogs of browsers.
alert("This will display an alert dialog box when executed by a browser")
input = prompt("Please enter some text:")

# You can also write directly to the HTML of the page:
document.getElementsByTagName("body")[0].innerHTML = "replace all text w/ this"
