import Tkinter, tkMessageBox, math, DataSet

class classy:
    
    # @param dataSet: the CS log data set 
    # @param depend: the dependent variable to predict.  
    #                "default" || "browser" = browser
    #                also "time", "location", and "OS"   
    def __init__(self, dataSet, depend):
       # Number of errors
       locErr = 0
       locErr2 = 0
       timeErr = 0
       timeErr2 = 0
       OSErr = 0
       OSErr2 = 0
       koopErr = 0
       koopErr2 = 0
          
       # Number correct
       koopRight = 0
       locRight = 0
       timeRight = 0
       OSRight = 0
       koopRight2 = 0
       locRight2 = 0
       timeRight2 = 0
       OSRight2 = 0
       
       # For inputs (info content)
       timeInput = 0
       locInput = 0
       OSInput = 0
       browInput = 0
       
       time1way = 0
       time2way = 0
       loc1way = 0
       loc2way = 0
       os1way = 0
       os2way = 0
       brow1way = 0
       brow2way = 0   
       
       # Calculate errors
       for i in xrange(dataSet.size()):

           # Grab all variables
           koopa = dataSet.select([8])[0][i]   # Browser
           # Get the (non-numeric) user agent string for Browser and OS
           UA = dataSet.nonNum[koopa]
           browser = dataSet.getBrowser(UA)
           OS = dataSet.getOS(UA)
           
           loc = dataSet.select([0])[0][i]     # Location
           time = dataSet.select([2])[0][i]    # Time
           
           
           # BROWSER ERROR
           if browser == "other":
               brow1way += 1
               #print depend, loc, time, OS
               if depend == "location":
                   if loc != 0:
                       koopErr += 1
                   else: koopRight += 1    
               
               elif depend == "time": 
                   if time > 060000:
                       koopErr += 1
                   else: koopRight += 1
                     
               elif depend == "os": 
                   if OS != "other":
                       koopErr += 1
                   else:
                       koopRight += 1
           else: 
               brow2way += 1
               if depend == "location":
                   if loc != 0:
                       koopErr2 += 1
                   else: koopRight2 += 1    
               
               elif depend == "time": 
                   if time > 060000:
                       koopErr2 += 1
                   else: koopRight2 += 1
                     
               elif depend == "os": 
                   if OS != "other":
                       koopErr2 += 1
                   else:
                       koopRight2 += 1
           # LOCATION ERROR: 0=off-campus, 1=on-campus
           if (loc == 1):
               loc1way += 1
               if (depend == "browser" or depend == "default"): 
                   if browser != 'firefox':
                       locErr += 1
                   else: locRight += 1
                   
               elif depend == "time":
                   if time < 060000:
                       locErr += 1
                   else: locRight += 1    
               elif depend == "os":
                   if OS == "other":
                       locErr += 1
                   else: locRight += 1 
           else: 
               loc2way += 1
               if (depend == "browser" or depend == "default"): 
                   if browser != 'firefox':
                       locErr2 += 1
                   else: locRight2 += 1
                   
               elif depend == "time":
                   if time < 060000:
                       locErr2 += 1
                   else: locRight2 += 1    
               elif depend == "os":
                   if OS == "other":
                       locErr2 += 1
                   else: locRight2 += 1              
           # TIME ERROR: hhmmss    
           if (time < 060000):
                time1way += 1  
                if (depend == "browser" or depend == "default"): 
                   if not browser == 'other':
                       timeErr += 1
                   else: timeRight += 1 
                
                elif depend == "location": 
                    if loc != 0:
                        timeErr += 1
                    else: timeRight += 1
                
                elif depend == "os":
                    if not OS == "other":
                        timeErr += 1
                    else: timeRight += 1   
           else:
                time2way += 1
                if (depend == "browser" or depend == "default"): 
                   if not browser == 'other':
                       timeErr2 += 1
                   else: timeRight2 += 1 
                
                elif depend == "location": 
                    if loc != 0:
                        timeErr2 += 1
                    else: timeRight2 += 1
                
                elif depend == "os":
                    if not OS == "other":
                        timeErr2 += 1
                    else: timeRight2 += 1                 
           # OS ERROR
           if (OS == 'mac'):
                os1way += 1 
                if depend == "browser" or depend == "default": 
                    if browser != 'firefox':
                        OSErr += 1
                    else: OSRight += 1
                elif depend == "location": 
                    if loc != 1:
                        OSErr += 1
                    else: OSRight += 1
                elif depend == "time":
                   if time > 060000:
                       OSErr += 1
                   else: OSRight += 1
           else:  
                os2way += 1
                if depend == "browser" or depend == "default": 
                    if browser != 'firefox':
                        OSErr2 += 1
                    else: OSRight2 += 1
                elif depend == "location": 
                    if loc != 1:
                        OSErr2 += 1
                    else: OSRight2 += 1
                elif depend == "time":
                   if time > 060000:
                       OSErr2 += 1
                   else: OSRight2 += 1        
                      
       # Error Rates and Info Gains!
       bass = ""
       if (depend != "time"):
           one = (0.0 + time1way) / (time1way+time2way)
           two = (0.0 + time2way) / (time1way+time2way)
           timeInput = self.getInput(one, two)
                              
           timeErrorRate = (float(timeErr) / (dataSet.size()))
           timeInfoContent = self.getInfoContent(timeErr, timeRight, timeErr2, timeRight2, dataSet.size())  
           timeInfoGain = timeInput - timeInfoContent      
           bass +=  "Time Error Rate: " + str(timeErrorRate) + '\n'
           bass += "Time Info Gain:  " + str(timeInfoGain) + '\n\n'          
       
       if (depend != "location"):   
           one = (0.0 + loc1way)/(loc1way+loc2way)
           two = (0.0 + loc2way)/(loc1way+loc2way)
           locInput = self.getInput(one,two)
                       
           locErrorRate =  (float(locErr) / (dataSet.size()))
           locInfoContent = self.getInfoContent(locErr, locRight, locErr2, locRight2, dataSet.size())
           locInfoGain = locInput - locInfoContent    
           bass += "Location Error Rate: " + str(locErrorRate) + '\n'
           bass += "Location Info Gain: " + str(locInfoGain) + '\n\n'
       
       if (depend != "os"):
           one = (0.0 + os1way)/(os1way+os2way)
           two = (0.0 + os2way)/(os1way+os2way)
           OSInput = self.getInput(one,two)
                     
           OSErrorRate = (float(OSErr) / (dataSet.size()))
           OSInfoContent = self.getInfoContent(OSErr, OSRight, OSErr2, OSRight2, dataSet.size())
           OSInfoGain =  OSInput - OSInfoContent   
           bass += "OS Error Rate: " + str(OSErrorRate) + '\n'
           bass += "OS Info Gain: " + str(OSInfoGain) + '\n\n'
           
       if (depend != "browser" and depend != "default"):
           one = (0.0 + brow1way)/(brow1way+brow2way)
           two = (0.0 + brow2way)/(brow1way+brow2way)
           browInput = self.getInput(one,two)
         
           browErrorRate = (float(koopErr) / (dataSet.size()))
           browInfoContent = self.getInfoContent(koopErr, koopRight, koopErr2, koopRight2, dataSet.size())
           browInfoGain = browInput - browInfoContent     
           bass += "Browser Error Rate: " + str(browErrorRate) + '\n'
           bass += "Browser Info Gain: " + str(browInfoGain) + '\n\n'
           
       print bass
       
       tkMessageBox.showinfo(title='1R Decision Tree', message=bass)       

    def getInfoContent(self, err, right, err2, right2, dataSize):
       err,right,err2,right2,dataSize = \
       float(err),float(right),float(err2),float(right2),float(dataSize)
       print "Stuffs:",err,right,err2,right2,dataSize
       return ( (err + right) / dataSize ) * \
              -(err / dataSize) * math.log(err / dataSize, 2) \
              -(right / dataSize) * math.log(right / dataSize, 2) \
              + ( (err2 + right2) / dataSize ) * \
              -(err2 / dataSize) * math.log(err2 / dataSize, 2) \
              -(right2 / dataSize) * math.log(right2 / dataSize, 2)  
              
    def getBrowser(self, UA):
        # Get browser (Firefox, Opera, Safari, Chrome, IE, Other
        browser = "other"
        if UA.find("Mobile") > -1 and UA.find("AppleWebKit") > -1:
            browser = "mobilesafari"
        elif UA.find("Chrome") > -1:
            browser = "chrome"
        elif UA.find("Safari") > -1:
            browser = "safari"
        elif UA.find("Opera") > -1:
            browser = "opera"
        elif UA.find("Firefox") > -1 or UA.find("Shiretoko") > -1 \
        or UA.find("Namaroka") > -1 or UA.find("Iceweasel") > -1 \
        or UA.find("GranParadiso") > -1:
            browser = "firefox"
        elif UA.find("MSIE") > -1:
            browser = "ie"
        elif UA.find("Konqueror") > -1:
            browser = "konqueror"
    
    def getOS(self, UA):
        # Get OS (Macintosh, Windows, Linux, iPhone/iPad/iPod, Other)
        OS = "other"
        if UA.find("Linux") > -1:
            OS = "linux"
        elif UA.find("Windows") > -1:
            OS = "windows"
        elif UA.find("Macintosh") > -1:
            OS = "mac"
        elif UA.find("iPhone") > -1 or UA.find("iPad") > -1 or UA.find("iPod") > -1:
            OS = "mobilemac"
        elif UA.find("SunOS") > -1:
            OS = "sunos"
        elif UA.find("BSD") > -1:
            OS = "bsd"       
    
    def getInput(self, one, two):
        return -one * math.log(one, 2) - two * math.log(two, 2)