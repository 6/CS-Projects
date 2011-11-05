import numpy,cv,sys
import fun,gui
from PyQt4 import QtGui
g = fun.genFun()
i = fun.imgFun()


''' ======= CONFIG ======= '''
# Print out debug messages to cmd line
i.debugMode = True

# Directory where CBIR images are stored
imageDir = "../cbir/jpg/"

# File to store shelve database of histograms
dbPath = "imgdb_spatial4"

# Number of bins to use for color-space histograms
nBins = 8


''' ===== FUNCTIONS ===== '''

def hist(img, bins=8, min=0, max=255):
    ''' Compute histogram for an image in various color spaces.
        INPUT: img - image to compute histogram of
               bins - number of bins for histogram
               min - minimum of range
               max - maximum of range '''
    
    img = i.toNP(img)
    A = img[:,:,0].flatten() # R, X, or L
    B = img[:,:,1].flatten() # G, Y, or a
    C = img[:,:,2].flatten() # B, Z, or b
    data = numpy.vstack((A, B, C)).transpose()
    hist, edges = numpy.histogramdd(data, bins, range=[[min,max],
                                                       [min,max],
                                                       [min,max]])
    return hist

def computeColorHistograms(images, dbPath):
    ''' Create a program that loads all the images in the database and computes
    color histograms for each one.'''
    
    nImgs = float(len(images))
    
    # Open shelve database with writeback=True
    db = g.getShelve(dbPath, True)
    print "Computing histograms. This may take a few minutes...\n"
    
    # Loop through all images
    for idx,img in enumerate(images):
        
        # Load/convert image
        cvImg = i.load(imageDir + img)
        xyzImg = i.toNP(i.cvtColor(cvImg, cv.CV_BGR2XYZ))
        labImg = i.toNP(i.cvtColor(cvImg, cv.CV_BGR2Lab))
        
        # Split image into 3x3 sections
        div = 3
        npImg = i.toNP(cvImg)
        totalW = float(i.width(npImg))
        totalH = float(i.height(npImg))
        
        dW = totalW / div
        dH = totalH / div
        
        RGBHists,XYZHists,LabHists = False,False,False
        #RGBHists,XYZHists,LabHists = [],[],[]
        
        top = 0
        for row in range(1, div+1):
            left = 0
            for col in range(1, div+1):
                right = int(totalW - (col*dW))
                bottom = int(totalH - (row*dH))
                
                # Crop each image to correct section
                sectionRGB = i.crop(npImg, top, right, bottom,left)
                sectionXYZ = i.crop(xyzImg, top, right, bottom,left)
                sectionLab = i.crop(labImg, top, right, bottom,left)

                '''# Compute histograms for each section in various color spaces
                RGBHists.append(hist(sectionRGB, nBins))
                XYZHists.append(hist(sectionXYZ, nBins))
                LabHists.append(hist(sectionLab, nBins))'''
                
                # Concatenate histograms
                if top == 0 and left == 0:
                    RGBHists = hist(sectionRGB, nBins)
                    XYZHists = hist(sectionXYZ, nBins)
                    LabHists = hist(sectionLab, nBins)
                else:
                    RGBHists = numpy.concatenate((RGBHists, hist(sectionRGB, nBins)))
                    XYZHists = numpy.concatenate((XYZHists, hist(sectionXYZ, nBins)))
                    LabHists = numpy.concatenate((LabHists, hist(sectionLab, nBins)))
                left += int(dW)
            top += int(dH)
        
        # Store histograms in database
        db[img] = [RGBHists, XYZHists, LabHists]
        
        # Display percent done
        percentDone = int((idx/nImgs)*100)

        # Sync if necessary
        if (idx+1) % 50 == 0:
            print str(percentDone)+"%\t",\
              str(idx)+"/"+str(int(nImgs)),\
              "\nSyncing shelve database...\n"
            db.sync()
    
    # Sync one last time when done
    db.sync()

''' ======= RUN CODE ======= '''

# Get list of all images
images =  i.getImgFiles(imageDir)

# If database of color histograms doesn't exist yet
if not g.fileExists(dbPath):
    
    # Compute the histograms and save into database at 'dbPath'
    computeColorHistograms(images, dbPath)

# Start GUI
app = QtGui.QApplication(sys.argv)
gallery = gui.App(imageDir, images, dbPath)
gallery.show()
sys.exit(app.exec_())