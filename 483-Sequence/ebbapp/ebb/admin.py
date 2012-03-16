'''
Admin panel for removing/modifying database content.
'''
from ebb import app
from flask import g, redirect, render_template, request, url_for
from decorators import login_required
import config

@app.route('/admin')
@login_required
def admin():
    if g.userId in config.adminUsers:
        return "admin panel"
    return redirect(url_for('index'))
