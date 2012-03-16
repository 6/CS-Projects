from uuid import uuid4
from google.appengine.ext import db
from google.appengine.api import images
import helpers
import datetime

class User(db.Model):
    '''
    Stores user registration/login info.
    Use the username as a unique key for other tables.
    '''
    username = db.StringProperty(required = True)
    usercased = db.StringProperty(required = True)
    email = db.StringProperty(required = True)
    password = db.StringProperty(required = True)
    dateregistered = db.DateTimeProperty(auto_now_add = True)
    resetkey = db.StringProperty()

def getUserCased(usernameLower):
    '''
    Returns the case-sensitive version of the username, given the lowercase.
    Returns False if no such lowercase username exists.
    '''
    query = User.gql("WHERE username = :username", username=usernameLower)
    return False if query.count() == 0 else str(query.get().usercased)

def validateLogin(usernameLower, hashPass):
    '''
    Returns the case-sensitive version of the username, given the lowercase
    username and the hashed password. Returns False if invalid login.
    '''
    query = User.gql("WHERE username = :username AND password = :password",
                     username=usernameLower, password=hashPass)
    return False if query.count() == 0 else str(query.get().usercased)

def emailExists(email):
    '''
    Returns True if it exists in database. Returns False if it doesn't.
    '''
    query = User.gql("WHERE email = :email", email=email)
    return False if query.count() == 0 else True

def setResetKey(email, keyValue=None):
    '''
    Sets the value of the reset password key to a given value. Returns key.
    '''
    if keyValue is None:
        keyValue = str(uuid4())
    query = User.gql("WHERE email = :email", email=email)
    if query.count() > 0:
        user = query.get()
        user.resetkey = keyValue
        user.put()
    return keyValue

def validReset(email, key):
    '''
    Checks if given e-mail and reset key are valid.
    '''
    query = User.gql("WHERE email = :email AND resetkey = :resetkey",
                     email=email, resetkey=key)
    return False if query.count() == 0 else True

def changePassword(email, newPassHash):
    '''
    Assigns a new password to the account associated with the given email.
    '''
    query = User.gql("WHERE email = :email", email=email)
    if query.count() > 0:
        user = query.get()
        user.password = newPassHash
        user.put()
        
def getUserId(usernameLower):
    '''
    Returns the user ID, or false if no user found.
    '''
    user = getUserModel(usernameLower)
    if not user:
        return False
    return user.key()
    
def getUserModel(usernameLower):
    query = User.gql("WHERE username = :username", username=usernameLower)
    if query.count() == 0:
        return False
    return query.get()
        
#################

class Set(db.Model):
    user = db.ReferenceProperty(User, required=True)
    title = db.StringProperty(required = True) # title of study material
    isprivate = db.BooleanProperty(default=False)
    settype = db.StringProperty(required = True) # for now, normal or tag
    datecreated = db.DateTimeProperty(auto_now_add = True) # date created
    
    def __str__(self):
        return self.title
        
    def getNiceDate(self):
        return datetime.datetime.strftime(self.datecreated, '%m/%d/%Y')

def getMostRecent(page=0, howMany=10):
    offset = page*howMany
    query = db.Query(Set)
    return query.order('-datecreated').filter("isprivate = ",False).fetch(howMany, offset)

def search(term,howMany=10):
    query = Set.gql("WHERE title >= :1 AND title < :2", term, u''+term + u"\ufffd")
    return query.fetch(howMany)

def removeById(theid):
    db.delete(theid)
    
def getSetById(setId):
    studyset = db.get(setId)
    return studyset
    
def getCreator(setId):
    return db.get(setId).user

################################################################################    
    
class StudyData(db.Model):
    '''
    Stores the prompts and answers of the study material.
    '''
    studyset = db.ReferenceProperty(Set, required=True)
    
    # image,text, or sound (sound/image --> CDN due to 1MB limit)
    prompttype = db.StringProperty(required = True)
    answertype = db.StringProperty(required = True)
    
    # TextData or BlobData FIX ASAP THIS IS UGLY
    promptblob = db.BlobProperty() # 1 MB limit?
    answerblob = db.BlobProperty() # 1 MB limit?
    prompttext = db.StringProperty()
    answertext = db.StringProperty()
    
    def __str__(self):
        if self.prompttype == 'text':
            return self.prompttext
        return "blob (change this)"
    
def newOrUpdateSet(user, title, prompts, answers, deftypes, isPrivate, isEdit):
    '''
    Create a new study set with the given title, prompts and answers.
    '''
    usr = getUserModel(user)
    studySet = None
    if isEdit:
        studySet = getSetById(isEdit)
        # delete all current text things
        
    else:
        studySet = Set(user=usr, title=title, isprivate=isPrivate, settype="normal")
        studySet.put()
    
    for i in range(len(prompts)):
        prompt,answer,deftype = prompts[i],answers[i],deftypes[i]
        # change "img" to "blob"
        if deftype != 'text':
            deftype = 'blob'
        # check if text is empty
        if deftype != 'blob' and (not prompt.strip() or not answer.strip()):
            continue 
        #TODO FIX CREATEs row if both term + def empty
        studyData = StudyData(studyset=studySet, prompttype='text', answertype=deftype, prompttext=prompt)
        if deftype == 'text':
            studyData.answertext = answer
        else:
            # resize the image to 550x350 maximum while we're at it
            studyData.answerblob = images.resize(answer.read(), 550, 300)
        studyData.put()
    return studySet

def getUserSets(usernameLower, settype):
    '''
    Returns the user's sets.
    Returns False if no sets created.
    '''
    usr = getUserModel(usernameLower)
    if not usr:
        return False
    query = Set.gql("WHERE user = :user AND settype = :settype ORDER BY title", user=usr, settype=settype)
    if query.count() == 0:
        return False
    return query.fetch(100) # TODO: limit
    
def getAllNormalSets(usernameLower):
    created = getUserSets(usernameLower,"normal")
    favs = getUserFavs(usernameLower,"normal")
    if not created:
        created = []
    if not favs:
        favs = []
    return sorted(created+favs, key=lambda theset: theset.title.lower())
    
def getAllTagSets(usernameLower):
    created = getUserSets(usernameLower,"tag")
    favs = getUserFavs(usernameLower,"tag")
    if not created:
        created = []
    if not favs:
        favs = []
    return sorted(created+favs, key=lambda theset: theset.title.lower())
    
def getSetData(studySet):
    '''
    Returns tuple: (terms[], definitions[])
    '''
    if not studySet:
        return False
    query = StudyData.gql("WHERE studyset = :studyset", studyset=studySet)

    terms,definitions,termtypes,definitiontypes = [],[],[],[]
    for data in query:
        termtypes.append(data.prompttype)
        definitiontypes.append(data.answertype)
        if data.prompttype == 'blob':
            #TODO sound vs image
            terms.append(data.promptblob)
        else:
            # text
            terms.append(data.prompttext)
        if data.answertype == 'blob':
			# append the key (to access it later)
            definitions.append(data.key())
        else:
            #text
            definitions.append(data.answertext)
            
    return terms,definitions,termtypes,definitiontypes

def getStudyData(studySet):
    query = StudyData.gql("WHERE studyset = :studyset", studyset=studySet)
    if query.count() == 0:
        return False
    return query.fetch(100)

def getImageByStudyDataId(studyDataId):
	studySet = db.get(studyDataId)
	return studySet.answerblob
	
	
#############################################

class TagImage(db.Model):
    tagset = db.ReferenceProperty(Set, required=True)
    image = db.BlobProperty(required=True)
	
class Tag(db.Model):
    tagset = db.ReferenceProperty(Set, required=True)
    term = db.StringProperty(required = True)
    xpos = db.IntegerProperty(required = True)
    ypos = db.IntegerProperty(required = True)
    
    def __str__(self):
        return self.term
    
def newTagSet(user, title, image,isPrivate):
    tagset = Set(user=getUserModel(user), title=title, settype="tag")
    tagset.isprivate = isPrivate
    tagset.put()
    # resize the image to max 800x450
    tagimg = TagImage(tagset=tagset, image=images.resize(image.read(),900,400))
    tagimg.put()
    return tagset

def getTagSet(setId):
	tagSet = db.get(setId)
	return tagSet
	
def getImageBySetId(setId):
    tagSet=getTagSet(setId)
    query= TagImage.gql("WHERE tagset = :tagset", tagset=tagSet)
    return query.get().image

def addTag(setId, xpos, ypos, term):
    if not term.strip():
        return
    tagset = getTagSet(setId)
    tag = Tag(tagset=tagset, term=term, xpos = int(xpos),ypos = int(ypos))
    tag.put()
    return tag
    
def getImageDims(setId):
    pic = images.Image(image_data=getImageBySetId(setId))
    return pic.width, pic.height
    
def getImageTags(setId):
    tagset = getTagSet(setId)
    query = Tag.gql("WHERE tagset = :tagset", tagset=tagset)
    if query.count() == 0:
        return False
    return query.fetch(100)
    
def getUserImageSets(usernameLower):
    usr = getUserModel(usernameLower)
    if not usr:
        return False
    query = Set.gql("WHERE user = :user AND settype=:settype", user=usr, settype="tag")
    if query.count() == 0:
        return False
    return query.fetch(10)
    
def recentTag(page=0, howMany=10):
    offset = page*howMany
    query = db.Query(TagSet)
    return query.order('-datecreated').filter("isprivate = ",False).fetch(howMany, offset)
    
def changeTagTitle(title, setId, usernameLower):
    theset = getSetById(setId)
    if theset.user.username != usernameLower:
        return False
    theset.title = title
    theset.put()
    
    
############################

class Favorites(db.Model):
    user = db.ReferenceProperty(User, required=True)
    theset = db.ReferenceProperty(Set, required=True)

def favExists(usernameLower, setId):
    usr = getUserModel(usernameLower)
    st = db.get(setId)
    favq = Favorites.gql("WHERE user = :user AND theset = :theset", user=usr,theset=st)
    return favq.count() > 0

def addFav(usernameLower, setId):
    usr = getUserModel(usernameLower)
    st = db.get(setId)
    fav = Favorites(user=usr,theset=st)
    fav.put()
    
def removeFav(usernameLower, setId):
    usr = getUserModel(usernameLower)
    st = db.get(setId)
    query = db.Query(Favorites)
    fav = Favorites.gql("WHERE user = :user AND theset = :theset", user=usr,theset=st)
    thefav = fav.get()
    removeById(thefav.key())
    
def getUserFavs(usernameLower, settype):
    usr = getUserModel(usernameLower)
    query = Favorites.gql("WHERE user = :user", user=usr)
    if query.count() == 0:
        return False
    favs = query.fetch(100)
    sets = []
    for fav in favs:
        if fav.theset.settype == settype:
            sets.append(fav.theset)
    return sets
    
    
##################

class Stat(db.Model):
    # the user to track
    user = db.ReferenceProperty(User, required=True)
    # the set to track user for
    theset = db.ReferenceProperty(Set, required=True)
    # the term they are attempting to answer (which one is determined by settype)
    termTag = db.ReferenceProperty(Tag)
    termData = db.ReferenceProperty(StudyData)
    # number of times they got the answer correct
    numCorrect = db.IntegerProperty(required = True)
    # number of tries they had on this term
    numTries = db.IntegerProperty(required = True)

#returns overall percent correct, total number of people who tried
def getStatsFor(theset, term):
    query = None
    if theset.settype == "tag":
        query = Stat.gql("WHERE theset = :theset AND termTag = :term", theset=theset, term=term)
    else:
        query = Stat.gql("WHERE theset = :theset AND termData = :term", theset=theset, term=term)
    if query.count() == 0:
        return 0,0
    totalPercent = 0
    numPeople = query.count()
    for stat in query:
        if stat.numTries > 0:
            totalPercent += (float(stat.numCorrect)/stat.numTries)
    overallPercent = (totalPercent / numPeople)*100
    return overallPercent, numPeople
    
def getStatForUser(theset, term, user):
    query = None
    if theset.settype == "tag":
        query = Stat.gql("WHERE theset = :theset AND termTag = :term AND user = :user", 
            theset=theset, term=term, user=user)
    else:
        query = Stat.gql("WHERE theset = :theset AND termData = :term AND user = :user", 
            theset=theset, term=term, user=user)
    if query.count() == 0:
        return False
    return query.get()
    
def getOverallScoreFor(theset, user):
    query = Stat.gql("WHERE theset = :theset AND user = :user",theset=theset, user=user)
    if query.count() == 0:
        return 0
    mean = 0
    numQs = query.count()
    for userterm in query:
        mean += (userterm.numCorrect / float(userterm.numTries))
    return (mean / float(numQs))*100
    
def getOverallStats(theset):
    query = Stat.gql("WHERE theset = :theset", theset=theset)
    if query.count() == 0:
        return False,[]
    distr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    mean = 0
    users = []
    scores = []
    for stat in query:
        if stat.user.username in users:
            # ignore if already calculated for this user
            continue
        users.append(stat.user.username)
        overallScore = getOverallScoreFor(theset, stat.user)
        mean += overallScore
        if overallScore == 100:
            overallScore = 99 # need to round down so no 100-109 section
        distr[ int(overallScore / 10) ] += 1
        scores.append([stat.user.username, overallScore])
        
    mean = float(mean)/len(users)
    return mean, distr
    
def addStat(setId, termId, user, isCorrect):
    theset = getSetById(setId)
    theterm = db.get(termId)
    newOrUpdateStat = getStatForUser(theset, theterm, user)
    if newOrUpdateStat:
        newOrUpdateStat.numTries += 1
        if isCorrect:
            newOrUpdateStat.numCorrect += 1
    else:
        numCorrect = 0
        if isCorrect:
            numCorrect = 1
        newOrUpdateStat = Stat(user=user, theset=theset, numCorrect=numCorrect, numTries=1)
        if theset.settype == "tag":
            newOrUpdateStat.termTag = theterm
        else:
            newOrUpdateStat.termData = theterm
    newOrUpdateStat.put()