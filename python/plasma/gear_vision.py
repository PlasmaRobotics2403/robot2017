# import the necessary packages
from collections import deque
import numpy as np
import argparse
import imutils
import cv2
import time
import numpy
import os
import math
from networktables import NetworkTable
import pygame
from pygame.locals import *

pygame.init()
WIDTH = 640
HEIGHT = 480



####################(0,0,0) (190,190,233)#################### 178,210,233

estDist=999
angleOff = 0
realAngleOff = 0
photoAngle = 0
debug=False

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--debug",
	help="Display realtime video output")
ap.add_argument("-n", "--noNetworkTable",
	help="Don't feed to robot network table")
args = vars(ap.parse_args())

if args.get("debug", False):
        windowSurface = pygame.display.set_mode((WIDTH, HEIGHT), 0,32)
        pygame.font.init()
        myfont = pygame.font.SysFont("monospace", 20)
        plasmalogo = pygame.image.load("logoBlackPurpleSmall.jpg")
        debug=True

if not args.get("noNetworkTable", False):
        NetworkTable.setIPAddress('10.24.3.2')
        NetworkTable.setClientMode()
        NetworkTable.setUpdateRate(0.01)
        NetworkTable.initialize()
        table = NetworkTable.getTable("vision")
        starttime=time.time()
        while not table.isConnected():
            if time.time()-starttime > 1:
                print('GearVision is waiting for network tables')
                starttime=time.time()
                time.sleep(0.25)


# define the lower and upper boundaries of the "green"
# ball in the HSV color space, then initialize the
# list of tracked points
tapeHSVLower = (55,80,0)      #(29, 86, 6)             (55,80,18)       (30,80,0)
tapeHSVUpper = (90,255,255)     #(64, 255, 255)          (90,175,55)   (110,255,255)

 
# if a video path was not supplied, grab the reference
# to the webcam
if not args.get("video", False):
	camera = cv2.VideoCapture(0)
 
# otherwise, grab a reference to the webcam number passed as arg i.e. 1
else:
	camera = cv2.VideoCapture(args["video"])

#Set Camera to 640x480
camera.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

starttime=time.time()
loopcnt=0
lastFPS=0

# main - keep looping
while True:
        loopcnt = loopcnt + 1
        # grab the current angle from RIO and frame from camera
        if not args.get("noNetworkTable", False):
                photoAngle = table.getNumber('gearPhotoAngle', 0)
        (grabbed, frame) = camera.read()
        frame=numpy.rot90(frame, 2)
 
        # if we are viewing a video and we did not grab a frame,
        # then we have reached the end of the video
        if args.get("video") and not grabbed:
                break
 
	# resize the frame, blur it, and convert it to the HSV
	# color space
        frame = imutils.resize(frame, width=640)
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)


        mask = cv2.inRange(hsv, tapeHSVLower, tapeHSVUpper)
        mask = cv2.erode(mask, None, iterations=1)

        mask, contours, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        total = 0
	
        if debug:
                os.system('clear')

        #calculate FPS 
        if time.time() - starttime > 1:
                print ("FPS: " +str(loopcnt))
                lastFPS=loopcnt
                loopcnt=0
                starttime=time.time()

        angleOff=0
        estDist=-1

        contours = sorted(contours, key = cv2.contourArea, reverse = True)[:2]


        for c in contours:
        	peri = cv2.arcLength(c,True)
        	approx = cv2.approxPolyDP(c, 0.02 * peri, True)
        	if len(approx) >= 4 and len(approx) <= 6:
        		x, y, w, h = cv2.boundingRect(c)
        		if h > 10 and w > 10:
                                if debug:
                                    cv2.drawContours(frame, [approx], -1, (0,255,0), 2)
                                total += 1
        			#x, y, w, h = cv2.boundingRect(c)
                                estDist = int((4.7*127*480)/(h*3.5)/25.4)
                                if debug:
                                        print ("w = " + str(w) + ", h = " + str(h) + " Est Dist: " + str(estDist)) #str(int((5*127*480)/(h*3.5)/25.4)))
        
        if debug:
                print ( "Number of Contours Found: " + str(len(contours)))
	#contours = sorted(contours, key = cv2.contourArea, reverse = True)[:2]

        if len(contours) >= 2:
                if debug:
                        cv2.imwrite("debug-gearmask.jpg", mask)
                        cv2.imwrite("debug-gearframe.jpg", frame)
                x, y, w, h = cv2.boundingRect(contours[0])
                x1, y1, w1, h1 = cv2.boundingRect(contours[1])
                #print ("w = " + str(w) + ", h = " + str(h) + "w1 = " + str(w1) + ", h1 = " + str(h1))
                if h > 10 and w > 10 and h1 > 10 and w1 > 10:
                        #print ("w = " + str(w) + ", h = " + str(h) + "w1 = " + str(w1) + ", h1 = " + str(h1))
                        print ("x = " + str(x) + " x1 = " + str(x1))
                        if x > x1:
                                xNew = x
                                wNew = w
                        else:
                                xNew = x1
                                wNew = w1
                        targetCntr = xNew
                        offCntr = float(targetCntr - 320)      #320
                        inchOffset = 2 * offCntr / float(wNew)
                        print('inchOffset = ' + str(inchOffset) + ' estDist = ' + str(estDist))
                        angleOff = -1 * math.degrees(math.atan(float(inchOffset)/float(estDist)))
                        print('angleOff = ' + str(angleOff))
                        if not args.get("noNetworkTable", False):
                                realAngleOff = photoAngle - angleOff
                                
                        if debug:
                                print("x=" + str(x) + "x1=" + str(x1))
                                print("Target Center:" + str(targetCntr))
                                print("Is off center by: " + str(offCntr))
                                print('Nic' + str(photoAngle))
                                print("Angle is off by: " + str(int(angleOff)) + " degrees")
                

        if not args.get("noNetworkTable", False):
                print("NEW Angle is off by: " + str(float(realAngleOff)) + " degrees")
                table.putValue('gearElevatorAngle', float(angleOff))
                table.putValue('test1', float(angleOff))
                table.putValue('gearNeededAngle', float(realAngleOff))
                table.putValue('gearDistance', float(estDist))
                table.putValue('gearFPS', int(lastFPS))
                #table.putValue('test', str('Yay'))


        if debug:
                frame=cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
                frame=numpy.rot90(frame, 3)
                frame=cv2.flip(frame, dst=None, flipCode=1)
                img = pygame.surfarray.make_surface(frame)
                windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
                pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
                windowSurface.blit(plasmalogo,(0,0))
                pygame.draw.line(windowSurface, (255, 255, 255), (320,60), (320, 480))
                pygame.draw.line(windowSurface, (75, 75, 75), (320+124,60), (320+124, 480))
                pygame.draw.line(windowSurface, (75, 75, 75), (320-124,60), (320-124, 480))
                pygame.display.flip()


        #key = cv2.waitKey(1) & 0xFF
 
	# if the 'q' key is pressed, stop the loop
        #if key == ord("q"):
        #	break
	
# cleanup the camera and close any open windows
camera.release()
cv2.destroyAllWindows()
pygame.quit()
sys.exit()
