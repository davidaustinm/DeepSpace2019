package frc.robot.utilities;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.*;

public class UDPServer implements Runnable, TargetInfo {
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
	protected int port = 5802;
	protected Thread thread;
	protected long lastTime = 0;
	protected byte[] content = new byte[1024];

	protected double currX, currY, currArea;

	public UDPServer() {
		try {
			thread = new Thread(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UDPServer c = new frc.robot.utilities.UDPServer();
		System.out.println("starting thread...");
		try {
			c.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void start() {
		System.out.println("UDP Start");
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
		lastTime = time;
        // SmartDashboard.putNumber("distance", distance);
        // SmartDashboard.putNumber("angle", angle);

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

	public void run() {
		// In the UDP version the RoboRIO will be the server side, opening up a connection
		// for the pi to connect to and fire data at us.
		System.out.println("Running");
		DatagramSocket dgs = null;
		try {
			dgs = new DatagramSocket(port);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}

		while(!Thread.interrupted()) {
			try {
				// Reset the receive buffer else we'll get some weird results.
				Arrays.fill(content, (byte)0);
				DatagramPacket dgp = new DatagramPacket(content, content.length);
				dgs.receive(dgp);
				String textData = new String(dgp.getData(), StandardCharsets.UTF_8);

				int start = textData.lastIndexOf("^");
				int end = textData.lastIndexOf(";");
				String goodData = textData.substring(start+1, end);
				System.out.println(goodData);
				if (start != -1 && end != -1) {
					String[] xy = goodData.split("\\|");
					double x = Double.parseDouble(xy[0]);
					double y = Double.parseDouble(xy[1]);
					double area = Double.parseDouble(xy[2]);
					System.out.println("UDP recv: " + x + ", " +  y + " area: " + area);
					/*
					SmartDashboard.putNumber("inner UDP x: ", x);
					SmartDashboard.putNumber("inner UDP y: ", y);
					SmartDashboard.putNumber("inner UDP area: ", area);
					*/
					setTargetInfo(x, y, area);
				}
				Thread.sleep(10);
			} catch (Exception e) {
				System.out.println("Error in UDP code: " + e.getMessage());
			}
		}
	}
}
