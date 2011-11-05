import numpy,Tkinter,tkSimpleDialog
tk = Tkinter
'''
Class for analyzing CS (and Communications?) log data.
'''
class DataSet:
    
    def __init__(self,data=False,metadata=False,debugMode=False):
        ''' Params:
                data <list> - a list of lists of data
                metadata <list> - optional way to specify metadata/column names
                debugMode <bool> - whether or not to display debug messages
        ''' 
        self.dbg = debugMode
        self.data = data
        self.meta = metadata
        if self.data and self.meta:
            self.matrix = numpy.zeros((len(self.data),len(self.data[0])), numpy.uint64)
        self.nonNum = {} # dictionary for storing non-numerical data
        
    def debug(self,str):
        ''' Prints debug messages '''
        if self.dbg:
            print str
    
    def __makeDataNumeric(self):
        ''' Private function to convert non-numeric data to numeric, and encode
            the actual string in self.nonNum <dict> '''
        
        # The column number in which non-numeric data starts
        # FIX: Make this more generic?
        colNon = 7
        dictIdx = 0
        for i in xrange(len(self.data)):
            for j in xrange(colNon,len(self.data[0])):
                self.nonNum[dictIdx] = self.data[i][j]
                self.data[i][j] = dictIdx
                dictIdx += 1
    
    def toMatrix(self):
        ''' Adds list of lists data to the matrix. '''
        
        self.debug("Converting to NumPy matrix form...")
        self.__makeDataNumeric()
        
        for i in xrange(len(self.data)):
            for j in xrange(len(self.data[0])):
                self.matrix[i,j] = self.data[i][j]
        
        self.debug("Finished converting to NumPy matrix.")
        
    def toCSV(self,fname="data.csv",delimiter=","):
        ''' Write data to a CSV file '''
        
        self.debug("Writing data to CSV file...")
        file = open(fname,'w')
        
        # Write column names on first line
        cols = []
        for col in self.meta:
            cols.append(col[0])
        file.write(delimiter.join(map(str,cols))+"\n")
        
        # Write data, unencoding when necessary
        for i in xrange(len(self.data)):
            toPrint = []
            for j in xrange(len(self.data[0])):
                curData = self.data[i][j]
                if self.meta[j][1]["dict"]:
                    key = int(curData)
                    curData = self.nonNum[key]
                toPrint.append(curData)
            file.write(delimiter.join(map(str,toPrint))+"\n")
            
        file.close()
        self.debug("Finished writing data.")
        
    def toCMD(self):
        ''' Prints data out to command line. '''
        
        for i in xrange(len(self.data)):
            toPrint = []
            for j in xrange(len(self.data[0])):
                curData = self.data[i][j]
                curMeta = self.meta[j][1]
                if curMeta["dict"]:
                    key = int(curData)
                    #print "d",key
                    curData = self.nonNum[key]
                elif curMeta["vals"]:
                    key = int(curData)
                    #print "m",key,curMeta
                    curData = curMeta["vals"][key]
                toPrint.append(curData)
            print "Row "+str(i+1)+": "+', '.join(map(str,toPrint))
        
    def select(self,indices):
        ''' Takes in a list of indices and return the specified columns. 
            Max indices=5. Returns <list> of columns in <ndarray> format.'''
        
        if len(indices) > 5:
            self.debug("Error: There must be 5 or less indices. Truncating.")
            indices = indices[:,5]
        
        columns = []
        for colIdx in indices:
            columns.append(self.matrix[:,colIdx])
            
        return columns
    
    def median(self,col=None):
        ''' Calculates the mean value for a specified column. '''
        
        if col != None:
            ''' Returns the mean value for 
                the specified column (regardless of
                relevance). 
            
                @param col: column we want the mean of 
                @param lucre: the mean of the col'''
            
            # Select the column (since it returns a list, get only first list)
            column = self.select([col])
            
            return numpy.median(column)
            
        else:
            ''' Returns a list of the mean value for 
                each (relevant) column. 
                Relevant columns are: 
                    0 (IP), 2 (referrer), 4 (page size)
                
                @param mist: list of means  ''' 
            self.debug("Calculating means...")
            mist = []
            mist.append(self.median(0))
            mist.append(self.median(3))
            mist.append(self.median(5))
            self.debug("Finished calculating medians.")
            return mist
        
    def mean(self,col=None):   
        ''' Calculates the mean value for a specified column. '''
        
        if col != None:
            ''' Returns the mean value for 
                the specified column (regardless of
                relevance). 
            
                @param col: column we want the mean of 
                @param lucre: the mean of the col'''
            
            # Select the column (since it returns a list, get only first list)
            column = self.select([col])
            
            return numpy.average(column)
            
        else:
            ''' Returns a list of the mean value for 
                each (relevant) column. 
                Relevant columns are: 
                    0 (IP), 2 (referrer), 4 (page size)
                
                @param mist: list of means  ''' 
            self.debug("Calculating means...")
            mist = []
            mist.append(self.mean(0))
            mist.append(self.mean(3))
            mist.append(self.mean(5))
            self.debug("Finished calculating means.")
            return mist
    
    def stdev(self,col=None):
        ''' Same procedure as mean, 'cept we're gettin
            the standard deviation.''' 
            
        if col != None:
            # Select the column (since it returns a list, get only first list)
            column = self.select([col])
            
            return numpy.std(column)
            
        else:
            self.debug("Calculating stdevs...")
            mist = []
            mist.append(self.stdev(0))
            mist.append(self.stdev(3))
            mist.append(self.stdev(5))
            self.debug("Finished calculating stdevs.")      
            return mist  
    
    def range(self,col=None):
        ''' Returns a list of 2-element lists with the minimum and maximum
            values in each column; with an optional index, returns the range
            for a single column.'''
            
        if col != None:
            column = self.select([col])
            return [numpy.min(column), numpy.max(column)] 
        else:
            self.debug("Calculating ranges...")
            mist = []
            
            for i in range(len(self.data[0])):
                mist.append(self.range(i))
            self.debug("Finished calculating ranges.")
            return mist 
        
    def size(self):
        ''' Returns the number of data points.'''
        
        return self.matrix.shape[0]
    
    def cols(self):
        ''' Returns the number of variables in each data point.'''
        
        return self.matrix.shape[1]
    
    def point(self, row):
        ''' Takes in a row index and returns the data vector.'''
        
        #check if valid row value
        while (row < 0 or row > self.size()):
            ok = tkSimpleDialog.askinteger('Error: Invalid row', 'Specify a new row here:')
            if (ok != None):
                row = ok
            else:
                row = -1
        return self.matrix[row]
    
    def value(self, row,  col):
        ''' Takes in a row and column and returns the data value.'''
        
        #check if valid row value
        while (row < 0 or row >= self.size()):
            ok = tkSimpleDialog.askinteger('Error: Invalid row', 'Specify a new row here:')
            if (ok != None):
                row = ok
            else:
                row = -1
        
        while (col < 0 or col >= self.cols()):
            ok = tkSimpleDialog.askinteger('Error: Invalid column', 'Specify a new column here:')
            if (ok != None):
                col = ok
            else:
                col = -1
        return self.matrix[row][col]

    def onCampusPercentage(self):
        '''ip percentages: on-campus!'''
        
        #count the number of ones we have in the first col.
        m = numpy.sort(self.select([0])[0])
        countOnes = m.searchsorted(1, side='right') - m.searchsorted(1, side='left')
        return 100.0*countOnes / self.size()
    
    def offCampusPercentage(self):
        '''ip percentages: off-campus!'''
        
        return 100.0 - self.onCampusPercentage()
    
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
        return browser
        
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
        return OS   