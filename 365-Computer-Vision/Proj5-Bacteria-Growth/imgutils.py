'''
Created on Apr 4, 2010

@author: bseastwo
'''
import cv
import cvnum
import numpy

def imageInfo(image, title="image"):
    '''
    Print image information.
    '''
    print title, image.shape, image.min(), image.max(), image.mean()
    
def ncc(img1, img2):
    '''
    Compute the normalized cross correlation for a pair of images.
    NCC is computed as follows:
    \mu = \frac{1}{N} \sum_{x=1}^N I(x)
    ncc(I_1, I_2) = \frac{(I_1 - \mu_1)(I_2 - \mu_2)}{\sqrt{\sum (I_1 - 
\mu_1)^2 \sum (I_2 - \mu_2)^2}}
   
    where all sums are over the image plane, and the two images I_1 and I_2
    have the same number of elements, N.
   
    If the supplied images have a different number of elements, returns -1.
    '''
    if (img1.size != img2.size):
        return -1
   
    I1 = img1 - img1.mean()
    I2 = img2 - img2.mean()
   
    correlation = (I1 * I2).sum()
    normalizer = numpy.sqrt((I1**2).sum() * (I2**2).sum())
   
    return correlation / normalizer


def normalize(image, range=(0,255), dtype=numpy.uint8):
    '''
    Linearly remap values in input data into range (0-255, by default).  
    Returns the dtype result of the normalization (numpy.uint8 by default).
    '''
    # find input and output range of data
    if isinstance(range, (int, float, long)):
        minOut, maxOut = 0., float(range)
    else:
        minOut, maxOut = float(range[0]), float(range[1])
    minIn, maxIn = image.min(), image.max()
    ratio = (maxOut - minOut) / (maxIn - minIn)
    
    # remap data
    output = (image - minIn) * ratio + minOut
    
    return output.astype(dtype)
    
def equalize(image, alpha=1.0):
    '''
    Apply histogram equalization to an image.  Returns the uint8 result of
    the equalization.
    '''
    # build histogram and cumulative distribution function
    hist = numpy.histogram(image, 256, (0, 255))
    cdist = numpy.cumsum(hist[0])
    cdist = (255.0 / image.size) * cdist
    
    # apply distribution function to image
    output = alpha * cdist[image] + (1-alpha) * image
    return numpy.uint8(output)

def gaussian(sigma, radius=0, norm=False):
    '''
    Computes the values of a 1D Gaussian function with standard deviation
    sigma.  The number of values returned is 2*radius + 1.  If radius is 0, 
    an appropriate radius is chosen to include at least 98% of the Gaussian.  
    If norm is True, the Gaussian function values sum to 1.
    
    returns a (2*radius+1)-element numpy array
    '''
    sigma = float(sigma)
    
    # choose an appropriate radius if one is not given; 98% of Gaussian is
    # within 5 sigma of the center.
    if radius == 0:
        radius = numpy.floor(sigma * 5.0/2) 
        
    # compute Gaussian values
    range = numpy.arange(-radius, radius + 1)
    denom = 1 / (2 * (sigma ** 2))
    data = numpy.exp(-denom * (range ** 2))
    
    # normalize
    if norm:
        scale = 1 / (sigma * numpy.sqrt(2 * numpy.pi))
        data = scale * data
        
    return data
    
if __name__ == "__main__":
    
    size = 1024
    mag = 256
    I1 = mag * numpy.random.rand(size, size)
    I1n = I1 + 0.15 * mag * numpy.random.randn(size, size)
    I2 = mag * numpy.random.rand(size, size)
   
    print "ncc(I1, I1) =", ncc(I1, I1)
    print "ncc(I1, I2) =", ncc(I1, I2)
    print "ncc(I2, inv(I2)) =", ncc(I2, mag - I2)
    print "ncc(I1, I1 + N(0, .15) =", ncc(I1, I1n)
    print "ncc(I1, I1n + 4) =", ncc(I1, I1n + mag/2)
    print "ncc(I1, I1n * 4) =", ncc(I1, I1n * mag/2)
    quit()
    
    import pylab

    input = cv.LoadImage("grayscale.tif")
    npInput = cvnum.cv2array(input)[:,:,0]
    print "Input: [%d, %d]" % (npInput.min(), npInput.max())
    cv.ShowImage("input", input)
    
    normImg = normalize(npInput)
    print "Norm:  [%d, %d]" % (normImg.min(), normImg.max())
    cv.ShowImage("normalized", cvnum.array2cv(normImg))
    
    eqImg = equalize(npInput, 0.7)
    print "Equal: [%d, %d]" % (eqImg.min(), eqImg.max())
    cv.ShowImage("equalized", cvnum.array2cv(eqImg))
    
    pylab.ion()
    pylab.subplot(3,1,1)
    pylab.hist(npInput.flatten(), 256, (0, 255))
    pylab.title("Input")
    pylab.subplot(3,1,2)
    pylab.hist(normImg.flatten(), 256, (0, 255))
    pylab.title("Normalized")
    pylab.subplot(3,1,3)
    pylab.hist(eqImg.flatten(), 256, (0, 255))
    pylab.title("Equalized, a = 0.8")
    
    while 27 != cv.WaitKey(40):
        pylab.draw()
    
    pylab.ioff() 