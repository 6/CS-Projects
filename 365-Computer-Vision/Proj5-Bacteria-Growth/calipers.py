'''
Created on Mar 15, 2010

@author: bseastwo

Measures maximal distances along user-defined axes in binary images.  This is
essentially calipers applied to digital images.
'''

import cv
import cvnum
import numpy
import optparse

mouse_up, mouse_drag, mouse_done = map(None, range(3))

def calipers(image, axis):
    '''
    Measures width of a binary mask along an axis.  Like digital calipers.
    Returns the distance and position of the maximal pixels:
    (distance, (xmin, ymin), (xmax, ymax))
    '''
    # find positions of foreground pixels
    yy,xx = numpy.where(image > 0)
    
    # project along the axis
    xxyy = numpy.vstack((xx, yy)).transpose()
    distances = numpy.dot(xxyy, axis)
    
    # compute maximum distance
    maxidx = numpy.argmax(distances)
    minidx = numpy.argmin(distances)
    return (distances[maxidx] - distances[minidx], (xx[minidx], yy[minidx]), (xx[maxidx], yy[maxidx]))

def onMouse(event, x, y, flags, mouseState):
    '''
    Handle mouse events, keeping track of left mouse click and drags to define
    axes for caliper measurements.
    '''
    if event == cv.CV_EVENT_LBUTTONDOWN:
        # begin mouse drag
        mouseState[0] = mouse_drag
        mouseState[1] = (x, y)
        mouseState[2] = (x, y)
    elif event == cv.CV_EVENT_MOUSEMOVE and mouseState[0] == mouse_drag:
        # mouse is being dragged
        mouseState[2] = (x, y)
    elif event == cv.CV_EVENT_LBUTTONUP and mouseState[0] == mouse_drag:
        # finished dragging mouse
        mouseState[0] = mouse_done
        mouseState[2] = (x, y)
    elif event == cv.CV_EVENT_MOUSEMOVE:
        # ignore mouse moves when not dragging
        pass
    else:
        # some other event; cancel dragging
        mouseState[0] = mouse_up
        
if __name__ == "__main__":
    # parse parameters
    parser = optparse.OptionParser()
    parser.add_option("-f", "--file", help="image file to load")
    parser.add_option("-t", "--threshold", help="threshold level (10)", type="int", default=70)
    (options, values) = parser.parse_args()
    
    if options.file == None:
        print parser.print_help()
        exit()
    
    # load image
    cvImage = cv.LoadImage(options.file)
    npImage = numpy.float32(cvnum.cv2array(cvImage))
    
    # convert to luminance
    npImage = numpy.uint8(0.30 * npImage[:,:,2] + 0.59 * npImage[:,:,1] + 0.11 * npImage[:,:,0])
    
    # threshold
    thresh = numpy.copy(npImage)
    thresh[thresh < options.threshold] = 0
    
    # make a copy of threshold image for drawing
    cvThresh = cvnum.array2cv(numpy.dstack((thresh, thresh, thresh)))
    cvDraw = cv.CreateImage(cv.GetSize(cvThresh), cvThresh.depth, cvThresh.nChannels)
    
    # setup display
    cv.ShowImage("original", cvnum.array2cv(npImage))
    font = cv.InitFont(cv.CV_FONT_HERSHEY_TRIPLEX, 0.5, 0.5)
    message = "axis: [{0:4.2f}, {1:4.2f}]    distance: {2:6.3f}"
    cv.NamedWindow("thresh")
    
    # setup mouse state
    # up/drag/down, start point, and end points
    mouseState = [mouse_up, (0, 0), (0, 0)]
    middle = (cvThresh.width/2, cvThresh.height/2)
    
    # register mouse function, passing in mouse state list
    cv.SetMouseCallback("thresh", onMouse, mouseState)
    
    key = -1
    while key != 27:
        # make a fresh copy of the image each time
        cv.Copy(cvThresh, cvDraw)
        
        # get the current mouse state
        state, startXY, finalXY = mouseState[0], mouseState[1], mouseState[2]
        
        if state != mouse_up:
            # draw user's line
            cv.Line(cvDraw, startXY, finalXY, (0, 255, 0))
            
            # normalize axis
            axis = numpy.array((finalXY[0]-startXY[0], finalXY[1]-startXY[1]))
            norm = numpy.sqrt(numpy.dot(axis, axis))
            axis = axis / (norm + 1e-7)
            
            # find distance along axis
            (dist, p1, p2) = calipers(thresh, axis)
            
            # draw info on image
            cv.Circle(cvDraw, p1, 4, (0, 0, 255))
            cv.Circle(cvDraw, p2, 4, (0, 0, 255))
            cv.PutText(cvDraw, message.format(axis[0], axis[1], dist), (10, cvDraw.height-10), font, (0, 0, 255))
    
        # display
        cv.ShowImage("thresh", cvDraw)
        key = cv.WaitKey(30)