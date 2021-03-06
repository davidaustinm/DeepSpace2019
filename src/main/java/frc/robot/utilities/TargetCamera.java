/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class TargetCamera implements Runnable, TargetInfo {
    Object lock = new Object();
    double distance;
    CvSink cvSink;
    CvSource outputStream;
    Mat source, output, mask, hierarchy;
    ArrayList<MatOfPoint> contours;
    Thread thread;
    double fieldOfView = 15 / 26.0;
    double imageWidth = 160;
    double angle = 0;
    /*
    double[] coefficients = new double[] {
        15.5887262581734, 38646.2278445553, -7.35691471763911e+6,
        8.80924154583584e8, -4.04032044493288e+10
    };
    */
    protected double[] coefficients = new double[] {
		20.075,
		30236.23,
		-2.4818e+6,
		1.1185e+8
	};
    public TargetCamera() {
        System.out.println("start camera thread");
        
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(320, 240);
      
        cvSink = CameraServer.getInstance().getVideo();
        outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);
      
        source = new Mat();
        output = new Mat();
        mask = new Mat();
        hierarchy = new Mat();
        contours = new ArrayList<MatOfPoint>();
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void run() {
        while(!Thread.interrupted()) {
          long start = System.currentTimeMillis();
          cvSink.grabFrame(source);
          if (source.empty()) {
            System.out.print("source empty");
            continue;
          }
            contours = new ArrayList<MatOfPoint>();
          Imgproc.resize(source, source, new Size(320, 240));
          Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
          Imgproc.threshold(output, mask, 100, 255, Imgproc.THRESH_BINARY);
          Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
          long elapsed = System.currentTimeMillis() - start;
          String s = "";
          double area = 0;
          double center = 0;
          //System.out.println(contours.size());
          for (int i=0; i < contours.size(); i++) {

            try {
              RotatedRect ellipse = Imgproc.fitEllipseDirect(contours.get(i));
              //s += ellipse.angle + " ";
              Size size = ellipse.size;
              double aspect_ratio = size.height/size.width;
              if (aspect_ratio < 1) aspect_ratio = 1/aspect_ratio;
              s += aspect_ratio + " ";
              Point c = ellipse.center;
              center += c.x - imageWidth;
            } catch(Exception ex) {
              continue;
            }
            area += Imgproc.contourArea(contours.get(i));
          }
          area /= contours.size();
          
          center /= contours.size();
          //double[] targetInfo = Robot.targetInfo.getTargetInfo();
          double distance = coefficients[0];
          double x = 1/area;
          for (int i = 1; i < coefficients.length; i++) {
            distance += coefficients[i]*x;
            x /= area;
          }
          
          setDistance(distance);
          angle = -Math.toDegrees(Math.atan(fieldOfView / imageWidth * center));
          SmartDashboard.putNumber("distance", distance);
          SmartDashboard.putNumber("angle", angle);
          if(RobotMap.DEBUG){
            //System.out.println(/*elapsed + " " + */distance + " " + angle);
          }
          outputStream.putFrame(output);
          //System.out.println(area + " " + center + " " + distance + " " + x + " " + angle);
          
        }
        
    }

    public double getDistance(){
            return distance;
    }
    public void setDistance(double d) {
            distance = d;
    }
    public double getAngle(){
            return angle;
    }
    public void setAngle(double a) {
            angle = a;
    }
}
