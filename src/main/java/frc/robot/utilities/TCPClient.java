package frc.robot.utilities;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    public void start() {
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

    protected Socket connectToPi(String serverIP, int port) {

        System.out.println("Connecting to " + serverIP + " on port " + port);
        Socket client = null;
        try {
            client = new Socket(serverIP, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
        } catch (IOException ex) {
            client = null;
            System.out.println("Error connecting to pi.");
            ex.printStackTrace();
        }
        return client;
    }

    public void run() {
        Socket client = connectToPi(serverIP, port);
        while(!Thread.interrupted()) {
            if (client == null) { 
                // TODO: Extend the if to check for socket connectivity, reconnect if needed.
                client = connectToPi(serverIP, port);
            }
            try {
                if (client == null) {
                    continue; 
                }
                InputStream in = client.getInputStream();
                byte[] content = new byte[1024];
                int len;
                len = in.read(content);
                byte[] valid = Arrays.copyOfRange(content, 0, len);
                String textData = new String(valid, StandardCharsets.UTF_8);
                int start = textData.lastIndexOf("^");
                int end = textData.lastIndexOf(";");
                String goodData = textData.substring(start+1, end);
                if (start != -1 && end != -1) {
                    String[] xy = goodData.split("\\|");
                    double x = Double.parseDouble(xy[0]);
                    double y = Double.parseDouble(xy[1]);
                    double area = Double.parseDouble(xy[2]);
                    SmartDashboard.putNumber("inner TCP x", x);
                    SmartDashboard.putNumber("inner TCP y", y);
                    SmartDashboard.putNumber("inner TCP area", area);
                    setTargetInfo(x, y, area);
                }
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