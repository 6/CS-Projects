import os,re,shelve
import cv,numpy

__version__ = "0.2"

# Display debug messages?
debugMode = True

''' ======= GENERAL FUNCTIONS ======= '''
class genFun:
    ''' Class with some useful general functions. '''
    
    def __init__(self):
        pass
    
    def t(self, var):
        ''' Returns variable type name (e.g. 'str', 'int') '''
        return type(var).__name__
    
    def debug(self, str):
        ''' A generic function to use for printing strings when debugging. '''
        
        if debugMode and self.t(str) == "str":
            print(str)
            
        elif not self.t(str) == "str":
            print "Error: Debug message not of type <str>"
    
    def getFiles(self, directory, ext=False):
        ''' Returns all files in directory, or all files with an extension
            in the param <list> 'ext'.'''
        
        files = []
    
        if self.t(directory) == "str":
            
            if (not ext or self.t(ext) == "list"):
                
                self.debug("Looking for files in "+directory)
                
                try:
                    allFiles = os.listdir(directory)
                    
                    # If need to check for extensions, compile regex
                    if ext:
                        regex = re.compile("[^\s]+(\.(?i)("+\
                                           '|'.join(map(str,ext))+\
                                           "))$")
                        
                    for file in allFiles:
                        
                        # If need to check for extensions, check for match
                        if ext:
                            match = re.match(regex,file)
                            if match:
                                files.append(file)
                            continue
                        
                        files.append(file)
                        
                    self.debug(str(len(files))+" files found.\n")
                                  
                except OSError:
                    self.debug("Error: Please specify a valid directory.")
            
            else:
                self.debug("Error: Extensions must be of type 'list'.")
                
        else:
            self.debug("Error: Please specify a valid directory type.")
        
        return files
    
    def fileExists(self, path):
        ''' Returns True if file exists, False if doesn't exist '''
        
        return os.path.isfile(path)
    
    def getShelve(self, pathToDB, writeBack=False):
        ''' Returns shelve db at location 'pathToDB'. Creates new
            shelve if doesn't exist yet.'''
        
        if not self.fileExists(pathToDB):
            self.debug("Creating new shelve at "+pathToDB)
            
        if writeBack:
            return shelve.open(pathToDB, writeback=True)
            
        return shelve.open(pathToDB)


''' ========= IMAGE-SPECIFIC FUNCTIONS ========= '''
class imgFun:
    ''' Class with some useful image-specific functions. Meant to be used
        with OpenCV or NumPy. '''
    
    def __init__(self):
        self.g = genFun()
    
    def getImgFiles(self, directory):
        ''' Returns all image files in a specified directory.
            INPUT:  directory - specifies directory to look for images in.
            OUTPUT: <list> of image file names.
            - Note: GIF format seems to be unsupported by OpenCV, 
                    so it is not included. '''
        
        return self.g.getFiles(directory, ["bmp", "jpe?g", "tiff?", "png"])
    
    def type(self, img):
        ''' Returns type of image.
            INPUT:  img - either an OpenCV or NumPy image.
            OUTPUT: <str> 'cv' (OpenCV) or 'np' (NumPy)'''
            
        if self.g.t(img) == "iplimage":
            return "cv"
        
        elif self.g.t(img) == "ndarray":
            return "np"
        
        self.g.debug("Error: Unknown image type: "+self.g.t(img))
    
    def depth(self, img):
        ''' Returns dtype or IPL_DEPTH of NumPy or CV image '''
        
        if self.type(img) == "cv":
            return img.depth
        return str(img.dtype)
    
    def channels(self, img):
        ''' Returns the number of channels of 'img'. '''
        
        nChannels = 1
        if self.type(img) == "cv":
            nChannels = img.nChannels
        
        else:
            try:
                nChannels = img.shape[2]
            except:
                pass
        
        return nChannels
    
    def width(self, img):
        ''' Returns <int> width in pixels of self.img '''
        width = None
        if self.type(img) == "cv":
            width = img.width
        else:
            width = img.shape[1]
        return width
    
    def height(self, img):
        ''' Returns <int> height in pixels of self.img '''
        height = None
        if self.type(img) == "cv":
            height = img.height
        else:
            height = img.shape[0]
        return height
    
    def rows(self, img):
        return self.height(img)
    
    def cols(self, img):
        return self.width(img)
        
    def toNP(self, img):
        ''' Convert CV image to NumPy'''
        
        imgRet = img
        if self.type(img) == "cv":
            
            depth2dtype = {
                cv.IPL_DEPTH_8U : 'uint8',
                cv.IPL_DEPTH_8S : 'int8',
                cv.IPL_DEPTH_16U: 'uint16',
                cv.IPL_DEPTH_16S: 'int16',
                cv.IPL_DEPTH_32S: 'int32',
                cv.IPL_DEPTH_32F: 'float32',
                cv.IPL_DEPTH_64F: 'float64'
            }
            
            imgRet = numpy.fromstring(
                 img.tostring(),
                 dtype=depth2dtype[self.depth(img)],
                 count=self.width(img)*self.height(img)*self.channels(img))
            imgRet.shape=(self.height(img),self.width(img),self.channels(img))
            
        return imgRet
        
    def toCV(self, img):
        ''' Convert NumPy image to CV ''' 
        
        imgRet = img
        if self.type(img) == "np":
            
            # Take this line out?
            img = numpy.uint8(img)
            
            # CV doesn't support float128, int64, uint64, or uint32?
            notSupp = ["float128", "int64", "uint64", "uint32"]
            imgType = str(img.dtype)
            
            if imgType in notSupp:
                self.g.debug("Error: dtype "+imgType+" not supported in OpenCV")
                
                if imgType[0:1] == 'u':
                    self.g.debug("Converting to uint16")
                    img = numpy.uint16(img)
                    
                elif imgType[0:1] == 'f':
                    self.g.debug("Converting to float64")
                    img = numpy.float64(img)
                    
                else:
                    self.g.debug("Converting to int16")
                    img = numpy.int16(img)
        
            dtype2depth = {
                'uint8':   cv.IPL_DEPTH_8U,
                'int8':    cv.IPL_DEPTH_8S,
                'uint16':  cv.IPL_DEPTH_16U,
                'int16':   cv.IPL_DEPTH_16S,
                'int32':   cv.IPL_DEPTH_32S,
                'float32': cv.IPL_DEPTH_32F,
                'float64': cv.IPL_DEPTH_64F,
            }
            try:
                nChannels = img.shape[2]
            except:
                nChannels = 1
            imgRet = cv.CreateImageHeader((self.width(img),self.height(img)), 
                                         dtype2depth[self.depth(img)],
                                         self.channels(img))
            cv.SetData(imgRet, img.tostring(), 
                       img.dtype.itemsize*self.channels(img)*self.width(img))
            
        return imgRet
        
    """def cvtDepth(self, img, newDepth):
        ''' Converts depth of 'img' to 'newDepth', and returns new image.'''
        
        '''validNPDepths = [numpy.uint8, numpy.int8, numpy.uint16, numpy.int16,
                         numpy.uint32, numpy.int32, numpy.uint64, numpy.int64,
                         numpy.float32, numpy.float64, numpy.float128]
        if newDepth in validNPDepths:'''
        if self.type(img) == "cv":
            
            newImg = cv.CreateImage((self.width(img), self.height(img)), 
                                 newDepth, 
                                 self.channels(img))
            cv.CvtScale(img, newImg)
            return newImg
        
        else:
            print "help"
    
    def makeVisible(self, img):
        ''' Converts image to depth/dtype of int8 '''
        
        if self.type(img) == "cv":
            img = self.cvtDepth(img, cv.IPL_DEPTH_8S)
        
        else:
            img = self.cvtDepth(img, numpy.int8)
        
        return img"""
            
    def load(self, imgpath):
        ''' Loads cv image from 'imgpath' file. '''
        return cv.LoadImage(imgpath)
    
    def save(self, img, imgName):
        ''' Saves 'img' to a file '''
        img = self.toCV(img)
        cv.SaveImage(imgName,img)
        
    def show(self, img, title="Image"):
        ''' Displays 'img' to the screen'''
        img = self.toCV(img)
        cv.ShowImage(title,img)
        cv.WaitKey(0)
    
    def toSingle(self, img, whichChannel=0):
        ''' Converts param 'img' to a single channel. Param 'whichChannel'
            specifies which channel to use (R = 0, G = 1, B = 2). '''
        
        # Convert image to NumPy if necessary
        if self.type(img) == "cv":
            img = self.toNP(img)
        #print "SHAPE1:",img.shape 
        # Only convert if image currently has 3 channels
        if len(img.shape) == 3:
            if img.shape[2] >= (whichChannel + 1):
                img = img[:,:,whichChannel]
        return img
    
    def toBinary(self, img, threshold, whichChannel):
        ''' Converts NumPy 'img' to binary form, where param 'threshold'
            specifies the threshold value for which anything greater than or
            or equal to this value will be 255. Everything else will be changed
            to 0. '''
        
        self.g.debug("Binary thresholding...")
        if self.type(img) == "cv":
            img = self.toNP(img)
        #print img.shape, img.max(), img.min()
        
        tempSingle = self.toSingle(img, whichChannel)
        img = numpy.where(tempSingle >= threshold,255,0)
        #print img.shape, img.max(), img.min()
        
        return img
    
    def cvtColor(self, img, newColorType, newChannels=False):
        ''' Converts 'img' to new CV color type. Returns new image. '''
        
        if self.type(img) == "np":
            img = self.toCV(img)
        
        if newChannels == False:
            newChannels = self.channels(img)
            
        newImg = cv.CreateImage((self.width(img), self.height(img)), 
                                self.depth(img), newChannels)
        cv.CvtColor(img, newImg, newColorType)
        
        return newImg
    
    def toGrayscale(self, img):
        ''' Converts 'img' to grayscale. Returns grayscale image. '''
        
        return self.cvtColor(img, cv.CV_BGR2GRAY, 1)
    
    def crop(self, img,top=0,right=0,bottom=0,left=0):
        ''' Crops self.img by X number of pixels from each param. 
            The order of params is 'top','right','bottom','left'.'''
        
        img = self.toNP(img)
        
        return img[top : self.height(img)-bottom, 
                   left : self.width(img)-right]
    
    def otsuThresh(self, img, whichChannel=0):
        ''' Returns <int> Otsu threshold level of self.img using the channel
            specified by param 'whichChannel'. Based roughly off of Java
            implementation found here:
            http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
            '''
        
        self.g.debug("Calculating Otsu threshold on channel "+str(whichChannel)+"...")
        thresh = 0
        pixRange = 256
        
        # Flatten image and convert to single channel to make processing faster
        tempImg = self.toSingle(img, whichChannel).flatten()
        totalPix = tempImg.shape[0]
        
        # Calculate the frequency of each bin in the histogram
        N,bins = numpy.histogram(tempImg, bins=numpy.arange(pixRange+1))
        
        # One extra index bin appears. Remove it so N.shape == bins.shape
        bins = numpy.delete(bins,[256])
        
        # Calculate the sum of freq*bin value (is this just the dot product?)
        sum = (N*bins).sum()
        
        sumB = 0.0
        wB = 0
        wF = 0
        varMax = 0.0
        
        for t in range(256):
            wB += N[t] # Weight background
            if wB == 0:
                continue
            wF = totalPix - wB # Weight foreground
            if wF == 0:
                break
            
            sumB += t * N[t]
            mB = sumB / wB # Mean background
            mF = (sum - sumB) / wF # Mean foreground
            
            # Calculate between class variance
            varBtwn = (wB * wF)*(mB - mF)*(mB - mF)
            
            # Check if new maximum found
            if varBtwn > varMax:
                varMax = varBtwn
                thresh = t
        
        self.g.debug("Threshold determined to be "+str(thresh))
        return thresh
    
    """def resize(self, img, newDim):
     ''' Resizes an image so that smallest dimension is  param 'newDim'.'''
        img = self.toCV(img)
        imgSize = self.sizeCV()
        width = float(self.width(img))
        height = float(self.height(img))
        if width > height:
            # resize height to newDim
            newHeight = newDim
            newWidth = (newDim/height) * width
        else:
            #resize width to newDim
            newWidth = newDim
            newHeight = (newDim/width) * height
        newSize = (int(newWidth),int(newHeight))
        dest = cv.CreateImage(newSize, self.depth(img), self.channels(img))
        cv.Resize(img,dest)
        return dest"""