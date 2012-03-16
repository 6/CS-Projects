import hashlib, re, urllib
import settings, models, config
from flask import url_for

# GAE_specific
from google.appengine.api import mail

def hash(string):
    '''
    Returns the sha224 hexdigest (twice) of a string + secret salt
    '''
    return hashlib.sha224(config.hashSalts[0]+\
                              hashlib.sha224(string).hexdigest()+\
                              config.hashSalts[1]).hexdigest()

def validate(value, validValues, default=False):
    '''
    validValues is a list of valid values. Returns value if value
    is in validValues, otherwise returns default.
    '''
    return value if value in validValues else default

def isValidEmail(string):
    '''
    Returns true if valid email, false if not.
    '''
    pattern = "[^@]+@\w+[-\.\w]+"
    return len(string) <= 100 and re.match(pattern, string)

def isValidPassword(string):
    return 100 >= len(string) >= 6

def isValidUsername(string):
    if len(string) > 20:
        return False
    pattern = "[a-z0-9]+"
    return re.match(pattern, string, re.IGNORECASE)

def makeRelativeUrl(string):
    return string if string.startswith("/") else url_for('index')

def loginUser(username, password):
    '''
    Returns case-sensitive username if valid login information
    or false if invalid login information. TODO: better returns.
    '''
    # validate without DB
    if not username or not password:
        return False
    if not isValidUsername(username) or not isValidPassword(password):
        return False
    
    # validate using DB 
    userCased = models.validateLogin(username.lower(), hash(password))
    return userCased
    
def registerUser(username, email, password):
    '''
    Returns case-sensitive username if valid registration info 
    or false if invalid registration info. TODO: better returns
    '''
    if not username or not email or not password:
        return 1
    if not isValidUsername(username) or not isValidEmail(email) or \
            not  isValidPassword(password):
        return 2
    
    # check if username is reserved
    if username.lower() in config.reservedPages:
        return 3

    # check if username already exists in DB
    existingUsername = models.getUserCased(username.lower())
    if existingUsername:
        return 3

    # username doesn't exist, so register new user
    user = models.User(username=username.lower(), usercased=username, 
                       email=email, password=hash(password))
    user.put()
    return username

def sendResetEmail(email, key):
    '''
    Sends an email to given address with link to reset password.
    '''
    url = "http://"+config.url+url_for('resetPassword2')+"?e="+urllib.quote(email)+"&k="+key
    subject = "Reset your "+config.name+" password"
    body = """Reset your password by clicking the following link:
    
%s
    """ % url
    mail.send_mail(config.senderAddress, email, subject, body)

def makeSession(session, userId):
    session.regenerate_id()
    session['userId'] = userId

def getInt(string):
    '''
    Returns integer value of string or False if not int.
    '''
    if string is not None and string.isdigit():
        try:
            i = int(string)
            return i
        except:
            return False
    return False
