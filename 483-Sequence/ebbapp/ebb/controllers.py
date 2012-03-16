# -*- coding: utf-8 -*-
import time
from ebb import app
from flask import flash, g, get_flashed_messages, redirect, render_template, request, url_for
from werkzeug.wrappers import Response
from decorators import login_required, no_login
import config, helpers, models, settings, admin
from gaesessions import get_current_session

@app.before_request
def before_request():
    '''
    Initialize user session before each request.
    '''
    g.userId = None
    # check if has session cookie
    g.session = get_current_session()
    if g.session.is_active():
        g.userId = g.session.get('userId', None)

@app.route('/')
@app.route('/1')
@app.route('/2')
def index():
    '''
    Displays landing page if user not logged in, or featured/recommended
    content if they are logged in.
    '''
    if g.userId is not None:
        # redirect to their profile if logged in
        return redirect(url_for('profileOf',user=g.userId))
        #return render_template('home.html', userId=g.userId)
        
    demoIdx = request.environ['PATH_INFO'][1:]
    if len(demoIdx) > 0:
        demoIdx = int(demoIdx)
    else:
        demoIdx = 0
    sets = ['agZlYmJhcHByCwsSA1NldBjRjAEM','agZlYmJhcHByCwsSA1NldBjRjAEM','agZlYmJhcHByCwsSA1NldBjRjAEM']
    #studyset = models.getSetById(sets[demoIdx])
    #terms = models.getStudyData(studyset)
    studyset = []
    terms = []
    return render_template('landing.html', theset=studyset, terms=terms)

@app.route('/<user>')
def profileOf(user):
    '''
    Shows public profile of user.
    '''
    # convert to string if username only contains integers
    user = str(user)
    # get the sets they've created
    sets = models.getAllNormalSets(user.lower())
    imgsets = models.getAllTagSets(user.lower())
    return render_template('profile.html', user=user,sets=sets,imgsets=imgsets)

@app.route('/browse')
@app.route('/browse/<page>')
def browse(page=0):
    '''
    Browse through most recently submitted sets.
    '''
    results = models.getMostRecent(0, 10)
    return render_template('browse.html', results=results)

@app.route('/search')
def search():
    '''
    Displays set search results for a given query. 
    Note: search is difficult on app engine, due to lack of "LIKE" operator
    in GQL queries.
    '''
    query = request.args.get('q',False)
    results = models.search(query,10)
    return render_template('browse.html', searchquery=query, results=results)

@app.route('/help')
def help():
    return render_template('help.html')

@app.route('/about')
@app.route('/about/<subpage>')
def about(subpage=None):
    if subpage == "press":
        subpage = 1
    elif subpage == "contact":
        subpage = 2
    else:
        subpage = 0
    return render_template('about.html', subpage=subpage)

@app.route('/terms')
def terms():
    return render_template('terms.html')

@app.route('/privacy')
def privacy():
    return "privacy"

@app.route('/login')
@no_login
def login():
    '''
    Display a login form and show validation errors as necessary.
    If they were trying to access a URL requiring login, remember this URL, so
    we can redirect to it after successful login.
    '''
    next = request.args.get('n',u'')
    username = request.args.get('u',u'')
    error = helpers.getInt(request.args.get('r'))

    # determine whether or not to override focus to password
    f = helpers.validate(request.args.get('f'), ['username','password'])
    if not f:
        f = False if (username is u'') else 'password'

    return render_template('login.html', links=False, next=next, u=username,
                           oFocus=f, err=error)

@app.route('/authlogin', methods=['POST'])
@no_login
def auth_login():
    '''
    Validate username and password and go to next url.
    If invalid, go back to login form with error(s).
    '''
    username = request.form.get('username', False)
    password = request.form.get('password', False)
    next = request.form.get('next', False)

    # validate entered email and password
    userCased = helpers.loginUser(username, password)
    if userCased:
        # create session and redirect to next URL
        next = helpers.makeRelativeUrl(next)
        helpers.makeSession(g.session, userCased)
        return redirect(next)

    # invalid, so show errors
    return redirect(url_for('login', u=username, n=next, r=1))

@app.route('/register')
@no_login
def register():
    '''
    Display a registration form.
    '''
    next = request.args.get('n',u'')
    username = request.args.get('u',u'')
    email = request.args.get('e',u'')
    error = helpers.getInt(request.args.get('r'))

    # determine whether or not to override focus to password
    f = False
    if username is not u'':
        if email is u'':
            f = 'email'
        else:
            f = 'password'

    return render_template('register.html', links=False, next=next, u=username,
                           e=email, oFocus=f, err=error)

@app.route('/authregister', methods=['POST'])
@no_login
def auth_register():
    '''
    Validate registration information.
    '''
    username = request.form.get('username', False)
    email = request.form.get('email', False)
    password = request.form.get('password', False)
    next = request.form.get('next', False)
    
    userCased = helpers.registerUser(username, email, password)
    if userCased > 3:
        # no errors - create session and redirect to next URL
        next = helpers.makeRelativeUrl(next)
        helpers.makeSession(g.session, userCased)
        return redirect(next)

    # invalid, so show errors
    return redirect(url_for('register', u=username, e=email, n=next, r=userCased))

@app.route('/resetpassword')
@no_login
def resetPassword():
    '''
    Displays email form, or if already submitted, displays success message.
    Note: this is temporarily disabled, as sending emails is limited in app
    engine.
    '''
    # check if successfully sent email
    succ = request.args.get('succ', False)
    if succ:
        return render_template('reset.html', succ=True)
    email = request.args.get('email', False)
    err = request.args.get('r',False)
    return render_template('reset.html', e=email, err=err)

@app.route('/authreset', methods=['POST'])
@no_login
def auth_reset():
    '''
    If e-mail is valid and in DB, sets a reset key in DB for user and sends a
    reset password link to the user's e-mail.
    '''
    err = False
    
    # validate email
    email = request.form.get('e',None)
    if email is not None and helpers.isValidEmail(email):
        if models.emailExists(email):
            key = models.setResetKey(email)
            helpers.sendResetEmail(email, key)
        else:
            err = 1
    else:
        err = 1
    
    if err:
        return redirect(url_for('resetPassword', email=email, r=err))
    return redirect(url_for('resetPassword', succ=1))

@app.route('/reset')
@no_login
def resetPassword2():
    '''
    Allows user to enter their new password as long as the key is valid.
    '''
    # check if password has been successfully reset
    succ = request.args.get('succ', False)
    if succ:
        return render_template('reset2.html', succ=True)

    email = request.args.get('e', False)
    key = request.args.get('k', False)
    err = request.args.get('r', False)
    if email and key and helpers.isValidEmail(email):
        if models.validReset(str(email), str(key)):
            return render_template('reset2.html', email=email, key=key, err=err)
    return "Invalid reset password URL.<br>"+\
        "Please verify that you copied and pasted the whole URL."

@app.route('/authreset2', methods=['POST'])
@no_login
def auth_reset2():
    '''
    Make sure that password is valid for password reset.
    '''
    email = request.form.get('email', False)
    key = request.form.get('key', False)
    newPassword = request.form.get('password', False)
    if email and newPassword and helpers.isValidPassword(newPassword):
        models.changePassword(email, helpers.hash(newPassword))
        return redirect(url_for('resetPassword2', succ=1))
    return redirect(url_for('resetPassword2', e=email, k=key, r=1))

# LOGGED IN
@app.route('/logout')
def logout():
    '''
    Logs out the current user by destroying their session cookie.
    '''
    if g.session.has_key('userId'):
        g.session.terminate()
    return redirect(url_for('index'))

@app.route('/settings')
@login_required
def settings():
    # Generic password change page for reset also (separate page for "Change password") 
    return render_template('settings.html')
    
@app.route('/new')
@login_required
def newSet():
    '''
    Allow users to create a new "Terms and Definitions" set.
    '''
    title = request.args.get('title', False)
    prompts = request.args.getlist('prompts')
    answers = request.args.getlist('answers')
    deftypes = request.args.getlist('deftypes')
    isPrivate = False
    if request.args.get('privacy') == 'private':
        isPrivate = True
    return render_template('newset.html', settitle=title, private=isPrivate,prompts=prompts,deftypes=deftypes,
        answers=answers,flashes=get_flashed_messages())
        
@app.route('/edit/<setId>')
def editSet(setId=None):
    '''
    Display an edit form for a "Terms and Definitions" set.
    '''
    theset = models.getSetById(setId)
    terms,definitions,termtypes,definitiontypes = models.getSetData(theset)
    return render_template('newset.html', isEdit=True, setId=setId, settitle=theset.title, prompts=terms,
        deftypes=definitiontypes, answers=definitions,private=theset.isprivate, flashes=get_flashed_messages())

@app.route('/newtag')
@login_required
def newTagSet():
    '''
    Create a new "Ta"
    '''
    title = request.args.get('title', False)
    return render_template('newtagset.html', settitle=title, flashes=get_flashed_messages())
    
@app.route('/submittagset', methods=['POST'])
def submitTagSet():
    # validate title
    title = request.form.get('title')
    image = request.files.get('image')
    errors = False
    if not title.strip() or not image:
        errors = True
        flash("Title and image are required.")
    
    if errors:
        return redirect(url_for('newTagSet',title=title))
    
    isPrivate = False
    if request.form.get('privacy') == 'private':
        isPrivate = True
    
    # insert into database and redirect to new set page
    insertedSet = models.newTagSet(g.userId.lower(),title,image, isPrivate)
    return redirect(url_for('tagImage',setId=insertedSet.key(), play='edit'))

@app.route('/image/<setId>')
@app.route('/image/<setId>/<play>')
def tagImage(setId=None, play=None):
    theSet = models.getTagSet(setId)
    width,height = models.getImageDims(setId)
    tags = models.getImageTags(setId)
    isCreator = False
    if g.userId != None:
        creator = models.getCreator(setId).username
        isCreator = creator.lower() == g.userId.lower()
    isGame,isEdit = False,False
    if play == 'edit':
        isEdit = True
    elif play:
        isGame = True
    isFav = False 
    if g.userId:
        isFav = models.favExists(g.userId.lower(), setId) #TODO BREAKS WHEN NOT LOGGED IN
    return render_template('tagimage.html', theset=theSet, setId=setId,imgwidth=width,
        imgheight=height, tags=tags, isCreator=isCreator,isGame=isGame, isEditable=True, isEdit=isEdit,
        isFavable=True,isFav=isFav, flashes=get_flashed_messages())
        
@app.route('/modtagtitle',methods=['POST'])     
def modifyTagTitle():
    title = request.form.get('title')
    setId = request.form.get('setid')
    if not title.strip():
        flash("Please enter a title.")
    else:
        models.changeTagTitle(title,setId,g.userId.lower())
    return redirect(url_for('tagImage',setId=setId, play="edit"))

@app.route('/ajax_tag')
def ajaxAddTag():
    #TODO confirm user
    setid = request.args.get('setid')
    xpos = request.args.get('xpos')
    ypos = request.args.get('ypos')
    term = request.args.get('term')
    tag = models.addTag(setid, xpos, ypos, term)
    return str(tag.key())
    
@app.route('/ajax_remove_tag')
def ajaxRemoveTag():
    
    #TODO confirm user
    tagId = request.args.get('tagid')
    models.removeById(tagId)
    return ""

@app.route('/submitset', methods=['POST'])
def submitSet():
    '''
    Validate and save set.
    '''
    # validate title
    title = request.form.get('title')
    errors = False
    if not title.strip():
        errors = True
        flash("A title is required.")
        
    # validate prompts and answers
    prompts = request.form.getlist('prompt')
    deftypes = request.form.getlist('deftype')
    
    # make answers array
    answerstext = request.form.getlist('answer')
    answersfiles = request.files.getlist('answer')
    answers = []
    #return str(answerstext) + str(answersfiles)
    # combine answer files and answer texts in order
    for i in range(len(prompts)):
        if deftypes[i] == 'img':
            if len(answersfiles) > 0:
                answers.append(answersfiles.pop(0))
            else:
                answers.append()
        else:
            answers.append(answerstext.pop(0))
    
    actualLength = 0
    for i in range(len(prompts)):
        answer = answers[i]
        if deftypes[i] != 'text':
            answer = answer.filename
        if not prompts[i].strip() and not answer.strip():
            # both are empty, ignore
            continue
        if not prompts[i].strip() or not answer.strip():
            errors = True
            flash("Terms and definitions must not be empty.")
            break
        actualLength += 1
        
    if not errors and actualLength < 2:
        errors = True
        flash("You must define at least two terms.")
    
    # privacy
    isPrivate = False
    if request.form.get('privacy') == 'private':
        isPrivate = True

    if errors:
        #TODO also privacy
        return redirect(url_for('newSet',title=title,prompts=prompts,deftypes=deftypes,answers=answers,
            privacy=request.form.get('privacy')))
    
    isEdit=False
    if request.form.get('isedit') == 'yes':
        isEdit = request.form.get('isedit')
    
    # insert into database and redirect to new set page
    insertedSet = models.newOrUpdateSet(g.userId.lower(),title,prompts,answers, deftypes, isPrivate, isEdit)
    return redirect(url_for('reviewSet',reviewid=insertedSet.key()))

@app.route('/review/<reviewid>')
def reviewSet(reviewid=None):
    studyset = models.getSetById(reviewid)
    if not studyset:
        return "404 set not found"
    #terms,defs,termtypes,deftypes = models.getSetData(studyset)
    terms = models.getStudyData(studyset)
    isCreator = False
    isFav = False
    if g.userId != None:
        creator = models.getCreator(reviewid).username
        isCreator = creator.lower() == g.userId.lower()
        isFav = models.favExists(g.userId.lower(), reviewid)
    # TODO: ESCAPE QUOTES
    # ,defs=defs,termtypes=termtypes, deftypes=deftypes,
    return render_template('reviewset.html',settitle=studyset.title,
        private=studyset.isprivate,terms=terms, setId=reviewid,
        isEditable=True, isCreator=isCreator,isFavable=True,isFav=isFav)

@app.route('/new/choose')
def setChooser():
    return render_template('setchoose.html')

# for normal sets
@app.route('/img/<studyDataId>')
def serveImage(studyDataId=None):
    # GET studydata --> answerblob --> serve it
    image = models.getImageByStudyDataId(studyDataId)
    response = Response(image)
    return response

# for image tag
@app.route('/img2/<tagSetId>')
def serveImage2(tagSetId=None):
    image = models.getImageBySetId(tagSetId)
    response = Response(image)
    return response
    
@app.route('/game/<setId>/<gameType>')
def normalGame(setId, gameType):
    studyset = models.getSetById(setId)
    if not studyset:
        return "404 set not found"
    #terms,defs,termtypes,deftypes = models.getSetData(studyset)
    terms = models.getStudyData(studyset)
    # TODO: ESCAPE QUOTES
    # defs=defs,termtypes=termtypes,deftypes=deftypes
    return render_template('playnormal.html',set=studyset,terms=terms,
        setId=setId,type=gameType,isEditable=True)

@app.route('/fav/<setId>')
@login_required
def addOrRemoveFav(setId=None):
    '''
    Add or remove a given set as a favorite.
    '''
    if(models.favExists(g.userId.lower(), setId)):
        models.removeFav(g.userId.lower(),setId)
    else:
        models.addFav(g.userId.lower(),setId)
    #redir back to set
    theset = models.getSetById(setId)
    if theset.settype == "tag":
        return redirect(url_for('tagImage',setId=setId))
    return redirect(url_for('reviewSet',reviewid=setId))
    
@app.route('/stats/<setId>')
def viewStats(setId=None):
    '''
    View stats for a given set.
    '''
    theset = models.getSetById(setId)
    terms = []
    if theset.settype == "tag":
        terms = models.getImageTags(setId)
    else:
        terms = models.getStudyData(theset)
    stats = []
    for term in terms:
        overallPercent, num = models.getStatsFor(theset, term)
        stats.append([term, int(round(overallPercent)), overallPercent*6, num])
    mean, distr = models.getOverallStats(theset)
    return render_template('stats.html', stats=stats,theset=theset, mean=int(round(mean)), distr=distr)

@app.route('/ajax_addstat/<setId>')
@login_required
def addStat(setId=None):
    '''
    Called by an AJAX request whenever user answers a question in a game.
    '''
    termId = request.args.get('term')
    user = models.getUserModel(g.userId.lower())
    isCorrect = request.args.get('correct') == 'yes'
    models.addStat(setId, termId, user, isCorrect)
    return "ok" # have to return something
