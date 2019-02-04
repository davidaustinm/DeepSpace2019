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
    String serverIP = "10.40.3.104";
    int port = 5802;
    Thread thread;

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