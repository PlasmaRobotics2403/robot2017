#Build room exposure_a=4, gain=5


# import the necessary packages
from collections import deque
import numpy as np
import argparse
import imutils
import cv2
import time
import numpy

import pygame
from pygame.locals import *



def WindowStatusBar(windowSurface):
	txtfont = pygame.font.SysFont("monospace", 12)
	text1=txtfont.render("Channel Edit: ["+adjHSV+"] Press [H][S][V] to edit channel. Press [Q] to exit" ,1,(233,233,233))
	windowSurface.blit(text1,(70,10))
	text1=txtfont.render("Use arrow keys <-> for low range and Up/Dn For Upper range. [M] to toggle mask only" ,1,(233,233,233))
	windowSurface.blit(text1,(70,25))
	text1=txtfont.render("Low range: ("+str(HLo) + ","+str(SLo)+","+str(VLo)+")  Upper Range: ("+str(HUp)+","+str(SUp)+","+str(VUp)+")", 1, (233,233,233))
	windowSurface.blit(text1,(70,39))
	pygame.display.flip()
	return







pygame.init()
WIDTH = 640
HEIGHT = 480
windowSurface = pygame.display.set_mode((WIDTH, HEIGHT), 0,32)
pygame.display.set_caption("HSVUtil")
pygame.font.init()
myfont = pygame.font.SysFont("monospace", 20)
plasmalogo = pygame.image.load("logoBlackPurpleSmall.jpg")

radius=0

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image",
	help="path to the (optional) image file")
ap.add_argument("-b", "--buffer", type=int, default=64,
	help="max buffer size")
args = vars(ap.parse_args())

# define the lower and upper boundaries of the "green"
# ball in the HSV color space, then initialize the
# list of tracked points
# GREEN BALLS  H=28-38 S=11-255 V=1-255
# Gold Gear     H=23-32 S

# v4l2-ctl --set-ctrl=gain=00
# v4l2-ctl --set-ctrl=exposure_auto=1
# v4l2-ctl --set-ctrl=exposure_absolute=10


greenLower = (55,80,18)      #(22,14,80)      #(29, 86, 6)
greenUpper = (90,175,25)     #(44,255,255)     #(64, 255, 255)
HLo=55
HUp=90
SLo=80
SUp=175
VLo=18
VUp=25

adjHSV = "H"
TimeToQuit=False
MaskMode=False

pts = deque(maxlen=args["buffer"])

# if a video path was not supplied, grab the reference
# to the webcam
if not args.get("image", False):
	camera = cv2.VideoCapture(0)

# otherwise, grab a reference to the video file
else:
	camera = cv2.VideoCapture()

#gain=0
#camera.set(cv2.CAP_PROP_GAIN,gain)

#brightness=150
#camera.set(cv2.CAP_PROP_BRIGHTNESS,brightness)

#contrast=255
#camera.set(cv2.CAP_PROP_CONTRAST,contrast)

#saturation=255
#camera.set(cv2.CAP_PROP_SATURATION,saturation)

exposure=15
camera.set(cv2.CAP_PROP_EXPOSURE,exposure)

# keep looping in the main loop
while True:
	if args.get("image",False):
		# load from photo
		frame = cv2.imread(args["image"])
	else:
        # grab the current frame
		(grabbed, frame) = camera.read()


	# resize the frame, blur it, and convert it to the HSV
	# color space
	frame = imutils.resize(frame, width=600)
	blurred = cv2.GaussianBlur(frame, (3, 3), 0)
#	hsvblurred = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

	# construct a mask for the color "green", then perform
	# a series of dilations and erosions to remove any small
	# blobs left in the mask
	#mask = cv2.inRange(hsv, greenLower, greenUpper)
	mask = cv2.inRange(hsv, (HLo,SLo,VLo), (HUp,SUp,VUp))
	mask = cv2.erode(mask, None, iterations=2)
	mask = cv2.dilate(mask, None, iterations=4)   #8 GETS RID OF HOLES

	#Use pygame to display our mask screem
	framemask=numpy.rot90(mask)
	img = pygame.surfarray.make_surface(framemask)
	windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
	pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
	windowSurface.blit(plasmalogo,(0,0))
	WindowStatusBar(windowSurface)
	time.sleep(.2)



	#now use shape detection for  shapes in the HSV color space
	mask2 = cv2.inRange(blurred, greenLower, greenUpper)
	resultframe = cv2.bitwise_and(frame, frame, mask= mask2)
	mask2 = cv2.dilate(mask2, None, iterations=1)

	# Setup SimpleBlobDetector parameters.
	params = cv2.SimpleBlobDetector_Params()

	# Change thresholds
	params.minThreshold = 0;
	params.maxThreshold = 256;

	# Filter by Area.
	params.filterByArea = True
	params.minArea = 900

	# Filter by Circularity
	params.filterByCircularity = True
	params.minCircularity = 0.7 #0.1

	# Filter by Convexity
	params.filterByConvexity = False
	params.minConvexity = 0.7

	# Filter by Inertia - elipses
	params.filterByInertia = False
	params.minInertiaRatio = 0.5

	# Create a detector with the parameters
	ver = (cv2.__version__).split('.')
	if int(ver[0]) < 3 :
	    detector = cv2.SimpleBlobDetector(params)
	else :
	    detector = cv2.SimpleBlobDetector_create(params)


	# Detect blobs.
	reversemask=255-mask
	keypoints = detector.detect(reversemask)

	# Draw detected blobs as red circles.
	# cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS ensures the size of the circle corresponds to the size of blob
	im_with_keypoints = cv2.drawKeypoints(frame, keypoints, np.array([]), (0,0,255), cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
	if MaskMode==False:
		#framemask=cv2.cvtColor(mask2,cv2.COLOR_HSV2RGB)
		framemask=numpy.rot90(im_with_keypoints)
		#framemask=numpy.rot90(mask)
		img = pygame.surfarray.make_surface(framemask)
		windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
		pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
		windowSurface.blit(plasmalogo,(0,0))
		WindowStatusBar(windowSurface)
		time.sleep(.5)


    #THIS SECTOION JUST FINDS THE LARGEST CONTOUR FOUND IN THE MASK,
	#A DIFFERENT ROUT AT FINDING OBJECTS THAN ABOVE
	# find contours in the mask and initialize the current
	# (x, y) center of the ball
	cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
	cv2.CHAIN_APPROX_SIMPLE)[-2]


	center = None

	# only proceed if at least one contour was found
	if len(cnts) > 0:
		for c in cnts:
			# find the largest contour in the mask, then use
			# it to compute the minimum enclosing circle and
			# centroid
			#c = max(cnts, key=cv2.contourArea)
			((x, y), radius) = cv2.minEnclosingCircle(c)
			M = cv2.moments(c)
			center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

			# only proceed if the radius meets a minimum size
			if radius > 10:
			    # draw the circle and centroid on the frame,
			    # then update the list of tracked points
			    cv2.circle(frame, (int(x), int(y)), int(radius),
			            (0, 255, 255), 2)
			    cv2.circle(frame, center, 5, (0, 0, 255), -1)



	# update the points queue
	pts.appendleft(center)

# loop over the set of tracked points
	for i in range(1, len(pts)):
		# if either of the tracked points are None, ignore
		# them
		if pts[i - 1] is None or pts[i] is None:
			continue

		# otherwise, compute the thickness of the line and
		# draw the connecting lines
		thickness = int(np.sqrt(args["buffer"] / float(i + 1)) * 2.5)
		#cv2.line(frame, pts[i - 1], pts[i], (0, 0, 255), thickness)

	# show the frame to our screen
	#cv2.imshow("Frame", frame)



	for event in pygame.event.get():
		if event.type==KEYDOWN:
			key = event.key
			print (key)
			if key == 276:
				if adjHSV=="H":
					HLo=HLo-1
				if adjHSV=="S":
					SLo=SLo-1
				if adjHSV=="V":
					VLo=VLo-1
			if key == 275:
				if adjHSV=="H":
					HLo=HLo+1
				if adjHSV=="S":
					SLo=SLo+1
				if adjHSV=="V":
					VLo=VLo+1
			if key == 273:
				if adjHSV=="H":
					HUp=HUp+1
				if adjHSV=="S":
					SUp=SUp+1
				if adjHSV=="V":
					VUp=VUp+1
			if key == 274:
			    if adjHSV=="H":
                                    HUp=HUp-1
			    if adjHSV=="S":
                                    SUp=SUp-1
			    if adjHSV=="V":
                                    VUp=VUp-1
			if key == 104:
				adjHSV="H"
			if key == 115:
				adjHSV = "S"
			if key == 118:
				adjHSV = "V"
			if key == 109:
				MaskMode = not(MaskMode)
			if key == 113:
			    TimeToQuit=True
			    break
	if HUp > 255:
		HUp = 255
	if HUp < 0:
		HUp = 0
	if HLo > 255:
		HLo = 255
	if HLo < 0:
		HLo = 0
	if SUp > 255:
		SUp = 255
	if SUp < 0:
		SUp = 0
	if SLo > 255:
		SLo = 255
	if SLo < 0:
		SLo = 0
	if VUp > 255:
		VUp = 255
	if VUp < 0:
		VUp = 0
	if VLo > 255:
		VLo = 255
	if VLo < 0:
		VLo = 0

	if MaskMode==False:
		frame=cv2.cvtColor(im_with_keypoints,cv2.COLOR_BGR2RGB)
		frame=numpy.rot90(frame)
		img = pygame.surfarray.make_surface(frame)
		windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
		pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
		windowSurface.blit(plasmalogo,(0,0))
		WindowStatusBar(windowSurface)
		#if radius > 5:
		#	textradius=myfont.render("Radius: {:.2f} px".format( radius) ,1,(233,233,233))
		#	windowSurface.blit(textradius,(70,10))
		#	textradius=myfont.render("Est Dist: "+str(int((127*4/(radius/78))/25.4)) + " in.", 1, (233,233,233))
		#	windowSurface.blit(textradius,(70,30))
		#	textradius=myfont.render("Hlow: "+str(HLo) + " SUpper:"+str(HUp), 1, (233,233,233))
		#	windowSurface.blit(textradius,(70,50))
		#	pygame.display.flip()

		time.sleep(.3)

	if TimeToQuit == True:
	    break

# cleanup the camera and close any open windows
camera.release()
cv2.destroyAllWindows()
pygame.quit()
sys.exit()
