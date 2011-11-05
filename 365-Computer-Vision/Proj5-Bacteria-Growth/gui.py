import time
import cv

class SimpleGUI:
    def __init__(self):
        self.m_up, self.m_drag, self.m_done = map(None, range(3))
        # setup mouse state
        # up/drag/down, start point, and end points
        self.mState = [self.m_up, (0, 0), (0, 0)]
        
    def onMouse(self, event, x, y, flags, dontNeed):
        '''
        Handle mouse events, keeping track of left mouse click and drags to
        define axes for caliper measurements.
        '''
        if event == cv.CV_EVENT_LBUTTONDOWN:
            # begin mouse drag
            self.mState[0] = self.m_drag
            self.mState[1] = (x, y)
            self.mState[2] = (x, y)
            
        elif event == cv.CV_EVENT_MOUSEMOVE and self.mState[0] == self.m_drag:
            # mouse is being dragged
            self.mState[2] = (x, y)
            
        elif event == cv.CV_EVENT_LBUTTONUP and self.mState[0] == self.m_drag:
            # finished dragging mouse
            self.mState[0] = self.m_done
            self.mState[2] = (x, y)
        
        elif event == cv.CV_EVENT_MOUSEMOVE:
            # ignore mouse moves when not dragging
            pass
        else:
            # some other event; cancel dragging
            self.mState[0] = self.m_up

    def getCenters(self, cvImg):
        cv.ShowImage("original", cvImg)
        font = cv.InitFont(cv.CV_FONT_HERSHEY_DUPLEX, 1.0, 1.0)
        fontColor = (0,0,255)
        cv.NamedWindow("thresh")
        
        # register mouse function, passing in mouse state list
        cv.SetMouseCallback("thresh", self.onMouse, self.mState)
        
        # Keep track of centers
        centers = []
        # Keep track of previous mouse state
        prevMouseState = -1
        key = -1
        prevImg = False
        alreadyUndid = False
        while key != 27:
            
            # display
            cv.ShowImage("thresh", cvImg)
            key = cv.WaitKey(5)
            if self.mState[0] == 2:
                if prevMouseState == 1:
                    
                    # Keep track of previous image in case of undo
                    prevImg = cv.CreateImage(cv.GetSize(cvImg), cvImg.depth,\
                                             cvImg.nChannels)
                    cv.Copy(cvImg, prevImg)
                    print "CENTER:",self.mState[2]
                    x = self.mState[2][0]
                    y = self.mState[2][1]
                    centers.append((y,x))
                    
                    # Draw center label onto image (offset by a bit)
                    cv.PutText(cvImg, str(len(centers)), (x-8,y+8), font,\
                               fontColor)
                    
                    if alreadyUndid:
                        alreadyUndid = False
                    
            if (key == 65288 or key == 65535) and prevImg and not alreadyUndid:
                # If pressed "Backspace" or "Delete"
                cvImg = prevImg
                centers.pop()
                
                # Can only undo once
                alreadyUndid = True
                
            prevMouseState = self.mState[0]
            
        cv.DestroyAllWindows()
        return centers