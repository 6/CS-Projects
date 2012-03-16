from flask import Flask
from gaesessions import SessionMiddleware

import settings

# this is called from ../runapp.py
app = Flask('ebb')
# configure from settings.py 
app.config.from_object('ebb.settings')

# make forms CSRF-safe (note - must change templates to include CSRF field)
from flaskext.csrf import csrf
csrf(app)

import controllers
