# import the necessary packages
from collections import deque
import numpy as np
import argparse
import imutils
import cv2
import time
import numpy
import math
from networktables import NetworkTable #shooterAngle shooterDistance shooterVelocity
import os
import pygame
from pygame.locals import *

WIDTH = 640
HEIGHT = 480
debug = False


####################(0,0,0) (190,190,233)#################### 178,210,233

shootAngle = 72
towerHeight = 3.0734 #meters

radius=0
distAvg=0
realAngleNeeded=0

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--debug",
	help="Display realtime video output")
ap.add_argument("-v", "--video",
	help="Change video input")
ap.add_argument("-n", "--noNetworkTable",
	help="Don't feed to robot network table")
ap.add_argument("-hu", "--hsvhueupper",
	help="Edit Upper Hue (H) in HSV Code")
ap.add_argument("-su", "--hsvsaturationupper",
	help="Edit Upper Saturation (S) in HSV Code")
ap.add_argument("-vu", "--hsvvalueupper",
	help="Edit Upper Value (V) in HSV Code")
ap.add_argument("-hl", "--hsvhuelower",
	help="Edit Lower Hue (H) in HSV Code")
ap.add_argument("-sl", "--hsvsaturationlower",
	help="Edit Lower Saturation (S) in HSV Code")
ap.add_argument("-vl", "--hsvvaluelower",
	help="Edit Lower Value (V) in HSV Code")
args = vars(ap.parse_args())

if args.get("debug", False):
    debug=True
    pygame.init()
    windowSurface = pygame.display.set_mode((WIDTH, HEIGHT), 0,32)
    pygame.font.init()
    myfont = pygame.font.SysFont("monospace", 20)
    plasmalogo = pygame.image.load("logoBlackPurpleSmall.jpg")
    windowSurface = pygame.display.set_mode((WIDTH, HEIGHT), 0,32)
    pygame.font.init()
    myfont = pygame.font.SysFont("monospace", 20)
    plasmalogo = pygame.image.load("logoBlackPurpleSmall.jpg")

if not args.get("noNetworkTable", False):
        NetworkTable.setIPAddress('roborio-2403-FRC.local')
        NetworkTable.setClientMode()
        NetworkTable.setUpdateRate(0.01)
        NetworkTable.initialize()
        table = NetworkTable.getTable("vision")
        starttime=time.time()
        while not table.isConnected():
            if time.time()-starttime>1:
                print('ShooterVision is waiting for network tables')
                starttime=time.time()
                time.sleep(0.25)
            


# define the lower and upper boundaries of the "green"
# ball in the HSV color space, then initialize the
# list of tracked points
tapeHSVL=[0,1,2]
tapeHSVU=[0,1,2]

tapeHSVL[0] = 55
tapeHSVL[1] = 80
tapeHSVL[2] = 0
tapeHSVU[0] = 90
tapeHSVU[1] = 255
tapeHSVU[2] = 255



if args.get("hsvhuelower", 55):
        tapeHSVL[0] = int(args.get("hsvhuelower"))
if args.get("hsvsaturationlower", 80):
        tapeHSVL[1] = int(args.get("hsvsaturationlower"))
if args.get("hsvvaluelower", 0):
        tapeHSVL[2] = int(args.get("hsvvaluelower"))
if args.get("hsvhueupper", 90):
        tapeHSVU[0] = int(args.get("hsvhueupper"))
if args.get("hsvsaturationupper", 255):
        tapeHSVU[1] = int(args.get("hsvsaturationupper"))
if args.get("hsvvalueupper", 255):
        tapeHSVU[2] = int(args.get("hsvvalueupper"))



tapeHSVLower = (tapeHSVL[0], tapeHSVL[1], tapeHSVL[2])
tapeHSVUpper = (tapeHSVU[0], tapeHSVU[1], tapeHSVU[2])


# if a video path was not supplied, grab the reference
# to the webcam
if not args.get("video", False):
	camera = cv2.VideoCapture(0)

# otherwise, grab a reference to the video file
else:
	camera = cv2.VideoCapture(int(args.get("video")))
	print('Selected video: ' + args.get("video"))


#camera.set(cv2.CAP_PROP_FOURCC,cv2.CV_FOURCC('M','J','P','G'))
camera.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
                                         
minimumSize = 5
widthActualTape=406 #mm

starttime=time.time()
loopcnt=0
shootAngleAdjustment=0
shootRPMAdjustment=1
fuelPhotoAngle=0
lastFPS=0

# keep looping
while True:
    #frame = cv2.imread("tower_visual.jpg")
    loopcnt = loopcnt + 1
    if not args.get("noNetworkTable", False):
            photoAngle = table.getNumber('fuelPhotoAngle', 0)
            shootRPMAdjustment = table.getNumber('shootRPMAdjustment', 1)
            shootAngleAdjustment = table.getNumber('shootAngleAdjustment', 0)
    (grabbed, frame) = camera.read()
    #photoAngle = table.getNumber('photoAngle', 0)
    if args.get("video") and not grabbed:
        break
    # resize the frame, blur it, and convert it to the HSV
    # color space
    frame = imutils.resize(frame, width=640)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)


    # construct a mask for the retroreflective tape, then perform
    # a series of dilations and erosions to remove any small
    # blobs left in the mask
    mask = cv2.inRange(hsv, tapeHSVLower, tapeHSVUpper)
    mask, contours, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    total = 0

    if debug:
        os.system('clear')
        print("ShooterVision")
    #calculate FPS
    if time.time() - starttime > 1:
        print ("FPS: " +str(loopcnt))
        lastFPS=loopcnt
        loopcnt=0
        starttime=time.time()
    angleOff=0
    estDist=-1
    realDist=4.4
    shootVelocity=0
    v = -1
    rpm = -1
    flywheelEfficiency = 0.4

    if len(contours) > 1:

                contours = sorted(contours, key = cv2.contourArea, reverse = True)[:2]
                x0, y0, w0, h0 = cv2.boundingRect(contours[0])

                aspectRatio0 = w0/h0
                x1, y1, w1, h1 = cv2.boundingRect(contours[1])

                aspectRatio1 = w1/h1
                #x, y, w, h = cv2.boundingRect(contours[2])
                #print ("w = " + str(w) + ", h = " + str(h) + "x=" + str(x) + "y=" + str(y))





                #       Filter out noise by checking aspect ratio. Aspect ratio is w/h
                #       Top tape band should have a smaller aspect ratio than the bottom band
                if aspectRatio0 < aspectRatio1:         #tape is true

                        #Check to see if both contours are close to each other by y position
                        if y1-y0 < 100:

                                #Check to see if widths and heights aren't noise by size
                                if w0 > minimumSize and h0 > minimumSize and w1 > minimumSize and h1 > minimumSize:
                                        if debug:
                                                print('Top tape aspect ratio = ' + str(aspectRatio0))
                                                print ("w = " + str(w0) + ", h = " + str(h0) + " x=" + str(x0) + " y=" + str(y0))
                                                print('Bottom tape aspect ratio = ' + str(aspectRatio1))
                                                print ("w = " + str(w1) + ", h = " + str(h1) + " x=" + str(x1) + " y=" + str(y1))

                                        peri = cv2.arcLength(contours[0],True)
                                        approx = cv2.approxPolyDP(contours[0], 0.01 * peri, True)
                                        if debug:
                                            cv2.drawContours(frame, [approx], -1, (0,255,0), 2)
                                        peri = cv2.arcLength(contours[1],True)
                                        approx = cv2.approxPolyDP(contours[1], 0.01 * peri, True)
                                        #cv2.drawContours(frame, [approx], -1, (0,255,0), 2)
                                        approx = cv2.approxPolyDP(contours[0], 0.001 * peri, True)
                                        if debug:
                                            cv2.drawContours(frame, [approx], -1, (0,255,0), 2)
                                        x0, y0, w0, h0 = cv2.boundingRect(approx)
                                        if w0 < 3 or h0 < 3:
                                                break
                                        #(4.7 * height of object * camera pixel height)/(object height pixels * 3.5) / 25.4
                                        #(4.7 * width of object * camera pixel width)/(object width pixels * camera sensor width) / 25.4
                                        dist = 4.7*widthActualTape*640/(w0*6.22)/21.8
                                        #angle = math.asin(87/dist)#height of top band is 87 in
                                        if dist*dist < 3969:
                                                dist = 63
                                                realDist = 0
                                                print("Error: Can't calculate dist; width of tape not entirely found")
                                        else:
                                                realDist = math.sqrt((dist*dist)-((87-24)*(87-24)))/39.3701
                                                if debug:
                                                        print('Boiler Distance: ' + str(dist))
                                                        print('Real Distance: ' + str(realDist))
                                                #shootVelocity = (math.sqrt((9.81*realDist*realDist)/(-2*(3.0734-0.54-(math.tan(math.radians(shootAngle))*realDist)))))/(math.cos(math.radians(shootAngle)))
                                                #print('Velocity: ' + str(shootVelocity))
                                        #for velocity

                                                if realDist > 0:
                                                        for rpm in range(800,2500,50):
                                                                v = rpm * 0.0984 * 2 * math.pi / 60 * flywheelEfficiency
                                                                targetCntr = (w0)/2+x0
                                                                #print("Target Center:" + str(targetCntr))
                                                                offCntr = targetCntr - 320 + 60 + shootAngleAdjustment
                                                                #print("Is off center by: " + str(offCntr) + " pixels")
                                                                inchOffset = 15 * offCntr / w0
                                                                angleOff = math.degrees(math.atan(inchOffset/dist))
                                                                #print("Angle off: " + str(angleOff))
                                                                if debug:
                                                                        print('realDist = ' + str(realDist))
                                                                        print('v = ' + str(v) + ' rpm = ' + str(rpm))
                                                                        print("Target Center:" + str(targetCntr))
                                                                        print("Is off center by: " + str(offCntr) + " pixels")
                                                                        print("Angle off: " + str(angleOff))


                                                                if not args.get("noNetworkTable", False):
                                                                        realAngleNeeded = angleOff + photoAngle
                                                                if (0.007 * (realDist))/(.074*v*math.cos(math.radians(shootAngle))) >= 1:
                                                                        ##print("D'oh! Magic imaginary numbers broke it. You done did it well...")
                                                                        v = -1
                                                                        rpm = -1
                                                                else:
                                                                        yTest = (v * math.sin(math.radians(shootAngle))+0.074*9.81/0.007)*((realDist)/(v*math.cos(math.radians(shootAngle))))
                                                                        yTest = yTest + (0.074*0.074*9.81)/(0.007*0.007)*(math.log(1-(0.007 * (realDist))/(.074*v*math.cos(math.radians(shootAngle)))))+.54

                                                                        if yTest < towerHeight:
                                                                                ##print("D'oh... I'm too far away to score! Height below the boiler: " + str(towerHeight-yTest) + " and RPM = " + str(rpm))
                                                                                ##print("Angle off: " + str(angleOff))
                                                                                pass
                                                                        elif yTest > towerHeight + 1:
                                                                                ##print("D'oh... I'm too close to score! Height above the boiler: " + str(yTest-towerHeight) + " and RPM = " + str(rpm))
                                                                                ##print("Angle off: " + str(angleOff))
                                                                                pass
                                                                        else:
                                                                                ##print("Yay! I'm ready to shoot. Fire away when ready! Velocity = " + str(v) + " and RPM = " + str(rpm))
                                                                                ##print("Angle off: " + str(angleOff))
                                                                                break

                #else:                  # else part of the loop


   
    #print ('photoAngle ' + str(photoAngle))
    #if not args.get("noNetworkTable", False):
            #print('GetPhotoAngle ' + str(table.getNumber('photoAngle', 0)))
            #pass
    

	
    if not args.get("noNetworkTable", False):
        table.putNumber('shooterAngle', angleOff)
        table.putNumber('fuelNeededAngle', realAngleNeeded)
        table.putNumber('shooterDistance', realDist)
        table.putNumber('shooterVelocity', (v * shootRPMAdjustment))
        table.putNumber('shooterRPM', (rpm * shootRPMAdjustment))
        table.putNumber('shooterPiTime', time.time())
        table.putNumber('shooterFPS', lastFPS)

    if debug:
        cv2.imwrite("debug-shootermask.jpg", mask)
        cv2.imwrite("debug-shooterframe.jpg", frame)
        frame=cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
        frame=numpy.rot90(frame, 3)
        frame=cv2.flip(frame, dst=None, flipCode=1)
        img = pygame.surfarray.make_surface(frame)
        windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
        pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
        windowSurface.blit(plasmalogo,(0,0))
        pygame.draw.line(windowSurface, (255, 255, 255), (320,60), (320, 480))
        pygame.display.flip()


    key = cv2.waitKey(1) & 0xFF

    # if the 'q' key is pressed, stop the loop
    if key == ord("q"):
        break

# cleanup the camera and close any open windows
camera.release()
cv2.destroyAllWindows()
pygame.quit()
exit()
