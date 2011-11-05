'''This here is a naive Bayes classifier.  
        #1. Find P (x | A), p(A), p(x)
        #2. Find P (x | B), p(B)
        #3. P(A|x) = P(x|A) * p(A) / p(x)
        #4. P(B|x) = P(x|B) * p(B) / p(x)
        #5. Is P(A|x) or P(B|x) bigger?
        #6. Bigger prob ==> category label.'''

import Tkinter, tkMessageBox, math, DataSet

class bayesian:
    def __init__(self, dataSet, trainSet, depend):
        
        # Get actual probabilities
        for i in xrange(trainSet.size()):
            '''RESETTING'''
            dep, indep1, indep2, indep3 = False, False, False, False
            
            '''SNAGGING THE VARIABLES'''         
            # TRAIN SET
            koopaTrain = trainSet.select([8])[0][i]   # Browser
            # Get the (non-numeric) user agent string for Browser and OS
            UATrain = trainSet.nonNum[koopaTrain]
            
            browserTrain = trainSet.getBrowser(UATrain)
            OSTrain = trainSet.getOS(UATrain)
            locTrain = trainSet.select([0])[0][i]     # Location
            timeTrain = trainSet.select([2])[0][i]    # Time   
            
            '''CALCKING THE PROBS''' 
            # Lots of variables to know!  
            ''' probBROTHER, bigTimeBROTHER, smallTimeBROTHER, OSnotOtherBROTHER, \
            OSOtherBROTHER, locNotZeroBROTHER, locZeroBROTHER, probNotBrother, \
            bigTimeNotBROTHER, smallTimeNotBROTHER, OSnotOtherNotBROTHER, \
            OSOtherNotBROTHER, locNotZeroNotBROTHER, locZeroNotBROTHER \ '''
            
            dep, indep1, indep2, indep3 = self.getVarBools(depend, browserTrain, timeTrain, OSTrain, locTrain)
                    
            a, B, C, D, E, F, G, h, I, J, K, L, M, N  = self.checkDataPt(dep, indep1, indep2, indep3)

        '''STEPS THREE & FOUR:'''
        pBa = B * a
        pCa = C * a
        pIh = I * h
        pJh = J * h
        
        pDa = D * a
        pEa = E * a  
        pKh = K * h
        pLh = L * h
        
        pFa = F * a
        pGa = G * a
        pMh =  M * h
        pNh = N * h
        
        #Now, run through data points again.  This time, categorize based on feature vectors.
        right1 = 0
        error1 = 0
        right2 = 0
        error2 = 0
        right3 = 0
        error3 = 0
        
        for i in xrange(dataSet.size()):
            
            #Get Variables
            koopa = dataSet.select([8])[0][i]
            UA = dataSet.nonNum[koopa]
            browser = dataSet.getBrowser(UA)    # Browser
            OS = dataSet.getOS(UA)              # OS
            loc = dataSet.select([0])[0][i]     # Location
            time = dataSet.select([2])[0][i]    # Time
            
            dep, indep1, indep2, indep3 = self.getVarBools(depend, browser, time, OS, loc)
        
            if indep1:
                if pBa > pIh:
                    if dep:
                        right1 += 1
                    else:
                        error1 += 1
                else:
                    if dep == False:
                        right1 += 1
                    else:
                        error1 += 1
            else:
                if pCa > pJh:
                    if dep:
                        right1 += 1
                    else:
                        error1 += 1
                else:
                    if dep == False:
                        right1 += 1
                    else:
                        error1 += 1  
            
            if indep2:
                if pEa > pLh:
                    if dep:
                        right2 += 1
                    else:
                        error2 += 1
                else:
                    if dep == False:
                        right2 += 1
                    else:
                        error2 += 1
            else: 
                if pDa > pKh:
                    if dep:
                        right2 += 1
                    else:
                        error2 += 1
                else:
                    if dep == False:
                        right2 += 1
                    else:
                        error2 += 1
            
            if indep3:
                if pGa > pNh:
                    if dep:
                        right3
                    else:
                        error3 += 1
                else:
                    if dep == False:
                        right3 += 1
                    else:
                        error3 += 1
            else:
                if pFa > pMh:
                    if dep:
                        right3 += 1
                    else:
                        error3 += 1
                else:
                    if dep == False:
                        right3 += 1
                    else:
                        error3 += 1
        
        '''Now, get the error rates''' 
        bass = ""
        if (depend == "browser"):
           timeErrorRate = (float(error1) / (dataSet.size()))
              
           bass +=  "Time Error Rate: " + str(timeErrorRate) + '\n\n'
           
           OSErrorRate = (float(error2) / (dataSet.size()))      
           bass +=  "OS Error Rate: " + str(OSErrorRate) + '\n\n'
           
           locErrorRate = (float(error3) / (dataSet.size()))      
           bass +=  "Location Error Rate: " + str(locErrorRate) + '\n\n'
        
        elif (depend == "time"):    #browser, OS, location
           browErrorRate = (float(error1) / (dataSet.size()))      
           bass +=  "Browser Error Rate: " + str(browErrorRate) + '\n\n'
           
           OSErrorRate = (float(error2) / (dataSet.size()))      
           bass +=  "OS Error Rate: " + str(OSErrorRate) + '\n\n'
           
           locErrorRate = (float(error3) / (dataSet.size()))      
           bass +=  "Location Error Rate: " + str(locErrorRate) + '\n\n'
           
        elif (depend == "OS"):      #browser, time, location
           browErrorRate = (float(error1) / (dataSet.size()))      
           bass +=  "Browser Error Rate: " + str(browErrorRate) + '\n\n'
           
           timeErrorRate = (float(error2) / (dataSet.size()))      
           bass +=  "Time Error Rate: " + str(timeErrorRate) + '\n\n'
           
           locErrorRate = (float(error3) / (dataSet.size()))      
           bass +=  "Location Error Rate: " + str(locErrorRate) + '\n\n' 
           
        else: #location            #browser, time, OS
           browErrorRate = (float(error1) / (dataSet.size()))      
           bass +=  "Browser Error Rate: " + str(browErrorRate) + '\n\n'
           
           timeErrorRate = (float(error2) / (dataSet.size()))      
           bass +=  "Time Error Rate: " + str(timeErrorRate) + '\n\n'
           
           OSErrorRate = (float(error2) / (dataSet.size()))      
           bass +=  "OS Error Rate: " + str(OSErrorRate) + '\n\n'
           
        tkMessageBox.showinfo(title='Bayesian Classifier', message=bass)  
        
        
    # For snagging probabilities.      
    def checkDataPt(self, dep, indep1, indep2, indep3):        
        a, B, C, D, E, F, G, h, I, J, K, L, M, N = 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 
        if dep:
            a += 1
            if indep1:
                B += 1
            else:
                C += 1
            if indep2:
                D += 1
            else: 
                E += 1
            if indep3:
                F += 1
            else: G += 1
        else:
            h += 1
            if indep1:
                I += 1
            else:
                J += 1
            if indep2:
                K += 1
            else: 
                L += 1
            if indep3:
                M += 1
            else: N += 1    
                
        return a, B, C, D, E, F, G, h, I, J, K, L, M, N
    
    #Snag the dep & indep variables - are they true or false?
    def getVarBools(self, depend, browser, time, OS, loc):
        dep, indep1, indep2, indep3 = False, False, False, False
        if (depend == "browser"):
            if browser == "other":
                dep = True
            if time > 060000:
                indep1 = True
            if OS != "other":
                indep2 = True
            if loc != 0:
                indep3 = True
            
        elif (depend == "time"):
            if time > 060000:
                dep = True
            if browser == "other":
                indep1 = True
            if OS != "other":
                indep2 = True
            if loc != 0:
                indep3 = True
    
        elif (depend == "OS"):
            if OS != "other":
                dep = True
            if browser == "other":
                indep1 = True
            if time > 060000:
                indep2 = True
            if loc != 0:
                indep3 = True
    
        else: #depend == "location"
            if loc != 0: 
                dep = True
            if browser == "other":
                indep1 = True
            if time > 060000:
                indep2 = True
            if OS != "other":
                indep3 = True
        
        return dep, indep1, indep2, indep3