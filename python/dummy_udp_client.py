#!/usr/bin/python3

"""
Like dummy_server.py but for UDP.
"""

import time
import socket
import errno

roborio_addr = "127.0.0.1"
roborio_port = 5802

x = 0
y = 0
area = 0
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
while True:
    x = x + 1
    y = x * 2
    area = x * y
    if x > 200:
        x = 0
        continue  # Jump back to the top of our while loop

    try:
        s.sendto("^{}|{}|{};".format(x, y, area).encode('utf-8'),
                 (roborio_addr, roborio_port))
    except Exception as e:
        print("Some kind of exception.")
    time.sleep(1 / 60.0)
