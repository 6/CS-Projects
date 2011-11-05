import Tkinter, tkFileDialog, tkColorChooser, tkSimpleDialog, tkMessageBox, numpy
import math, random
import CSLogData, DataSet, ViewRef, classy, bayesian
import arff
# create a shorthand object for Tkinter
tk = Tkinter

# create a class to build and manage the display
class DisplayApp:

    def __init__(self, width, height):

        # create a tk object, which is the root window
        self.root = tk.Tk()
        
        # make the top frame
        self.frame2 = tk.Frame(self.root, width = 1024, height = 20)
        self.frame2.pack_propagate(0)
        self.frame2.pack()
        
        t = tk.Label(self.frame2, text="EIGENware Data Visualization", bg = "darkgreen", fg = "pink")
        t.pack(fill=tk.X)
        
        # Make the frame to choose axes
        self.frame = tk.Frame(self.root)        
        self.frame.pack()

        bk = tk.Label(self.frame, text="EIGENware Data Visualization", bg = "grey")
        bk.pack(fill=tk.X)
        
        MODES = [("Histogram    ", 1, self.goHisto),
                 ("2-D    ", 2, self.goNi),
                 ("3-D    ", 3, self.goSan)]

        v = tk.IntVar()
        for mode, val, com in MODES:
            tk.Radiobutton(self.frame, text=mode, value=val, variable=v, command=com).pack(side = tk.LEFT, anchor = tk.CENTER, in_ = bk)
            
        # width and height of the main window
        self.initDx = width
        self.initDy = height

        # set up the geometry for the window
        self.root.geometry( "%dx%d+50+30" % (self.initDx, self.initDy) )

        # set the title of the window
        self.root.title("EIGENware")

        # set the maximum size of the window for resizing
        self.root.maxsize( 1024, 768 )

        # bring the window to the front
        self.root.lift()

        # setup the menus
        self.buildMenus()

        # build the objects on the Canvas
        self.buildCanvas()

        # set up the key bindings
        self.setBindings()

        # set up other variables
        self.shape = "oval"
        self.axesColor = "black"
        
        # create view reference object
        self.vRef = ViewRef.ViewRef()
        
        # set up VTM
        self.buildAxes()
        
        # start off with no data
        self.dataSet = False
        self.dataM = False
        
        # turn off rotationMode
        self.rotationMode = False
        
        # turn off z's being set to zero
        self.ZeroThemZs = False
    
        # We start in Histo-Mode!    
        self.histoMode = True
        
        # Bins for histogram (so don't have to initialize more than once)
        self.bins = False
        
    def goHisto(self):
        print "Histogram Tiiime"
        
        # get number of bins?
        #self.numBins = tkSimpleDialog.askinteger("Histogram Bins", "Number of Bins:")
        
        #print "Answer:",self.numBins
        
        # If we're not in Histo-Mode
        if not self.histoMode:   
            self.histoMode = True;
            
            # Move current data points out of the way
            if type(self.dataSet) is not bool:
                for dataPoint in self.dataGfx:
                    self.canvas.delete(dataPoint)
            
        # turn on z's being set to zero
        self.ZeroThemZs = True;
        
        # turn off rotation.
        self.rotationMode = False
        
        # reset view
        self.vRef.reset(self.vRef.view, self.vRef.viewOffset)
        self.updateAxes()
        
        # If data is loaded
        if type(self.dataSet) is not bool:
        
            # If bins haven't already been calculated
            if type(self.bins) is bool:
                print "Calculating bin values"
                
                allDates = self.dataSet.select([1])[0]
                # Get unique dates
                uDates = numpy.unique(allDates)
                print "unique dates:",uDates
                self.bins = {}
                
                # for each unique date
                for i in range(uDates.shape[0]):
                    
                    # Count the number of lines
                    idxs = numpy.where(allDates == uDates[i])
                    freq = idxs[0].shape[0]
                    
                    # Make the count the bin size
                    self.bins[uDates[i]] = freq
            
            # Find current axes 
            xAxis = self.axesGfx[0]
            s = self.canvas.coords(xAxis)
            x0,y0,x1 = s[0]+1,s[1],s[2] 
            xLength = numpy.abs(x0-x1)-1
            
            # Calculate bin width
            binWidth = xLength / len(self.bins) # round down?
            
            # Find height multiplier based on max hits value
            maxHitsSort = sorted([(val,key) for (key,val) in self.bins.items()])
            maxHits = maxHitsSort[len(maxHitsSort)-1][0]
            heightMult = xLength/maxHits
            
            # Keep track of bin graphics objects
            self.binGfx = []
            
            # Loop through bins in order of oldest to most recent date
            for i,date in enumerate(sorted(self.bins.iterkeys())):

                # Calculate the four corners of the bin (rectangle)
                xStart = x0 + (binWidth * i)
                xEnd = xStart + binWidth
                
                yStart = y0
                hits = self.bins[date]
                yEnd = yStart - (hits * heightMult)
                
                # Draw the bin
                rgb = "#%2x%2x%2x" % (random.randint(17, 255), 
                                  random.randint(17, 255), 
                                  random.randint(17, 255) )
                bin = self.canvas.create_rectangle( xStart, yStart, 
                                        xEnd, yEnd,
                                        fill = rgb,
                                        outline='')
                self.binGfx.append(bin)

        self.updateData()
        
    def goNi(self):
        print "Ni Tiiime"
        # set z-coords of data points to zero.
        self.ZeroThemZs = True
        
        # turn off rotation.
        self.rotationMode = False
    
        # We're not in Histo-Mode!
        if self.histoMode:    
            self.histoMode = False
            
            if type(self.dataSet) is not bool:
                # Delete histogram bars
                for bar in self.binGfx:
                    self.canvas.delete(bar)
                
                # Re-draw data points
                self.buildData()
        
        # reset view
        self.vRef.reset(self.vRef.view, self.vRef.viewOffset)
        self.updateAxes()
        self.updateData()
        
    def goSan(self):
        print "San Tiiime"
        # regular 3-D visualization
        
        # turn on rotation.
        self.rotationMode = True
        
        # turn off z's being set to zero
        self.ZeroThemZs = False
        
        # We're not in Histo-Mode!
        if self.histoMode:    
            self.histoMode = False
            
            if type(self.dataSet) is not bool:
                # Delete histogram bars
                for bar in self.binGfx:
                    self.canvas.delete(bar)
                
                # Re-draw data points
                self.buildData()
                
        # reset view
        self.vRef.reset(self.vRef.view, self.vRef.viewOffset)
        self.updateAxes()
        self.updateData()
        
    def buildMenus(self):
        
        # create a new menu
        self.menu = tk.Menu(self.root)

        # set the root menu to our new menu
        self.root.config(menu = self.menu)

        # create a variable to hold the individual menus
        self.menulist = []

        # create a file menu
        filemenu = tk.Menu( self.menu )
        self.menu.add_cascade( label = "File", menu = filemenu )
        self.menulist.append(filemenu)

        # create another menu for kicks
        viewmenu = tk.Menu( self.menu )
        self.menu.add_cascade( label = "View", menu = viewmenu )
        self.menulist.append(viewmenu)

        # create another menu for more kicks
        analysismenu = tk.Menu( self.menu )
        self.menu.add_cascade( label = "Analysis", menu = analysismenu )
        self.menulist.append(analysismenu)

        # create another menu for even more kicks
        exportmenu = tk.Menu( self.menu )
        self.menu.add_cascade( label = "Export", menu = exportmenu )
        self.menulist.append(exportmenu)
        
        # menu text for the elements
        menutext = [ [ 'Open... \xE2\x8C\x98-O', 'Close', '-', 'Quit \xE2\x8C\x98-Q' ],
                     [ 'Restore Default View','Controls' ],
                     [ 'Data Statistics', 'Run 1R Decision Tree', 'Run Bayesian Classifier', 'Train and Test (Bayes)' ],
                     [ 'Make arff File', 'Make CSV File' ] ]

        # menu callback functions
        menucmd = [ [self.handleOpen, self.handleClose, None, self.handleQuit],
                    [self.handleCmd0, self.handleCmd1],
                    [self.handleCmd2, self.handleCmd3, self.handleCmd4, self.handleCmd6],
                    [self.handleCmd5, self.handleCmd7 ] ]
        
        # build the menu elements and callbacks
        for i in range( len( self.menulist ) ):
            for j in range( len( menutext[i]) ):
                if menutext[i][j] != '-':
                    self.menulist[i].add_command( label = menutext[i][j],
                                                  command=menucmd[i][j] )
                else:
                    self.menulist[i].add_separator()

    # builds a canvas for drawing that expands to fill the window
    def buildCanvas(self):
        self.canvas = tk.Canvas( self.root, width=self.initDx, 
                                 height=self.initDy )
        self.canvas.pack( expand=tk.YES, fill=tk.BOTH )
    
    # build the view transformation matrix [VTM], multiplies the axis endpoints
    # by the VTM, then creates three new line objects, one for each axis. Store
    # the three line objects.
    def buildAxes(self):
        
        # six endpoints. the length of each axis is 1
        self.axes = numpy.matrix([[0,0,0,1],
                                  [1,0,0,1],
                                  [0,0,0,1],
                                  [0,1,0,1],                                  
                                  [0,0,0,1],
                                  [0,0,1,1]])
        
        # build the view transformation matrix [VTM]
        self.vRef.build()
        
        # multiply the axis endpoints by the VTM
        pts = (self.vRef.vtm * self.axes.getT()).getT()[:,:2]
        
        xAxis = pts[:2].getA()
        yAxis = pts[2:4].getA()
        zAxis = pts[4:6].getA()
        
        # You'll also want a list to hold the actual graphics objects 
        # (the lines) that instantiate them on the screen.
        xObj = self.canvas.create_line(xAxis[0][0], xAxis[0][1],
                                       xAxis[1][0], xAxis[1][1], arrow=tk.LAST)
        yObj = self.canvas.create_line(yAxis[0][0], yAxis[0][1],
                                       yAxis[1][0], yAxis[1][1], arrow=tk.LAST)
        zObj = self.canvas.create_line(zAxis[0][0], zAxis[0][1],
                                       zAxis[1][0], zAxis[1][1], arrow=tk.LAST)
        self.axesGfx =  [xObj, yObj, zObj]
        
        
        
    def updateAxes(self):
        
        # build the view transformation matrix [VTM]
        self.vRef.build()
        
        # multiply the axis endpoints by the VTM
        pts = (self.vRef.vtm * self.axes.getT()).getT()[:,:2].getA()
        
        # for each line object
        i = 0
        for line in self.axesGfx:
            
            # new coordinates
            x1 = pts[i][0]
            y1 = pts[i][1]
            x2 = pts[i+1][0]
            y2 = pts[i+1][1]
            
            # update the coordinates of the object
            self.canvas.coords(line, x1, y1,
                                     x2, y2)
            i+= 2
            
    # For 2D and 3D data
    def buildData(self):
        # Normalize to 0-500px ( fix this up some more )
        normMax = 1.0
        
        tIdx = 2 # time
        fIdx = 5 # page/file size
        dIdx = 1 # dateTime
        
        tMinMax = self.dataSet.range(tIdx)
        tMin = tMinMax[0]
        tRange = tMinMax[1]-tMin
        
        fMinMax = self.dataSet.range(fIdx)
        fMin = fMinMax[0]
        fRange = fMinMax[1]-fMin
        
        dMinMax = self.dataSet.range(dIdx)
        dMin = dMinMax[0]
        dRange = dMinMax[1]-dMin
        
        cols = self.dataSet.select([tIdx, fIdx, dIdx])
        tCol = cols[0]
        fCol = cols[1]
        dCol = cols[2]
        
        tNorm = (normMax / tRange) * (tCol - tMin)
        fNorm = (normMax / fRange) * (fCol - fMin)
        dNorm = (normMax / dRange) * (dCol - dMin)
        
        dx = 4
        dy = 4
        
        self.dataGfx = []
        self.dataTemp = []
        
        
        print 'initializing points...'
        for i in xrange(tNorm.shape[0]):
            x = tNorm[i]
            y = fNorm[i]
            z = dNorm[i]
            if self.ZeroThemZs:
                z = 0
            self.dataTemp.append([x,y,z,1])
            if self.dataSet.select([0])[0][i] == 0: #on campus
                rgb = 'red'
            else:
                rgb = 'blue'
            
            # get referrer!
            refer = self.dataSet.select([3])[0][i] 
            
            #determine shape
            if refer == 0:      #no referrer
                shape = self.canvas.create_oval(x - dx,y - dy,x + dx,y + dy, fill=rgb)
            elif refer == 1:    #from colby
                shape = self.canvas.create_rectangle(x - dx,y - dy,x + dx,y + dy, fill=rgb)
            else:               #from outside
                shape = self.canvas.create_line(x - dx,y - dy,x + dx,y + dy, fill=rgb)
                
            #shape = self.canvas.create_oval(x - dx,y - dy,x + dx,y + dy, fill=rgb)
            self.dataGfx.append(shape)
            
        # Convert all the data currently displayed into a numpy matrix
        self.dataM = numpy.matrix(self.dataTemp)
        print 'finished initializing points'
     
    def updateData(self):
        
        if type(self.dataM) is not bool and self.histoMode == False:
            self.vRef.build()
        
            pts = (self.vRef.vtm * self.dataM.getT()).getT()[:,:2].getA()
        
            for i,point in enumerate(self.dataGfx):
                curCoords = self.canvas.coords(point)
                
                dX = pts[i][0] - curCoords[0]
                dY = pts[i][1] - curCoords[1]
                self.canvas.move(point, dX, dY)

            
    # binds user actions to functions
    def setBindings(self):
        self.root.bind( '<Button-1>', self.handleButton1 )
        self.root.bind( '<Button-2>', self.handleButton2 )
        self.root.bind( '<Button-3>', self.handleButton3 )
        self.root.bind( '<B1-Motion>', self.handleButton1Motion )
        self.root.bind( '<B2-Motion>', self.handleButton2Motion )
        self.root.bind( '<B3-Motion>', self.handleButton3Motion )
        self.root.bind( '<Command-o>', self.handleOpenEvent )
        self.root.bind( '<Control-o>', self.handleOpenEvent )
        self.root.bind( '<Command-w>', self.handleCloseEvent )
        self.root.bind( '<Control-w>', self.handleCloseEvent )
        self.root.bind( '<Control-q>', self.handleQuit )   #cmd-q happens automatically
        self.root.bind( '<Control-h>', self.handleHideAxes )
        self.root.bind( '<Double-Button-1>', self.handleDubButton1 )
                   
    # toggles axes on & off
    def handleHideAxes(self, event):
        #simple fix
        if self.axesColor == "black":
            self.axesColor = "white"
        else: self.axesColor = "black"
        for line in self.axesGfx:
            self.canvas.itemconfig(line, fill=self.axesColor)    
        
        
    # handles cmd-o event
    def handleOpenEvent(self, event):
        self.handleOpen()
    
    #handles cmd-w event
    def handleCloseEvent(self, event):
        self.handleClose()
        
    # handles Open... in the File menu
    def handleOpen(self):
        if type(self.dataSet) is not bool:
            tkMessageBox.showinfo(title='Open File Failed', message="Please close the current data set first.")
            return
        # Loading
        load = self.canvas.create_text(430, 10, text = "Opening file data...")    
        
        print 'handleOpen'
        fobj = tkFileDialog.askopenfile( parent=self.root, mode='rb', 
                                         title='Choose a data file' )
        if fobj == None:
            print 'User did not select a file'
            self.canvas.delete(load)
            return

        csData = CSLogData.CSLogDataClass(fobj,True)
        #csData = ComLogData.ComLogDataClass(fobj, True)
        csData.cleanData()

        # just close the file for now
        print 'closing file'
        fobj.close()
        
        # Construct metadata
        metadata = [
                ["ipLocation",{"dict":False,"tslate":True,
                               "vals":{0:"off campus",1:"on campus"}}],
                ["dateTime",{"dict":False,"tslate":False,"vals":False}],
                ["time",{"dict":False,"tslate":False,"vals":False}],
                ["referrer",{"dict":False,"tslate":True,
                             "vals":{0:"no referrer",1:"from colby",
                                     2:"outside source"}}],
                ["statusCode",{"dict":False,"tslate":False,"vals":False}],
                ["pageSize",{"dict":False,"tslate":False,"vals":False}],
                ["httpVersion",{"dict":False,"tslate":True,
                                "vals":{10:"HTTP/1.0",11:"HTTP/1.1"}}],
                ["accessedPage",{"dict":True,"tslate":False,"vals":False}],
                ["userAgent",{"dict":True,"tslate":False,"vals":False}]
               ]
        self.dataSet = DataSet.DataSet(csData.lines,metadata)
        self.dataSet.toMatrix()
        
        self.goHisto()
        
        self.updateData()
        
        self.canvas.delete(load)

    # handles Close in the File menu
    def handleClose(self):
        if type(self.dataSet) is not bool:
            if self.histoMode:
                # Delete histogram bars
                for bar in self.binGfx:
                    self.canvas.delete(bar)
            else:
                # Move current data points out of the way
                if type(self.dataSet) is not bool:
                    for dataPoint in self.dataGfx:
                        self.canvas.delete(dataPoint)
        # reset
        self.dataSet = False
        self.dataM = False
        self.bins = False  
    # handles Quit in the File menu
    # cmd-q automatically quits the program
    def handleQuit(self):
        yonrose = tkMessageBox.askquestion('Quit program?', 'Really quit?')
        if yonrose != 'no':
            self.root.destroy()
            print 'Quitting application.'
        print 'Resuming session.'

    # the next four functions handle menu item selections        
    def handleCmd0(self):
        yonrose = tkMessageBox.askquestion('Confirm Action', 
                                           'Restore default view?')
        if yonrose != 'no':
            self.vRef.reset(self.vRef.view, self.vRef.viewOffset)
            self.updateAxes()
            self.updateData()
            print 'View restored.'
        else: print 'View not restored.'

    def handleCmd1(self):       
        str =  ('Left Mouse Button:      Translate\n')
        str += ('Middle Mouse Button:  Rotate\n')
        str += ('Right Mouse Button:    Zoom\n')
        str += ('Ctrl + H:                      Show/Hide Axes \n')
        
        tkMessageBox.showinfo(title='Controls', message=str)

    def handleCmd2(self):
        #print self.dataSet.mean()
        #[0] = IP Address 
        #[1] = Referrer
        #[2] = Page Size
        
        if self.dataSet == None or self.dataSet == False:
            print 'User did not select a variable'
            return
        
        cheese = 'Size of Data Set: ' + str(self.dataSet.size()) + '\n\n'
        
        cheese += 'Mean % On-Campus: ' + str(self.dataSet.mean()[0]*100) + '%\n'
        cheese += 'StDev % On-Campus: ' + str(self.dataSet.stdev()[0]*100) + '%\n'
          
        if self.dataSet.median()[0] == 0:
            news = 'Off Campus'
        else:
            news = 'On Campus'
        cheese += 'Median Location: ' + news + '\n\n'
        
        cheese += 'Mean Page Size: ' + str(self.dataSet.mean()[2]*100) + '\n'
        cheese += 'StDev Page Size: ' + str(self.dataSet.stdev()[2]*100) + '\n' 
        #cheese += 'Median Page Size: ' + str(self.dataSet.median()[2]) + '\n\n'
                
        tkMessageBox.showinfo(title='Data Statistics', message=cheese)
    
    # Runs a 1R Decision Tree based on a given dependent variable
    # Message displays showing error rates for all four variables
    def handleCmd3(self):
        # Loading
        load = self.canvas.create_text(430, 10, text = "Loading...")
        dep = tkSimpleDialog.askstring("1R Decision Tree", "Please specify dependent variable (browser, location, time, or OS):")

        if dep == False or dep == None or self.dataSet == False:
            print 'User did not specify a variable, or no data set is loaded'
            self.canvas.delete(load)
            return
        
        dep = dep.lower()
        if (dep != "browser" and dep != "location" and dep != "time" and dep != "os"):
            doneMessage = self.canvas.create_text(370, 30, text = "Invalid variable name.")
            tkMessageBox.showinfo(title='1R Decision Tree', message="Invalid variable name.")
            self.canvas.delete(doneMessage)   
            self.canvas.delete(load)   
            return
        else:
            doneMessage = self.canvas.create_text(370, 30, text = "Loaded tree: " + dep + " prediction.")
                 
        self.classy = classy.classy(self.dataSet, dep)
        self.canvas.delete(load)
        self.canvas.delete(doneMessage)    
    
    def handleCmd4(self):
        # Loading
        load = self.canvas.create_text(430, 10, text = "Loading...")
        dep = tkSimpleDialog.askstring("Naive Bayes", "Please specify dependent variable (browser, location, time, or OS):")

        if dep == False or dep == None or self.dataSet == False:
            print 'User did not specify a variable, or no data set is loaded'
            self.canvas.delete(load)
            return
        
        dep = dep.lower()
        if (dep != "browser" and dep != "location" and dep != "time" and dep != "os"):
            doneMessage = self.canvas.create_text(370, 30, text = "Invalid variable name.")
            tkMessageBox.showinfo(title='1R Decision Tree', message="Invalid variable name.")
            self.canvas.delete(doneMessage)   
            self.canvas.delete(load)   
            return
        else:
            doneMessage = self.canvas.create_text(350, 30, text = "Loaded Bayes classifier: " + dep + " prediction.")
                 
        self.bayes = bayesian.bayesian(self.dataSet, self.dataSet, dep)  
        self.canvas.delete(load)
        self.canvas.delete(doneMessage)    
    
    def handleCmd5(self):
        # Loading
        load = self.canvas.create_text(430, 10, text = "Loading...")
        if self.dataSet == False:
            print "No data set loaded"
            self.canvas.delete(load)
            return 
        # Create ARFF file
        toArff = self.dataSet.select([0,2,8])
        location = toArff[0]
        time = toArff[1]
        userAgentIndices = toArff[2]
        
        arffData = []
        
        for i in range(location.shape[0]):
            uaIdx = userAgentIndices[i]
            
            # Get the (non-numeric) user agent string
            UA = self.dataSet.nonNum[uaIdx]
            browser = self.dataSet.getBrowser(UA)
            OS = self.dataSet.getOS(UA)
            arffData.append([OS, browser, location[i], time[i]])
        
        relation = "csweblogs"
        attrs = [["os",["windows","linux","mac","mobilemac","sunos","bsd","other"]],
                 ["browser",["firefox","opera","chrome","ie","mobilesafari","safari","konqueror","other"]],
                 ["location",[0,1]],
                 ["time","NUMERIC"]]
        
        percent = tkSimpleDialog.askfloat("Percentage Subset of Data", "Please specify the percentage of data you would like to write to an arff: ")  #0.01         #[1.0 = 100%]
        
        if percent == None or percent == False:
            print 'User did not select a variable'
            return
        
        self.arff = arff.ARFF(relation,attrs,arffData,percent)
        filename = "cslogdata.arff"
        self.arff.toFile(filename)
        print "Wrote arff file to "+filename
        self.canvas.delete(load)

    # Train & Predict    
    def handleCmd6(self):
        # Loading
        load = self.canvas.create_text(430, 10, text = "Loading...")
        if self.dataSet == False:
            print "No data set loaded"
            self.canvas.delete(load)
            return 
        
        # Get Training Set (Open Dialog)
        tkMessageBox.showinfo(title='Train and Test (Bayes)', message="Please select a training set.")
         
        fobj = tkFileDialog.askopenfile( parent=self.root, mode='rb', 
                                         title='Choose a data file' )
        if fobj == None:
            print 'User did not select a file'
            self.canvas.delete(load)
            return
        
        csData = CSLogData.CSLogDataClass(fobj,True)
        csData.cleanData()

        # just close the file for now
        print 'closing file'
        fobj.close()
        
        # Construct metadata
        metadata = [
                ["ipLocation",{"dict":False,"tslate":True,
                               "vals":{0:"off campus",1:"on campus"}}],
                ["dateTime",{"dict":False,"tslate":False,"vals":False}],
                ["time",{"dict":False,"tslate":False,"vals":False}],
                ["referrer",{"dict":False,"tslate":True,
                             "vals":{0:"no referrer",1:"from colby",
                                     2:"outside source"}}],
                ["statusCode",{"dict":False,"tslate":False,"vals":False}],
                ["pageSize",{"dict":False,"tslate":False,"vals":False}],
                ["httpVersion",{"dict":False,"tslate":True,
                                "vals":{10:"HTTP/1.0",11:"HTTP/1.1"}}],
                ["accessedPage",{"dict":True,"tslate":False,"vals":False}],
                ["userAgent",{"dict":True,"tslate":False,"vals":False}]
               ]
        self.trainSet = DataSet.DataSet(csData.lines,metadata)
        self.trainSet.toMatrix()
        
        # Get dependent variable
        dep = tkSimpleDialog.askstring("Naive Bayes", "Please specify dependent variable (browser, location, time, or OS):")

        if dep == False or self.dataSet == False:
            print 'User did not specify a variable, or no data set is loaded'
            self.canvas.delete(load)
            return
        
        dep = dep.lower()
        if (dep != "browser" and dep != "location" and dep != "time" and dep != "os"):
            doneMessage = self.canvas.create_text(370, 30, text = "Invalid variable name.")
            tkMessageBox.showinfo(title='1R Decision Tree', message="Invalid variable name.")
            self.canvas.delete(doneMessage)   
            self.canvas.delete(load)   
            return
        else:
            doneMessage = self.canvas.create_text(350, 30, text = "Loaded Bayes classifier: " + dep + " prediction.")
            
        # Use Bayesian.py (add dataset2 loc and so on) 
        self.bayes = bayesian.bayesian(self.dataSet, self.trainSet, dep) 
        
        self.canvas.delete(load)
    
    # exports as CSV
    def handleCmd7(self):
        # Loading
        load = self.canvas.create_text(430, 10, text = "Loading...")
        
        self.dataSet.toCSV()
        
        self.canvas.delete(load)
        
    # handles clicks from button 1
    def handleButton1(self, event):
        print 'handle button 1: %d %d' % (event.x, event.y)
        self.baseClick = [event.x, event.y]

    # handles clicks from button 2
    def handleButton2(self, event):
        print 'handle button 2: %d %d' % (event.x, event.y)
        self.baseClick2 = [event.x, event.y]
        #self.drawShape(event.x,event.y)

    # handles clicks from button 3
    def handleButton3(self, event):
        print 'handle button 3: %d %d' % (event.x, event.y)
        self.baseClick3 = [event.x, event.y]

    # handles double clicks from button 1
    def handleDubButton1(self, event):
        print 'handle double click: %d %d' % (event.x, event.y) 
        
        #if data is open
        if type(self.dataSet) is not bool:
            
            # Placeholder for infobox to display:
            popup = False

            
            if self.histoMode:
                # Histogram, so loop through bin graphics
                for i,point in enumerate(self.binGfx):
                    curCoords = self.canvas.coords(point)
                    x0,x1 = curCoords[0],curCoords[2]
                    y0,y1 = curCoords[1],curCoords[3]
                    
                    if (x0 < event.x < x1) and (y0 < event.y < y1):
                        # Get histogram data sorted by date:
                        histData = sorted(self.bins.iterkeys())
                        
                        # Extract date and hits
                        date = histData[i]
                        hits = self.bins[date]
                        date = str(date)
                        year = date[0:4]
                        month = date[4:6]
                        day = date[6:8]
                        popup = str(year)+"/"+str(month)+"/"+str(day)+"\nHits: "+str(hits)
                        break
            
            else:
                # 2D or 3D, so loop through data points
                for i,point in enumerate(self.dataGfx):
                    curCoords = self.canvas.coords(point)
                    dx = event.x - curCoords[0]
                    dy = event.y - curCoords[1]
                    if (dx < 10 and dx > -1 and dy < 10 and dy > -1):
                        #get point's data
                        info = self.dataM[i]
                        x = info[0,0]
                        y = info[0,1]
                        z = info[0,2]
                        popup = 'X-axis: '+str(x)+'\n'
                        popup += 'Y-axis: ' + str(y) +'\n'
                        popup += 'Z-axis: ' +str(z)
                        break
            
            # Check if a point was found
            if popup:            
                tkMessageBox.showinfo(title='Selected Data Point', message=popup)
    
    # handles motion while button 1 held down
    def handleButton1Motion(self, event):
        
        if not self.histoMode:
            ########## Errors here if baseClick doesn't exist
            ########## DisplayApp instance has no attribute 'baseClick'
            diff = [ event.x - self.baseClick[0], event.y - self.baseClick[1] ]
            print 'handle button1  motion %d %d' % (diff[0], diff[1])
            self.baseClick = [ event.x, event.y ]
    
            #Divide the differential motion (dx, dy) by the screen size 
            #(view X, view Y) & multiply by the extents
            delta0 = 1.0 * diff[0]*self.vRef.extent[0] / self.vRef.view[0]
            delta1 = 1.0 * diff[1]*self.vRef.extent[1] / self.vRef.view[1]
            
            # Update the VRP
            dvrpx = delta0 * self.vRef.u.item(0) + delta1 * self.vRef.vup.item(0)
            
            # for some reason, it thinks vup and u only have one element...
            dvrpy = delta0 * self.vRef.u.item(1) + delta1 * self.vRef.vup.item(1)
            
            theVRP = self.vRef.vrp.getA()
            theVRP[0][0] = self.vRef.vrp.item(0) + dvrpx
            theVRP[0][1] = self.vRef.vrp.item(1) + dvrpy
            
            #Call updateAxes()
            self.updateAxes()
            self.updateData()
    
    # handles motion while button 2 held down
    def handleButton2Motion(self,event):
        if self.rotationMode:
            diff = [ event.x - self.baseClick2[0], event.y - self.baseClick2[1] ]
            #self.baseClick2 = [ event.x, event.y ]
            
            #constant (200) is arbitrary (use half extent)
            delta0 = 1.0*diff[0]*math.pi / 10000
            delta1 = -1.0*diff[1]*math.pi / 10000
            
            self.vRef.rotateVRP(delta0, delta1)
            self.updateAxes()
            self.updateData()

    # handles motion while button 3 held down
    def handleButton3Motion(self,event):
        
        if not self.histoMode:
            diff = event.y - self.baseClick3[1]
            print 'handle button3  motion %d' % diff
            
            # Keep the scale factor between 0.1 and 3.0
            if diff < 0:
                if diff < -3:
                    diff = -3
                elif diff > -0.1:
                    diff = -0.1
            else:
                if diff > 3:
                    diff = 3
                elif diff < 0.1:
                    diff = 0.1
                    
            k = 0.01
            f = 1.0 + k*diff
            
            #Update the extent.
            self.vRef.extent[0] *= f
            self.vRef.extent[1] *= f
            self.vRef.extent[2] *= f
            
            self.updateAxes()
            self.updateData()

    # main TK loop
    def main(self):
        print 'Entering main loop'
        self.root.mainloop()

# execute the program if called from the command line
if __name__ == "__main__":
    dapp = DisplayApp(500, 500)
    dapp.main()