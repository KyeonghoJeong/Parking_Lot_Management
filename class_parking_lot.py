from socket import *

import RPi.GPIO as GPIO
import time

import cv2
import numpy as np
import matplotlib.pyplot as plt
import pytesseract

from datetime import datetime

#from pygame import mixer
from pydub import AudioSegment
from pydub.playback import play

from collections import Counter

import sys

import subprocess
import threading

# parking lot client class
class parking_lot:
    def __init__(self, trig, echo, red, green, camera, number):
        # each parking lot has different gpio pins, camera, parking lot number and some methods use same variable such as inforamtion, result_chars and parking_lot 
        self.trig = trig
        self.echo = echo

        self.red = red
        self.green = green

        self.camera = camera

        self.number = number

        self.information = ["", "", ""]

        self.result_chars = ''

        self.parking_lot

    def license_plate_recognition(self, number):
        img = cv2.imread('/home/pi/parking_lot_'+self.information[0]+'/'+str(number)+'.jpg')

        height, width, channel = img.shape

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        structuringElement = cv2.getStructuringElement(cv2.MORPH_RECT, (3,3))

        imgTopHat = cv2.morphologyEx(gray, cv2.MORPH_TOPHAT, structuringElement)
        imgBlackHat = cv2.morphologyEx(gray, cv2.MORPH_BLACKHAT, structuringElement)

        imgGrayscalePlusTopHat = cv2.add(gray, imgTopHat)
        gray = cv2.subtract(imgGrayscalePlusTopHat, imgBlackHat)

        img_blurred = cv2.GaussianBlur(gray, ksize = (5, 5), sigmaX = 0)

        img_thresh = cv2.adaptiveThreshold(
            img_blurred,
            maxValue = 255.0,
            adaptiveMethod = cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
            thresholdType = cv2.THRESH_BINARY_INV,
            blockSize = 19,
            C = 9
        )

        _, contours, _ = cv2.findContours(
            img_thresh,
            mode = cv2.RETR_LIST,
            method = cv2.CHAIN_APPROX_SIMPLE
        )

        temp_result = np.zeros((height, width, channel), dtype = np.uint8)

        cv2.drawContours(temp_result, contours = contours, contourIdx = -1, color = (255, 255, 255))

        temp_result = np.zeros((height, width, channel), dtype = np.uint8)

        contours_dict = []

        for contour in contours:
            x, y, w, h = cv2.boundingRect(contour)
            cv2.rectangle(temp_result, pt1 = (x, y), pt2 = (x + w, y + h), color = (255, 255, 255), thickness = 2)

            contours_dict.append({
                'contour': contour,
                'x': x,
                'y': y,
                'w': w,
                'h': h,
                'cx': x + (w / 2),
                'cy': y + (h / 2)
            })

        MIN_AREA = 80
        MIN_WIDTH, MIN_HEIGHT = 2, 8
        MIN_RATIO, MAX_RATIO = 0.25, 1.0

        possible_contours = []

        cnt = 0
        for d in contours_dict:
            area = d['w'] * d['h']
            ratio = d['w'] / d['h']
        
            if area > MIN_AREA \
            and d['w'] > MIN_WIDTH and d['h'] > MIN_HEIGHT \
            and MIN_RATIO < ratio < MAX_RATIO:
                d['idx'] = cnt
                cnt += 1
                possible_contours.append(d)
            
        temp_result = np.zeros((height, width, channel), dtype=np.uint8)

        for d in possible_contours:

            cv2.rectangle(temp_result, pt1=(d['x'], d['y']), pt2=(d['x']+d['w'], d['y']+d['h']), color=(255, 255, 255), thickness=2)

        MAX_DIAG_MULTIPLYER = 5
        MAX_ANGLE_DIFF = 12.0
        MAX_AREA_DIFF = 0.5
        MAX_WIDTH_DIFF = 0.8
        MAX_HEIGHT_DIFF = 0.2
        MIN_N_MATCHED = 3

        def find_chars(contour_list):
            matched_result_idx = []
            
            for d1 in contour_list:
                matched_contours_idx = []
                for d2 in contour_list:
                    if d1['idx'] == d2['idx']:
                        continue

                    dx = abs(d1['cx'] - d2['cx'])
                    dy = abs(d1['cy'] - d2['cy'])

                    diagonal_length1 = np.sqrt(d1['w'] ** 2 + d1['h'] ** 2)

                    distance = np.linalg.norm(np.array([d1['cx'], d1['cy']]) - np.array([d2['cx'], d2['cy']]))
                    if dx == 0:
                        angle_diff = 90
                    else:
                        angle_diff = np.degrees(np.arctan(dy / dx))
                    area_diff = abs(d1['w'] * d1['h'] - d2['w'] * d2['h']) / (d1['w'] * d1['h'])
                    width_diff = abs(d1['w'] - d2['w']) / d1['w']
                    height_diff = abs(d1['h'] - d2['h']) / d1['h']

                    if distance < diagonal_length1 * MAX_DIAG_MULTIPLYER \
                    and angle_diff < MAX_ANGLE_DIFF and area_diff < MAX_AREA_DIFF \
                    and width_diff < MAX_WIDTH_DIFF and height_diff < MAX_HEIGHT_DIFF:
                        matched_contours_idx.append(d2['idx'])

                matched_contours_idx.append(d1['idx'])

                if len(matched_contours_idx) < MIN_N_MATCHED:
                    continue

                matched_result_idx.append(matched_contours_idx)

                unmatched_contour_idx = []
                for d4 in contour_list:
                    if d4['idx'] not in matched_contours_idx:
                        unmatched_contour_idx.append(d4['idx'])

                unmatched_contour = np.take(possible_contours, unmatched_contour_idx)

                recursive_contour_list = find_chars(unmatched_contour)
            
                for idx in recursive_contour_list:
                    matched_result_idx.append(idx)

                break

            return matched_result_idx
        
        result_idx = find_chars(possible_contours)

        if not result_idx:
            return "적재물"

        else:
            matched_result = []
            for idx_list in result_idx:
                matched_result.append(np.take(possible_contours, idx_list))

            temp_result = np.zeros((height, width, channel), dtype=np.uint8)

            for r in matched_result:
                for d in r:

                    cv2.rectangle(temp_result, pt1=(d['x'], d['y']), pt2=(d['x']+d['w'], d['y']+d['h']), color=(255, 255, 255), thickness=2)

            PLATE_WIDTH_PADDING = 1.3
            PLATE_HEIGHT_PADDING = 1.5
            MIN_PLATE_RATIO = 3
            MAX_PLATE_RATIO = 10

            plate_imgs = []
            plate_infos = []

            for i, matched_chars in enumerate(matched_result):
                sorted_chars = sorted(matched_chars, key=lambda x: x['cx'])

                plate_cx = (sorted_chars[0]['cx'] + sorted_chars[-1]['cx']) / 2
                plate_cy = (sorted_chars[0]['cy'] + sorted_chars[-1]['cy']) / 2
            
                plate_width = (sorted_chars[-1]['x'] + sorted_chars[-1]['w'] - sorted_chars[0]['x']) * PLATE_WIDTH_PADDING
            
                sum_height = 0
                for d in sorted_chars:
                    sum_height += d['h']

                plate_height = int(sum_height / len(sorted_chars) * PLATE_HEIGHT_PADDING)
            
                triangle_height = sorted_chars[-1]['cy'] - sorted_chars[0]['cy']
                triangle_hypotenus = np.linalg.norm(
                    np.array([sorted_chars[0]['cx'], sorted_chars[0]['cy']]) - 
                    np.array([sorted_chars[-1]['cx'], sorted_chars[-1]['cy']])
                )
            
                angle = np.degrees(np.arcsin(triangle_height / triangle_hypotenus))
            
                rotation_matrix = cv2.getRotationMatrix2D(center=(plate_cx, plate_cy), angle=angle, scale=1.0)
            
                img_rotated = cv2.warpAffine(img_thresh, M=rotation_matrix, dsize=(width, height))
            
                img_cropped = cv2.getRectSubPix(
                    img_rotated, 
                    patchSize=(int(plate_width), int(plate_height)), 
                    center=(int(plate_cx), int(plate_cy))
                )

                if img_cropped.shape[1] / img_cropped.shape[0] < MIN_PLATE_RATIO or img_cropped.shape[1] / img_cropped.shape[0] < MIN_PLATE_RATIO > MAX_PLATE_RATIO:
                    continue
            
                plate_imgs.append(img_cropped)
                plate_infos.append({
                    'x': int(plate_cx - plate_width / 2),
                    'y': int(plate_cy - plate_height / 2),
                    'w': int(plate_width),
                    'h': int(plate_height)
                })

            longest_idx, longest_text = -1, 0
            plate_chars = []

            for i, plate_img in enumerate(plate_imgs):
                plate_img = cv2.resize(plate_img, dsize=(0, 0), fx=1.6, fy=1.6)
                _, plate_img = cv2.threshold(plate_img, thresh=0.0, maxval=255.0, type=cv2.THRESH_BINARY | cv2.THRESH_OTSU)

                _, contours, _ = cv2.findContours(plate_img, mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_SIMPLE)
            
                plate_min_x, plate_min_y = plate_img.shape[1], plate_img.shape[0]
                plate_max_x, plate_max_y = 0, 0

                for contour in contours:
                    x, y, w, h = cv2.boundingRect(contour)
                
                    area = w * h
                    ratio = w / h

                    if area > MIN_AREA \
                    and w > MIN_WIDTH and h > MIN_HEIGHT \
                    and MIN_RATIO < ratio < MAX_RATIO:
                        if x < plate_min_x:
                            plate_min_x = x
                        if y < plate_min_y:
                            plate_min_y = y
                        if x + w > plate_max_x:
                            plate_max_x = x + w
                        if y + h > plate_max_y:
                            plate_max_y = y + h

                img_result = plate_img[plate_min_y:plate_max_y, plate_min_x:plate_max_x]

                img_result = cv2.GaussianBlur(img_result, ksize=(3, 3), sigmaX=0)
                _, img_result = cv2.threshold(img_result, thresh=0.0, maxval=255.0, type=cv2.THRESH_BINARY | cv2.THRESH_OTSU)
                img_result = cv2.copyMakeBorder(img_result, top=10, bottom=10, left=10, right=10, borderType=cv2.BORDER_CONSTANT, value=(0,0,0))

                chars = pytesseract.image_to_string(img_result, lang='kor', config='--psm 7 --oem 0')

                #global result_chars

                #가나다라마바
                #거너더러머버서어저
                #고노도로모보소오조
                #구누두루무부수우주
                #아바사자
                #배
                #하허호
            
                self.result_chars = ''
                has_digit = False
                for c in chars:
                    if ord('가') == ord(c) \
                       or ord('나') == ord(c) \
                       or ord('다') == ord(c) \
                       or ord('라') == ord(c) \
                       or ord('마') == ord(c) \
                       or ord('바') == ord(c) \
                       or ord('거') == ord(c) \
                       or ord('너') == ord(c) \
                       or ord('더') == ord(c) \
                       or ord('러') == ord(c) \
                       or ord('머') == ord(c) \
                       or ord('버') == ord(c) \
                       or ord('서') == ord(c) \
                       or ord('어') == ord(c) \
                       or ord('저') == ord(c) \
                       or ord('고') == ord(c) \
                       or ord('노') == ord(c) \
                       or ord('도') == ord(c) \
                       or ord('로') == ord(c) \
                       or ord('모') == ord(c) \
                       or ord('보') == ord(c) \
                       or ord('소') == ord(c) \
                       or ord('오') == ord(c) \
                       or ord('조') == ord(c) \
                       or ord('구') == ord(c) \
                       or ord('누') == ord(c) \
                       or ord('두') == ord(c) \
                       or ord('루') == ord(c) \
                       or ord('무') == ord(c) \
                       or ord('부') == ord(c) \
                       or ord('수') == ord(c) \
                       or ord('우') == ord(c) \
                       or ord('주') == ord(c) \
                       or ord('아') == ord(c) \
                       or ord('바') == ord(c) \
                       or ord('사') == ord(c) \
                       or ord('자') == ord(c) \
                       or ord('배') == ord(c) \
                       or ord('하') == ord(c) \
                       or ord('허') == ord(c) \
                       or ord('호') == ord(c) \
                       or c.isdigit():
                        if c.isdigit():
                            has_digit = True
                        self.result_chars += c

                plate_chars.append(self.result_chars)

                if has_digit and len(self.result_chars) > longest_text:
                    longest_idx = i
        
            return self.result_chars

    def ultrasonic_sensor(self):
        GPIO.setmode(GPIO.BCM) # bcm mode
        GPIO.setwarnings(False) # to do not show warning

        # trig and echo pin setting
        trig = self.trig 
        echo = self.echo

        maxTime = 0.04 # to prevent getting stucked

        GPIO.setup(trig, GPIO.OUT) # make trig pin output
        GPIO.setup(echo, GPIO.IN) # make echo pin input

        try:
            while True:          
                GPIO.output(trig, False) # low
                time.sleep(1)

                GPIO.output(trig, True) # high
                time.sleep(0.00001)
                    
                GPIO.output(trig, False) #low

                # To prevent for senser getting stucked, I use timeout and maxTime varibles and initialize pulse_start and pulse_end before while loop.
                pulse_start = time.time()
                timeout = pulse_start + maxTime

                # When sensor starts to get signal, echo turns to high from low and pulse_start get current time at that time.
                while GPIO.input(echo) == 0 and pulse_start < timeout:
                    pulse_start = time.time()

                pulse_end = time.time()
                timeout = pulse_end + maxTime

                # When sensor gets all signal, echo turns to low from high and pulse_end get current time at that time.
                while GPIO.input(echo) == 1 and pulse_end < timeout:
                    pulse_end = time.time()

                pulse_duration = pulse_end - pulse_start # time when all signals are back to sensor - time when first signal is back to sensor
                distance = pulse_duration * 17000 # velocity = distance / time so distance = time * velocity. time is pulse_duration / 2 and velocity is 340m/s so distance is pulse_duration * 17000.
                distance = round(distance, 2) # unit is cm and I use round method to round it
                #print(distance,"\n")

                return distance
        except:
            GPIO.cleanup()

    def led(self, action):
        # gpio setting is same with ultrasonic_sensor method.
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)

        red = self.red
        green = self.green

        GPIO.setup(red, GPIO.OUT)
        GPIO.setup(green, GPIO.OUT)

        try:
            if action == "green":
                GPIO.output(red, 1)
                GPIO.output(green, 0)
            elif action == "red":
                GPIO.output(red, 0)
                GPIO.output(green, 1)
            elif action == "off":
                GPIO.output(red, 1)
                GPIO.output(green, 1)
            elif action == "flash":
                t_end = time.time() + 10

                # to make LED flash for a specific time I use time.time() twice and their time difference. I turn on and off LED and add time intervals to control how fast LED flashes. 
                while(time.time() < t_end):
                    GPIO.output(red, 0)
                    GPIO.output(green, 1)

                    time.sleep(0.5)
                            
                    GPIO.output(red, 1)
                    GPIO.output(green, 1)

                    time.sleep(0.5)
        except:
            GPIO.cleanup()

    def photo(self):
        camera = self.camera
        
        number_of_photo = 1

        cap = cv2.VideoCapture(camera)
        cap.set(3, 640)
        cap.set(4, 480)

        # it takes a pictures five times at 640x480 size for better license plate recognition.
        while number_of_photo <= 10:
            ret, frame = cap.read()
                
            cv2.imwrite('/home/pi/parking_lot_'+self.information[0]+'/'+str(number_of_photo)+'.jpg', frame) # Saving pictures in parking_lot_(number) folder as number 1 to 5. 

            number_of_photo = number_of_photo + 1

            time.sleep(0.1)

        cap.release()

    def video(self):
        camera = self.camera
        
        cap = cv2.VideoCapture(camera)

        # setting codec and video size.
        fourcc = cv2.VideoWriter_fourcc(*'XVID')
        writer = cv2.VideoWriter('/home/pi/parking_lot_'+self.information[0]+'/video.avi', fourcc, 30.0, (640, 480))

        length = 0

        # to record video for specific times I use length varible. It records video until length become 90.
        while(length < 60):
            ret, frame = cap.read()

            if ret == True:
                writer.write(frame)
            else:
                break

            length = length + 1

        cap.release()
        writer.release()

        #print("Video has been recorded\n")

    def video_transfer(self):
        # open video file and convert it.
        video = '/home/pi/parking_lot_'+self.information[0]+'/video.avi'
        vid = open(video, 'rb')
        converted_video = vid.read()

        # send file size first and get reply from server.
        self.parking_lot.sendall(bytes(str(sys.getsizeof(converted_video)), encoding = 'utf8'))
        self.parking_lot.sendall(bytes("\n", encoding = 'utf8'))

        reply = self.parking_lot.recv(1024)
        reply = reply.decode()
        #print(reply, "\n")

        # send video file to server.
        self.parking_lot.sendall(converted_video)
        vid.close()

        #print("Video is transferred to server\n")

    def speaker(self, order):
        # it plays mp3 files according to parking lot number and make led flash at the same time

        if order == "handicap":
            sound = AudioSegment.from_mp3('/home/pi/product/ParkingLot_'+self.number+'_handicap_warning.mp3')
        elif order == "load":
            sound = AudioSegment.from_mp3('/home/pi/product/ParkingLot_'+self.number+'_load_warning.mp3')

        play(sound)
        
        self.led("flash")

    def parking_in(self):
        while(True):
            # When first distance from sensor is below 10cm it starts to get second distance from sensor again.
            # If distance between first one and second one is under 0.5 it determines a car is parked.
            first_distance = self.ultrasonic_sensor()
            # you can change distacne to decide a car is parked in
            if first_distance <= 13:
                return "parked"
                #second_distance = self.ultrasonic_sensor()
                #if second_distance >= first_distance - 0.5 and second_distance <= first_distance + 0.5:
                    #return "parked"

    def parking_out(self):
        # when a car is parked, It records video and transfer it to server
        self.video()
        
        self.video_transfer()

        # It gets distance from sensor continously. If distance is over 10cm, It determines a car is out.
        # When a car is out, It saves date and time immediately and transfer it to server and turns off LED and connection. 
        while(True):
            first_distance = self.ultrasonic_sensor()

            # you can change distacne to decide a car is parked out
            if first_distance > 13:
                second_distance = self.ultrasonic_sensor()
                if second_distance > 15:
                    ParkingOutDate = datetime.today().strftime("%Y-%m-%d %H:%M:%S")
                    self.parking_lot.sendall(bytes(ParkingOutDate, encoding = 'utf8'))
                    self.parking_lot.sendall(bytes("\n", encoding = 'utf8'))
                    self.led("off")

                    self.parking_lot.close()
                    break

    def violation_check(self):
        # If distance is over 10cm after warning, It determines a car is out to do not break a law and send string data about it to server.
        # If distance is still below 10cm even after warning, It determines a car breaks a law and send string data about it to server and makes LED red and call parking_out() method to record parking out date.
    
        distance = self.ultrasonic_sensor()

        if distance > 13:
            self.parking_lot.sendall(bytes("\n", encoding = 'utf8'))
            self.parking_lot.close()
        elif distance <= 13:
            self.parking_lot.sendall(bytes("위반\n", encoding = 'utf8'))
            self.led("red")
            self.parking_out()
            
    def parking_lot(self):
        number = self.number # parking lot number.
        
        self.information[0] = number # saving parking lot number in list.

        result = [""] * 10 # list saving license plate recognition results.

        HOST = '00.00.00.00'
        PORT = 7000

        self.led("off") # At first led should be off.

        while(True):
            self.parking_lot = socket(AF_INET, SOCK_STREAM) # Socket on.

            # If a car is parked.
            if self.parking_in() == "parked":
                #print("A car has been parked\n")

                self.information[2] = datetime.today().strftime("%Y-%m-%d %H:%M:%S") # Saving parking in date and time in information list.

                self.photo() # Taking pictures

                #print("Photos have been taken\n")

                try:
                    self.parking_lot.connect((HOST,PORT)) # Connecting to server

                    #print("Parking lot has been connected to server\n")
                    
                    number_of_photo = 1
                    number_of_result = 0

                    license_plate = None

                    while number_of_photo <= 10:
                        result[number_of_result] = self.license_plate_recognition(number_of_photo) # Saving license plate recognition results in result list from 1 to 5

                        # Checking if result[number_of_result] is consist of only digit because license plate is consist of number and text.
                        # result[number_of_result].isdigit() == 0 means it isn't consist of only numbers and len(result[number_of_result]) == 7 means it has same number of character with korean license plate.
                        if result[number_of_result].isdigit() == 0 and len(result[number_of_result]) == 7:
                            license_plate = result[number_of_result]
                            self.information[1] = license_plate

                            print("License Plate:", self.information[1], "\n")
                            break
                        else:
                            number_of_result = number_of_result + 1
                            number_of_photo = number_of_photo + 1

                    if license_plate == None:
                        self.information[1] = "적재물" # If license_plate is still None, It can be considered as load.

                        print("License Plate:", self.information[1], "\n")

                    # Using for loop send information list from 0 to 2 to server
                    for number_of_list in self.information:
                        self.parking_lot.sendall(bytes(number_of_list, encoding = 'utf8'))
                        self.parking_lot.sendall(bytes("\n", encoding = 'utf8'))

                    #print("Data has been transferred\n")

                    order = self.parking_lot.recv(1024)
                    order = order.decode()

                    # pass means that a car doesn't break a law, handicap violation means that a car break a law and load violation means loads are on parking lot.
                    if order == "pass":
                        self.led("green")
                        self.parking_out()
                    elif order == "handicap violation":
                        self.led("red")
                        self.speaker("handicap")
                        self.violation_check()
                    elif order == "load violation":
                        self.led("red")
                        self.speaker("load")
                        self.violation_check()

                except Exception as e:
                    print(e)

parking_lot_1 = parking_lot(25, 8, 10, 9, 2, "1")
parking_lot_2 = parking_lot(23, 24, 17, 27, 4, "2")
parking_lot_3 = parking_lot(14, 15, 2, 3, 6, "3")

t1 = threading.Thread(target=parking_lot_1.parking_lot, args=())
t2 = threading.Thread(target=parking_lot_2.parking_lot, args=())
t3 = threading.Thread(target=parking_lot_3.parking_lot, args=())

t1.start()
t2.start()
t3.start()
