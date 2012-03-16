# -*- coding: utf-8 -*-
import logging, email
from google.appengine.ext import webapp
from google.appengine.ext.webapp.mail_handlers import InboundMailHandler
from google.appengine.ext.webapp.util import run_wsgi_app

class MailHandler(InboundMailHandler):
    def receive(self, message):
        # log sender
        logging.info("Recieved email from: "+message.sender)
        
        # send it to gmail
        bodies = message.bodies(content_type='text/plain')
        allBodies = "";
        for body in bodies:
            allBodies = allBodies + "\n---------------------------\n" + body[1].decode()
            if isinstance(allBodies, unicode):
                allBodies = allBodies.encode('utf-8')   
            #xmpp.send_message("pgsners+ebb@gmail.com", "\n" + message.subject + allBodies)
            message = mail.EmailMessage(sender=sender, subject=unicode(message.subject))
            message.body = allBodies
            message.to="pgsners+ebb@gmail.com"
            message.send()

app = webapp.WSGIApplication([MailHandler.mapping()], debug=True)
run_wsgi_app(app)
