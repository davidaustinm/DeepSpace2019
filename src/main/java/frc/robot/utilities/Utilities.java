/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

/**
 * Add your docs here.
 */
public class Utilities {
    public static double clip(double x, double min, double max) {
        if (x > max) return max;
        if (x < min) return min;
        return x;
    }

    public static double normalizeAngle(double angle, double cutpoint) {
        while(angle > cutpoint) angle -= 360;
        while(angle < cutpoint-360) angle += 360;
        return angle;
    }
}
