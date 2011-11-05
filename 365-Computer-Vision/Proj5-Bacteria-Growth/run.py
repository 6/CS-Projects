import sys
import numpy, cv, pylab
from scipy import ndimage
import fun, gui, imgutils
fun.debugMode = False
g = fun.genFun()
i = fun.imgFun()
sgui = gui.SimpleGUI()

argv = sys.argv
if len(argv) < 7:
    print "Error: Please specify all parameters:"
    print "$ python run.py <imgDirectory> <firstImgIndex> <tolerance> <areas.csv> <rates.csv> <petriDiam> <scale>"
    print "For example:\n$ python run.py bact03bg/data/ 900 2 areas.csv 100 700"
    sys.exit(0)
options = argv[1:]

imageDir = options[0]
firstImgIdx = int(options[1])
tolerance = int(options[2])
outFile = options[3]
ratesOutFile = options[4]
petriDiam = float(options[5]) #100/50mm
scale = float(options[6]) / petriDiam #726.2?

# Directory where images are stored
#imageDir = "../img/bact03bg/data/"
#firstImgIdx = 870 + 49
#firstImgIdx = 846 + 29
#tolerance = 2

# Get a list of all images and sort them
images = i.getImgFiles(imageDir)
images.sort()
images = images[firstImgIdx:]
#print images

firstImg = images[0]
print firstImg
firstImgCV = i.load(imageDir+firstImg)
firstImg = numpy.int8(i.toNP(firstImgCV))
#i.show(firstImg)


# Determine threshold using Otsu's method (calculate only for first image)
channel = 0 # red channel works well for green images

# The approximate center points of each colony (selected by user?)
#centers = [[191, 664], [487, 731], [246, 377], [542, 443]]
#centers = [[226, 160], [192, 515], [198, 857], [557, 159], [554, 872]]

centers = sgui.getCenters(firstImgCV)

if len(centers) == 0:
    print "Please select centers"
    sys.exit(0)

print "Centers found at:",centers

# Array to hold all areas of regions
areas = numpy.zeros((len(images), len(centers)), numpy.float64)
# Time range
t = numpy.arange(0, areas.shape[0], 1)

# Interactive mode ON!
pylab.ion()
pylab.subplot(1,3,1)
pylab.xlabel('Time (minutes / 5)')
pylab.ylabel('Area (mm^2)')
pylab.title('Areas Over Time')
pylab.grid(True)
print "LEN:",len(images)
# Loop through each image
for idx in range(1,len(images)):
    
    print idx
    
    curImg = numpy.int8(i.toNP(i.load(imageDir+images[idx])))
   
    curImg = numpy.abs(curImg - firstImg)
    curImg = i.toCV(curImg)

    cv.Smooth(curImg, curImg, smoothtype=cv.CV_MEDIAN)

    # List to hold components (pixels in a colony)
    allComponents = []
    
    # Label value to start with
    label = 255
    
    regionImg = i.toNP(curImg)
    
    regionImg = regionImg[:,:,channel]
    
    # Loop through each colony
    for colony in range(len(centers)):
        #print "Colony:",colony
        center = centers[colony]
        row = center[0]
        col = center[1]
        mask, components = i.growRegions(regionImg, row, col, label, tolerance)
        
        # Decrement label to give each region separate label
        label -= 50
        regionImg = mask
        allComponents.append(components)
        areas[idx,colony] = len(components)
        
    
    regionImg[regionImg < label] = 0
    regionImg = numpy.int64(regionImg)
    
    binary = i.toBinary(regionImg, 1, 0)/255
    
    # Erode/Dilate filters
    edFilter  = numpy.array([[1,1,1,1,1], [1,1,1,1,1], [1,1,1,1,1], [1,1,1,1,1], [1,1,1,1,1]])
    #edFilter  = numpy.array([[1,1,1], [1,1,1], [1,1,1]])
    
    # Dilate image twice
    dilated = ndimage.filters.correlate(regionImg, edFilter)
    dilatedB = ndimage.filters.correlate(binary, edFilter)
    
    dilated /= dilatedB
    dilatedB = i.toBinary(dilated, 1, 0)/255
    
    dilated = ndimage.filters.correlate(dilated, edFilter)
    dilatedB = ndimage.filters.correlate(dilatedB, edFilter)
    
    dilated /= dilatedB
    dilatedB = i.toBinary(dilated, 1, 0)/255
    
    # Erode twice
    erosionMask = ndimage.filters.correlate(dilatedB, edFilter)
    erosionMask = i.toBinary(erosionMask, edFilter.sum(), 0)/255
    
    erosionMask = ndimage.filters.correlate(erosionMask, edFilter)
    erosionMask = i.toBinary(erosionMask, edFilter.sum(), 0)/255
    
    # Create dilation-erosion mask
    DEMask = erosionMask * dilated
    
    # Add areas to array
    label = 255
    pylab.subplot(1,3,1)
    pylab.clf()
    for colony in range(len(centers)):
        #print "Area:",len(numpy.where(DEMask == label)[0])
        areas[idx,colony] = len(numpy.where(DEMask == label)[0])/(scale**2)
        label -= 50
        
        s = areas[:,colony].transpose()
        pylab.plot(t, s, ".", label = "Region "+str(colony+1))
        
        pylab.draw()
        
    #i.save(DEMask, "de2_"+str(idx)+".jpg")
    #if idx == 350:
    #    break
    
#print areas

# Write all areas to CSV
g.toCSV(areas, outFile)

# Get rates of growth
middle = areas.shape[0]/3 #200

# Multiply SD by 3 to enlarge smoothing kernel --> less noise
stdDev = numpy.std(areas[middle:middle+20,:], 0)*3
rates = numpy.zeros_like(areas)
ratesSmoothed = numpy.zeros_like(areas)
for idx in range(len(centers)):
    
    #filter = numpy.array([[-3],[-2],[-1],[0],[1],[2],[3]])
    
    # Gaussian function to smooth rates
    #filter = 1/(numpy.sqrt(2*numpy.pi)*stdDev[idx])*numpy.e**(-filter**2 / (2*stdDev[idx]**2))
    filter = imgutils.gaussian(stdDev[idx], norm=True)[:,numpy.newaxis]
    print "FILTER:",filter
    rates[:,idx] = ndimage.filters.correlate(areas,filter)[:,idx]
    ratesSmoothed[:,idx] = rates[:,idx]
    rates[:,idx] = ndimage.filters.correlate(rates, numpy.array([[-1],[0],[1]]))[:,idx]
    
g.toCSV(rates, ratesOutFile)

# Show area plot


pylab.subplot(1,3,1)
pylab.xlabel('Time (minutes / 5)')
pylab.ylabel('Growth (mm^2)')
pylab.title('Growth Over Time')
for region in range(len(centers)):
    s = areas[:,region].transpose()
    pylab.plot(t, s, "-",label = "Region "+str(region+1))
    
pylab.legend()
pylab.savefig('areas_plot')



# Show smoothed rate plot
pylab.subplot(1,3,2)
for region in range(len(centers)):
    s = ratesSmoothed[:,region].transpose()
    pylab.plot(t, s, "-", label = "Region "+str(region+1))

pylab.legend()    
pylab.xlabel('Time (minutes / 5)')
pylab.ylabel('Growth (mm^2)')
pylab.title('Growth (Smoothed) Over Time')
pylab.grid(True)
pylab.savefig('areas_smoothed_plot')


# Show rate plot
pylab.subplot(1,3,3)
for region in range(len(centers)):
    s = rates[:,region].transpose()
    pylab.plot(t, s, ".", label = "Region "+str(region+1))

pylab.legend()    
pylab.xlabel('Time (minutes / 5)')
pylab.ylabel('Rate of growth (mm^2 / 10 minutes)')
pylab.title('Rates of Growth Over Time')
pylab.grid(True)
pylab.savefig('rates_plot')

pylab.show()
        