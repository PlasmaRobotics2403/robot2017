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
pygame.init()
WIDTH = 640
HEIGHT = 480
windowSurface = pygame.display.set_mode((WIDTH, HEIGHT), 0,32)
pygame.font.init()
myfont = pygame.font.SysFont("monospace", 20)
plasmalogo = pygame.image.load("logoBlackPurpleSmall.jpg")


####################(0,0,0) (190,190,233)#################### 178,210,233


radius=0

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-v", "--video",
	help="path to the (optional) video file")
ap.add_argument("-b", "--buffer", type=int, default=64,
	help="max buffer size")
args = vars(ap.parse_args())


# define the lower and upper boundaries of the "green"
# ball in the HSV color space, then initialize the
# list of tracked points
tapeHSVLower = (55,80,18)      #(29, 86, 6)
tapeHSVUpper = (90,175,25)     #(64, 255, 255)
pts = deque(maxlen=args["buffer"])
 
# if a video path was not supplied, grab the reference
# to the webcam
if not args.get("video", False):
	camera = cv2.VideoCapture(0)
 
# otherwise, grab a reference to the video file
else:
	camera = cv2.VideoCapture(args["video"])





# keep looping
while True:
	# grab the current frame
	(grabbed, frame) = camera.read()
 
	# if we are viewing a video and we did not grab a frame,
	# then we have reached the end of the video
	if args.get("video") and not grabbed:
		break
 
	# resize the frame, blur it, and convert it to the HSV
	# color space
	frame = imutils.resize(frame, width=600)
	#hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

	blurred = cv2.GaussianBlur(frame, (3, 3), 0)
	hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
 
	# construct a mask for the retroreflective tape, then perform
	# a series of dilations and erosions to remove any small
	# blobs left in the mask
	mask = cv2.inRange(hsv, tapeHSVLower, tapeHSVUpper)
	mask = cv2.erode(mask, None, iterations=2)
	mask = cv2.dilate(mask, None, iterations=2)   #8 GETS RID OF HOLES
	#maskBGR = cv2.cvtColor(mask, cv2.COLOR_HSV2BGR)
	#gray = cv2.cvtColor(mask, cv2.COLOR_BGR2GRAY)
	#mask = cv2.erode(mask, None, iterations=2)
	#mask = cv2.dilate(mask, None, iterations=2)
# find contours in the mask and initialize the current
	# (x, y) center of the ball
	#cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)[-2]
	#edged = cv2.Canny(mask, 10, 250)
	#kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (7,7))
	#closed = cv2.morphologyEx(edged, cv2.MORPH_CLOSE, kernel)
	mask, contours, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
	total = 0

	for c in contours:
                peri = cv2.arcLength(c,True)
                approx = cv2.approxPolyDP(c, 0.02 * peri, True)

                if len(approx) == 4:
                        cv2.drawContours(frame, [approx], -1, (0,255,0), 2)
                        total += 1
                        x, y, w, h = cv2.boundingRect(c)
                        print ("w = " + str(w) + ", h = " + str(h) + "Est Dist: " + str(int((127*2/(w/78)/13))))

	
	#cnt = contours[0]
	#x, y, w, h = cv2.boundingRect(cnt)
	#cv2.rectangle(frame, (x,y), (x+w, y+h), (0,255,0), 2)
	#cv2.drawContours(frame, contours, -1, (0,255,0), 3)



		# show the frame to our screen
	#cv2.imshow("Frame", frame)
	
	frame=cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
	frame=numpy.rot90(frame)
	img = pygame.surfarray.make_surface(frame)
	windowSurface.blit(img, (0, 0)) #Replace (0, 0) with desired coordinates
	pygame.draw.rect(windowSurface, (0,0,0) ,[0,0,640,60],0)
	windowSurface.blit(plasmalogo,(0,0))
	if radius > 5:
		textradius=myfont.render("Radius: {:.2f} px".format( radius) ,1,(233,233,233))
		windowSurface.blit(textradius,(70,10))
		
		textradius=myfont.render("Est Dist: "+str(int((127*4/(radius/78))/25.4)) + " in.", 1, (233,233,233))
		windowSurface.blit(textradius,(70,30))
	pygame.display.flip()


	key = cv2.waitKey(1) & 0xFF
 
	# if the 'q' key is pressed, stop the loop
	if key == ord("q"):
		break
	
# cleanup the camera and close any open windows
camera.release()
cv2.destroyAllWindows()
pygame.quit()
sys.exit()
