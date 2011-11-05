import sys, numpy
from PyQt4 import QtCore, QtGui
import fun
g = fun.genFun()
i = fun.imgFun()

class Dist():
    ''' Class of some distance metrics'''
    def __init__(self):
        pass
    
    def LIntersect(self, d1, d2):
        ''' Determines how similar two histograms are using histogram intersection.
            d1 and d2 are numpy arrays of same shape. Returns 1-distance. '''
        return 1 - (numpy.minimum(d1, d2).sum() / numpy.minimum(d1.sum(), d2.sum()))
    
    def L1Dist(self, d1, d2):
        ''' d1 and d2 are numpy arrays of same shape. Returns L1 distance'''
        return numpy.abs((d1 - d2)).sum()
    
    def L2Dist(self, d1, d2):
        ''' d1 and d2 are numpy arrays of same shape. Returns L2 distance'''
        return numpy.sqrt(( (d1 - d2)**2 ).sum())


''' Based off TrollTech (Qt) GPLed example code from:
http://google.com/codesearch/p?hl=en#-wKRNbYkeKI/pub/FreeBSD/distfiles
                            /PyQt-x11-gpl-4-snapshot-20070318.tar.gz|7rUjFyxwge8
                            /PyQt-x11-gpl-4-snapshot-20070318/examples/widgets/styles.py
'''

class App(QtGui.QWidget):
    def __init__(self, imageDir, imageList, dbPath, parent=None):
        QtGui.QWidget.__init__(self, parent)
        
        self.dist = Dist()
        
        self.imageDir = imageDir
        self.images = imageList
        self.imageFiles = {}
        self.color = "rgb"
        self.metr = self.dist.LIntersect
        self.searchImage = False
        
        self.nImgs = float(len(self.images))
        if not g.fileExists(dbPath):
            print "Error: Image database doesn't exist yet."
            sys.exit()
        else:
            self.db = g.getShelve(dbPath)
        
        self.originalPalette = QtGui.QApplication.palette()

        self.createTopRightGroupBox()
        self.createBottomGroupBox()
        self.createProgressBar(self.nImgs)

        self.imageLabel = QtGui.QLabel()
        self.imageLabel.setBackgroundRole(QtGui.QPalette.Base)


        mainLayout = QtGui.QGridLayout()
        #mainLayout.addLayout(topLayout, 0, 0, 1, 2)
        level = 0
        #mainLayout.addWidget(self.topLeftGroupBox, level, 0)
        mainLayout.addWidget(self.imageLabel, level, 0)
        mainLayout.addWidget(self.topRightGroupBox, level, 1)
        #level += 1
        #mainLayout.addWidget(self.imageLabel, level, 0, 1, 2)
        level += 1
        mainLayout.addWidget(self.bottomGroupBox, level, 0, 1, 2)
        level += 1
        mainLayout.addWidget(self.progressBar, level, 0, 1, 2)
        mainLayout.setRowStretch(1, 1)
        #mainLayout.setRowStretch(2, 1)
        #mainLayout.setColumnStretch(0, 1)
        #mainLayout.setColumnStretch(1, 1)
        self.setLayout(mainLayout)

        self.setWindowTitle(self.tr("CBIR Image Search"))

    def printCol(self):
        print self.color
    def printDist(self):
        print self.metr
    
    # Change color space    
    def opt1(self): 
        self.color = "rgb"
        self.printCol()
    def opt2(self): 
        self.color = "xyz"
        self.printCol()
    def opt3(self): 
        self.color = "lab"
        self.printCol()
    
    # Change distance metric
    """def opt4(self): 
        self.metr = self.dist.LIntersect
        self.printDist()
    def opt5(self): 
        self.metr = self.dist.L1Dist
        self.printDist()
    def opt6(self): 
        self.metr = self.dist.L2Dist
        self.printDist()"""
            
    def setSearchImage(self,item):
        file = self.imageFiles[str(item)]
        image = QtGui.QImage(self.imageDir+file)
        if image.isNull():
                QtGui.QMessageBox.information(self, self.tr("Image Viewer"),
                                              self.tr("Can't load %1.").arg(file))
                return
        print "Selected:",file
        self.imageLabel.setPixmap(QtGui.QPixmap.fromImage(image))
        self.searchImage = file
    
    def findSimilarImages(self, maxReturn=10):
        '''- Modify your program to limit results by a constant match score threshold.
        Note: You may want to make this an option on the query function.
    - Compute the precision of the color histogram matching strategy for these
      queries. That is, what is the ratio of images in the result set that match
      the query image.'''
        if not self.searchImage:
                QtGui.QMessageBox.information(self, self.tr("Image Viewer"),
                                              self.tr("Please select an image."))
                return
        print "Searching with",self.searchImage
        imgHists = self.db[self.searchImage]
        histTypes = {'rgb':0,
                     'xyz':1,
                     'lab':2}
        hType = histTypes[self.color]
        dType = self.metr
        
        imgHists = imgHists[hType]
        
        scores = []
        for idx,img in enumerate(self.images):
            
            hists = self.db[img]
            curHists = hists[hType]
            
            # Compute difference for each histogram
            scores.append([dType(imgHists, curHists), img])
            self.advanceProgressBar(idx)
        scores.sort()
        
        # Move to 100% 
        self.progressBar.setValue(self.nImgs)
        
        # Remove first image (search image)
        scores = scores[1:maxReturn+1]
        print "Scores:",scores
        self.showResults(scores)
        
    def showResults(self, images):

        for idx,imgnfo in enumerate(images):
            print "Showing:",imgnfo[1]
            
            colors = {0: "00ff00",
                      1: "ffff00",
                      2: "ff0000"}
            
            col = 1
            # Check thresholds
            if self.color == "rgb":
                if imgnfo[0] > 0.57:
                    col = 2
                elif imgnfo[0] < 0.4:
                    col = 0
            if self.color == "xyz":
                if imgnfo[0] > 0.52:
                    col = 2
                elif imgnfo[0] < 0.42:
                    col = 0
            elif self.color == "lab":
                if imgnfo[0] > 0.52:
                    col = 2
                elif imgnfo[0] < 0.38:
                    col = 0
                    
            # Set border color
            self.res[idx].setStyleSheet("QLabel{border:4px solid #"+colors[col]+"}")
                
            img = QtGui.QImage(self.imageDir+imgnfo[1])
            self.res[idx].setPixmap(QtGui.QPixmap.fromImage(img))

            #self.res[idx].resize(0.2 * self.res[idx].pixmap().size())
        
    def advanceProgressBar(self, newVal):
        curVal = self.progressBar.value()
        maxVal = self.progressBar.maximum()
        self.progressBar.setValue(newVal + (maxVal - curVal) / 100)
    
    def createTopRightGroupBox(self):
        self.topRightGroupBox = QtGui.QGroupBox(self.tr("Select Search Image"))
        self.imgList = QtGui.QTreeWidget()
        self.imgList.headerItem().setText(0, self.imageDir)
        
        # Add images
        for img in self.images:
            pic = QtGui.QTreeWidgetItem([img])
            self.imageFiles[str(pic)] = img
            self.imgList.addTopLevelItem(pic)
        
        self.connect(self.imgList, 
                     QtCore.SIGNAL("itemClicked(QTreeWidgetItem *,int)"),
                     self.setSearchImage)
        
        # Allow sorting
        self.imgList.setSortingEnabled(True)
        self.imgList.setRootIsDecorated(True)
        
        defaultPushButton = QtGui.QPushButton(self.tr("Search"))
        defaultPushButton.setDefault(False)
        self.connect(defaultPushButton, QtCore.SIGNAL("clicked()"), 
                     self.findSimilarImages)

        layout = QtGui.QGridLayout()
        
        # Create radio buttons
        radioButton1 = QtGui.QRadioButton(self.tr("RGB"))
        radioButton2 = QtGui.QRadioButton(self.tr("CIE XYZ"))
        radioButton3 = QtGui.QRadioButton(self.tr("CIE Lab"))
        
        radioButton1.setChecked(True)
        
        
        # Connect commands to radio buttons
        self.connect(radioButton1, QtCore.SIGNAL("clicked()"), self.opt1)
        self.connect(radioButton2, QtCore.SIGNAL("clicked()"), self.opt2)
        self.connect(radioButton3, QtCore.SIGNAL("clicked()"), self.opt3)
        
        # Add widgets to layout
        layout.addWidget(self.imgList,0,0,1,3)
        
        layout.addWidget(radioButton1,1,0)
        layout.addWidget(radioButton2,2,0)
        layout.addWidget(radioButton3,3,0)
        
        layout.addWidget(defaultPushButton,4,0,1,3)
        self.topRightGroupBox.setLayout(layout)

    def createBottomGroupBox(self):
        self.bottomGroupBox = QtGui.QGroupBox(self.tr("Search Results"))
        
        layout = QtGui.QGridLayout()
        
        # Better way to do this?
        self.result1,self.result2,self.result3,self.result4,self.result5,\
        self.result6,self.result7,self.result8,self.result9,self.result10 = \
        QtGui.QLabel(),QtGui.QLabel(),QtGui.QLabel(),QtGui.QLabel(),\
        QtGui.QLabel(),QtGui.QLabel(),QtGui.QLabel(),QtGui.QLabel(),\
        QtGui.QLabel(),QtGui.QLabel()
        
        self.res = [self.result1, self.result2, self.result3, self.result4,
                    self.result5, self.result6, self.result7, self.result8,
                    self.result9, self.result10]
        row = 0
        newline = 5
        for idx,r in enumerate(self.res):
            r.setBackgroundRole(QtGui.QPalette.Base)
            r.setSizePolicy(QtGui.QSizePolicy.Ignored, QtGui.QSizePolicy.Ignored)
            r.setScaledContents(True)
            layout.addWidget(r, row, idx%newline)
            if (idx+1) % newline == 0:
                row += 1

        #layout.setRowStretch(5, 1)
        self.bottomGroupBox.setLayout(layout)

    def createProgressBar(self,max):
        self.progressBar = QtGui.QProgressBar()
        self.progressBar.setRange(0, max)
        self.progressBar.setValue(0)

if __name__ == "__main__":
    app = QtGui.QApplication(sys.argv)
    gallery = App()
    gallery.show()
    sys.exit(app.exec_())
