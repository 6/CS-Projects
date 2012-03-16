from flaskext import wtf
from flaskext.wtf import validators

class SubmitSet(wtf.Form):
    '''
    Submit a new study-set.
    '''
    title = wtf.TextField('Study Set Title', validators=[validators.Required()])
    #TODO
