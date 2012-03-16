from urlparse import urlparse
from flask import g, redirect, request, url_for
from functools import wraps

def login_required(f):
    '''
    Redirect user to login page if they aren't logged in.
    '''
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if g.userId is None:
            u = urlparse(request.url)
            # prevent redirection to some other site -> rel url
            #TODO: prevent some like "profile/deactivate"
            nextUrl = u[2]+u[3]+u[4]+u[5]
            #TODO: "you must login to access"
            return redirect(url_for('login', n=nextUrl))
        return f(*args, **kwargs)
    return decorated_function

def no_login(f):
    '''
    Redirect user to index if they're already logged in.
    '''
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if g.userId is not None:
            return redirect(url_for('index'))
        return f(*args, **kwargs)
    return decorated_function
