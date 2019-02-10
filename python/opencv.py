import sys
import cv2
import json
import time
import errno
import socket
import numpy as np
import contexttimer

from math import sqrt


lowH = 0  # 46
lowS = 0  # 82
lowV = 254  # 49

highH = 180  # 156
highS = 255  # 187
highV = 255  # 172

STX = '^'
ETX = ';'
FS = '|'

IDEAL_TARGET_RATIO = 5.5 / 2.0

gui = False
print("len sys.argv: " + str(len(sys.argv)))
if len(sys.argv) > 1:
    gui = True


def start_socket():
    TCP_IP = '0.0.0.0'
    TCP_PORT = 5802
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((TCP_IP, TCP_PORT))
    s.listen(1)
    return s.accept()


def editLowHue(h):
    global lowH
    lowH = h


def editHighHue(h):
    global highH
    highH = h


def editLowSat(s):
    global lowS
    lowS = s


def editHighSat(s):
    global highS


def editLowVal(v):
    global lowV
    lowV = v


def editHighVal(v):
    global highV
    highV = v


def distance(a, b):
    """
    Accepts a point tuple (x, y) for arguments a and b
    then returns number of pixels between the points.
    """
    x1 = a[0]
    y1 = a[1]
    x2 = b[0]
    y2 = b[1]

    xdiff = x1 - x2
    ydiff = y1 - y2
    return sqrt(xdiff**2 + ydiff**2)


cap = cv2.VideoCapture(0)
cap.set(3, 320)
cap.set(4, 240)

if gui:
    cv2.namedWindow("image")
    cv2.createTrackbar('LH', 'image', lowH, 255, editLowHue)
    cv2.createTrackbar('LS', 'image', lowS, 255, editLowSat)
    cv2.createTrackbar('LV', 'image', lowV, 255, editLowVal)

    cv2.createTrackbar('HH', 'image', highH, 255, editHighHue)
    cv2.createTrackbar('HS', 'image', highS, 255, editHighSat)
    cv2.createTrackbar('HV', 'image', highV, 255, editHighVal)

conn, addr = start_socket()

cont = True
while cont:
    start_time = time.time()
    ret, image = cap.read()
    if gui:
        cv2.imshow('image', image)
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    ret, hsv_mask = cv2.threshold(hsv, 200, 255, cv2.THRESH_BINARY)
    res = hsv_mask
    contours, hierarchy = cv2.findContours(hsv_mask,
                                           cv2.RETR_TREE,
                                           cv2.CHAIN_APPROX_SIMPLE)

    left_targets = list()
    right_targets = list()
    for c in contours:
        if cv2.contourArea(c) > 50:
            rect = cv2.minAreaRect(c)
            ((cx, cy), (w, h), angle) = rect
            real_h = max(h, w)
            real_w = min(h, w)
            aspect_ratio = real_h / real_w
            if (aspect_ratio < IDEAL_TARGET_RATIO * .50
               or
               aspect_ratio > IDEAL_TARGET_RATIO * 1.5):
                print("BAD aspect ratio: {}".format(aspect_ratio))
                # This is way off from a 5.5"x2" square's aspect ratio so
                # skip it.
                continue

            print("good aspect ratio: {}".format(aspect_ratio))
            angle += 90.0
            label = "U"
            if (10 < angle < 20):
                label = "L"
                left_targets.append(rect)
            elif (70 < angle < 80):
                label = "R"
                right_targets.append(rect)
            box = cv2.boxPoints(rect)
            box = np.int0(box)
            if gui:
                cv2.putText(res, label, (int(cx), int(cy)),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2,
                            cv2.LINE_AA)
                cv2.drawContours(res, [box], 0, (0, 0, 255), 2)

    target_list = list()
    for lt in left_targets:
        ((lx, ly), (lw, lh), langle) = lt
        lcenter = (int(lx), int(ly))
        shortest = 9999
        right_match = None
        for rt in right_targets:
            ((rx, ry), (rw, rh), rangle) = rt
            if rx < lx:
                # Skip this target because with a lower X value it must
                # be on the left.
                continue
            rcenter = (rx, ry)
            dist = distance(lcenter, rcenter)
            if dist < shortest:
                shortest = dist
                right_match = rt
        if right_match is not None:
            ((rx, ry), (rw, rh), rangle) = right_match
            rcenter = (int(rx), int(ry))
            cv2.line(res, lcenter, rcenter, (0, 255, 0), 3)
            newx = int((lcenter[0] + rcenter[0])/2)
            newy = int((lcenter[1] + rcenter[1])/2)
            averArea = ((lw*lh)+(rw*rh))/2
            print(averArea)
            if gui:
                cv2.circle(res, (newx, newy), 10, (0, 255, 0), -1)
            target_list.append((newx, newy, averArea))

    shortest = 999
    winner = None
    for t in target_list:
        # get center x,y of image (res)
        cx = 320 / 2
        cy = 240 / 2
        dist = distance((cx, cy), t)
        if dist < shortest:
            shortest = dist
            winner = t
    if winner is None:
        ret_str = "^NaN|NaN|NaN;"
        blist = bytearray()
        blist.extend(ret_str.encode('utf-8'))
        try:
            conn.sendall(blist)
        except IOError as e:
            if e.errno == errno.EPIPE:
                conn, addr = start_socket()
    if winner is not None:
        if gui:
            cv2.circle(res, (winner[0], winner[1]), 30, (255, 0, 255), -1)
        resp = {}
        resp['winner_x'] = winner[0]
        resp['winner_y'] = winner[1]
        js = json.dumps(resp, indent=4, sort_keys=True, default=str)
        try:
            ret_str = "^{}|{}|{};".format(winner[0], winner[1], winner[2])
            blist = bytearray()
            blist.extend(ret_str.encode('utf-8'))
            conn.sendall(blist)
        except socket.error as e:
            if e.errno == errno.ECONNRESET:
                # Handle disconnection -- close & reopen socket etc.
                conn, addr = start_socket()
            elif e.errno == errno.EPIPE:
                # Handle disconnection -- close & reopen socket etc.
                conn, addr = start_socket()
            else:
                # Other error, re-raise
                raise
    end_time = time.time()
    print("FPS: {}".format(round(1.0 / (end_time - start_time), 1)))
    if gui:
        cv2.imshow('mask', res)
        key = cv2.waitKey(25)
        if key == ord('q'):
            cont = False
conn.close()
cap.release()
cv2.destroyAllWindows()
