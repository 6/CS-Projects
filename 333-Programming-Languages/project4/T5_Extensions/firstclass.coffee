"""
Authors: Sarah Harmon, Peter Graham
Description: Project 4, Extension

Demonstrates that functions are first-class variables in CoffeeScript.
"""

slapper = (person, slap_fn) -> slap_fn person

slap = (person) -> console.log "Slap #{person}!!"

slapper "Joe", slap
