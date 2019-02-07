#!/usr/bin/python3

"""
A kind of 'dummy' server for the camera system. This will just put out
data as the camera code would, but it'll be completely fabricated.
We'll run a predictable pattern and if at any point in the system the numbers
don't align then we know we got a mis-read of sorts.

But mostly this is just to debug TCPClient.java without actually being
on the robot.
"""

import time
import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind(('0.0.0.0', 5802))
s.listen(1)

conn, addr = s.accept()
print("Connection to {} established.".format(addr))
x = 0
y = 0
area = 0
while True:
    x = x + 1
    y = x * 2
    area = x * y
    if x > 200:
        x = 0
        continue  # Jump back to the top of our while loop

    conn.send("^{}|{}|{};".format(x, y, area).encode('utf-8'))
    time.sleep(1 / 24.0)
