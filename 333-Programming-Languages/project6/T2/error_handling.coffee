"""
Authors: Sarah Harmon, Peter Graham
Description: Project 6, Task 2
"""

# CoffeeScript does exception/error handling through try-catch-finally blocks.
# This is built into the language. Since CoffeeScript does not support file I/O,
# a way to report an error may be to send an AJAX GET/POST request with basic
# information on the error to a script on the server-side that will log the
# error message to a file on the server. Another method would be to show some
# form of an alert/error dialog to the client.
try
  a = b * c
catch e
  # The error object has some information on the type of error, stacktrace, etc
  console.log "ERROR:\n
Type: #{e.type}\n
Args: #{e.arguments}\n
Message: #{e.message}"
  console.log "\nSTACKTRACE:\n", e.stack
finally
  console.log "FINALLY, DO THIS"
  
# Example of throwing an error:
try
  throw new Error
catch e
  console.log "Caught the error that was thrown manually"
