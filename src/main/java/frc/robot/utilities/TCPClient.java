package frc.robot.utilities;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.*;

public class TCPClient implements Runnable, TargetInfo {
	//private final Object lock = new Object();
	protected double[] targetInfo = new double[] {-1, -1, -1};
	/*
	protected double[] coefficients = new double[] {
												15.5887262581734,
												38646.2278445553,
												-7.35691471763911e+6,
												8.80924154583584e8,
												-4.04032044493288e+10 };
												*/
	protected double[] coefficients = new double[] {
		20.075,
		30236.23,
		-2.4818e+6,
		1.1185e+8
	};
	protected double distance;
	protected double angle;
	protected double fieldOfView = 15 / 26.0;
	protected double imageWidth = 160;
	protected String serverIP = "10.40.3.51";
	protected int port = 5802;
	protected Thread thread;
	protected long lastTime = 0;
	protected byte[] content = new byte[1024];

	protected double currX, currY, currArea;

	public TCPClient() {
		try {
			thread = new Thread(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TCPClient c = new frc.robot.utilities.TCPClient();
		c.serverIP = args[0];
		System.out.println("starting thread...");
		try {
			c.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void start() {
		if (!thread.isAlive()) thread.start();
	}

	protected void setTargetInfo(double x, double y, double averArea) {
		//System.out.println(x + " " + y + " " + averArea);
		this.currX = x;
		this.currY = y;
		this.currArea = averArea;

		double d = coefficients[0];
		double val = 1 / currArea;

		for (int i = 1; i < coefficients.length; i++) {
			d += coefficients[i] * val;
			val /= currArea;
		}

		double center = currX - imageWidth;

		distance = d;
		angle = -Math.toDegrees(Math.atan(fieldOfView / imageWidth * center));

		long time = System.currentTimeMillis();
		//double elapsed = time - lastTime;
		lastTime = time;
		//System.out.println(elapsed + " " + distance + " " + angle);
        SmartDashboard.putNumber("distance", distance);
        SmartDashboard.putNumber("angle", angle);

	}

	public double getDistance() {
		return distance;
	}

	public double getAngle() {
		return angle;
	}

	public double[] getTargetInfo() {
		return new double[] { currX, currY, currArea };
	}

	public double getTargetX() {
		return currX;
	}

	public double getTargetY() {
		return currY;
	}

	public double getTargetArea() {
		return currArea;
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
			try {
				if (client == null) {
					client = connectToPi(serverIP, port);
					continue; 
				}
				InputStream in = client.getInputStream();
				int len;
				len = in.read(content);
				if (len == -1) {
					// We lost our connection
					client.close();
					client = null;
					continue; // Start back at the top of our while loop
				}
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
					//SmartDashboard.putNumber("inner TCP x: ", x);
					// SmartDashboard.putNumber("inner TCP y: ", y);
					// SmartDashboard.putNumber("inner TCP area: ", area);
					setTargetInfo(x, y, area);
				}
				Thread.sleep(10);
			} catch (Exception e) {
				client = null;
				System.out.println("Error in TCP code, going to reconnect.");
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
