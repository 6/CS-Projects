# -*- coding: utf-8 -*-
"""
    flaskext.csrf
    ~~~~~~~~~~~~~

    A small Flask extension for adding CSRF protection.

    :copyright: (c) 2010 by Steve Losh.
    :license: MIT, see LICENSE for more details.
"""
from uuid import uuid4
from flask import g, request#, session
from werkzeug.routing import NotFound

# GAE_specific
from gaesessions import get_current_session

_exempt_views = []

def csrf_exempt(view):
    _exempt_views.append(view)
    return view

def csrf(app):
    @app.before_request
    def _csrf_check_exemptions():
        try:
            dest = app.view_functions.get(request.endpoint)
            g._csrf_exempt = dest in _exempt_views
        except NotFound:
            g._csrf_exempt = False
    
    @app.before_request
    def _csrf_protect():
        if not g._csrf_exempt:
            if request.method == "POST":
                g.session = get_current_session()
                if g.session.is_active():
                    token = g.session.pop('_csrf_token', None)
                    if not token or token != request.form.get('tok'):
                        #TODO ...or header referrer does not match expected
                        # Flask_specific:
                        #return abort(400)
                        # GAE_specific:
                        return "Bad request (CSRF?)", 400

    def generate_csrf_token():
        # GAE_specific
        g.session = get_current_session()
        if '_csrf_token' not in g.session:
            g.session['_csrf_token'] = str(uuid4())
        return g.session['_csrf_token']
    
    app.jinja_env.globals['csrf_token'] = generate_csrf_token
