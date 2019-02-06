package frc.robot.utilities;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;

public class TCPClient implements Runnable{
    private final Object lock = new Object();
    double[] targetInfo = new double[] {-1,-1, -1};
    double[] coefficients = new double[] {
        15.5887262581734, 38646.2278445553, -7.35691471763911e+6,
        8.80924154583584e8, -4.04032044493288e+10
    };
    double distance;
    double angle;
    double fieldOfView = 15 / 26.0;
    double imageWidth = 160;
    String serverIP = "10.40.3.51";
    int port = 5802;
    Thread thread;
    long lastTime = 0;

    public TCPClient() {
        thread = new Thread(this);
    }

    public void start(){
        thread.start();
    }

    public void setTargetInfo(double x, double y, double averArea) {
    	synchronized(lock) {
    		targetInfo = new double[] {x, y, averArea};
        }
        
        double area = targetInfo[2];
        double d = coefficients[0];
        double val = 1/area;
        for (int i = 1; i < coefficients.length; i++) {
          d += coefficients[i]*val;
          val /= area;
        }
           
        double center = targetInfo[0] - imageWidth;
        distance = d;
        angle = -Math.toDegrees(Math.atan(fieldOfView / imageWidth * center));
        long time = System.currentTimeMillis();
        double elapsed = time - lastTime;
        lastTime = time;
        System.out.println(elapsed + " " +distance + " " + angle);
        
    }

    public double getDistance() {
        return distance;
    }
    public double getAngle() {
        return angle;
    }
    
    public double[] getTargetInfo() {
    	synchronized(lock) {
    		return targetInfo;
    	}
    }

    public void run() {
        Socket client = null;
        try {
           System.out.println("Connecting to " + serverIP + " on port " + port);
           client = new Socket(serverIP, port);
           System.out.println("Just connected to " + client.getRemoteSocketAddress());
        } catch (IOException e) {
           e.printStackTrace();
        }
        while(!Thread.interrupted()) {
            try {
                InputStream in = client.getInputStream();
                byte[] content = new byte[1024];
                int len;
                len = in.read(content);
                byte[] valid = Arrays.copyOfRange(content, 0, len);
                String textData = new String(valid, StandardCharsets.UTF_8);
                int start = textData.lastIndexOf("^");
                int end = textData.lastIndexOf(";");
                String goodData = textData.substring(start+1, end);
                String[] xy = goodData.split("\\|");
                setTargetInfo(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]), Double.parseDouble(xy[2]));
                //System.out.println("X:" + xy[0] + " Y:" + xy[1] + " Average Area:" + xy[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] convertALToArray(List<Byte> in) {
	    final int n = in.size();
		byte ret[] = new byte[n];
		for (int i = 0; i < n; i++) {
			ret[i] = in.get(i);
        }
        return ret;
    }
}