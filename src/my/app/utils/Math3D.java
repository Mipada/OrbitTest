//Math3D functions

package my.app.utils;

import java.lang.Math.*;
import java.text.DecimalFormat;
import java.text.ParseException;
//import javax.media.j3d.Transform3D;
//import javax.media.j3d.TransformGroup;
import javax.swing.text.NumberFormatter;
import javax.vecmath.*;
//import navagationPkg.*;


public class Math3D{
	public static Vector3d RotateX(Vector3d point3D, double degrees)
	{
		//Here we use Euler's matrix formula for rotating a 3D point x degrees around the x-axis

		//[ a  b  c ] [ x ]   [ x*a + y*b + z*c ]
		//[ d  e  f ] [ y ] = [ x*d + y*e + z*f ]
		//[ g  h  i ] [ z ]   [ x*g + y*h + z*i ]

		//[ 1    0        0   ]
		//[ 0   cos(x)  sin(x)]
		//[ 0   -sin(x) cos(x)]

		double cDegrees = (Math.PI * degrees) / 180.0f; //Convert degrees to radian for .Net cos/sin functions
		double cosDegrees = Math.cos(cDegrees);
		double sinDegrees = Math.sin(cDegrees);

		double y = (point3D.y * cosDegrees) + (point3D.z * sinDegrees);
		double z = (point3D.y * -sinDegrees) + (point3D.z * cosDegrees);

		return new Vector3d(point3D.x, y, z);
	}

	public static Vector3d RotateY(Vector3d point3D, double degrees)
	{
		//Y-axis

		//[ cos(x)   0    sin(x)]
		//[   0      1      0   ]
		//[-sin(x)   0    cos(x)]

		double cDegrees = (Math.PI * degrees) / 180.0; //Radians
		double cosDegrees = Math.cos(cDegrees);
		double sinDegrees = Math.sin(cDegrees);

		double x = (point3D.x * cosDegrees) + (point3D.z * sinDegrees);
		double z = (point3D.x * -sinDegrees) + (point3D.z * cosDegrees);

		return new Vector3d(x, point3D.y, z);
	}

	public static Vector3d RotateZ(Vector3d point3D, double degrees)
	{
		//Z-axis

		//[ cos(x)  sin(x) 0]
		//[ -sin(x) cos(x) 0]
		//[    0     0     1]

		double cDegrees = (Math.PI * degrees) / 180.0; //Radians
		double cosDegrees = Math.cos(cDegrees);
		double sinDegrees = Math.sin(cDegrees);

		double x = (point3D.x * cosDegrees) + (point3D.y * sinDegrees);
		double y = (point3D.x * -sinDegrees) + (point3D.y * cosDegrees);

		return new Vector3d(x, y, point3D.z);
	}

	public static Vector3d Translate(Vector3d points3D, Vector3d oldOrigin, Vector3d newOrigin)
	{
		//Moves a 3D point based on a moved reference point
		Vector3d difference = new Vector3d(newOrigin.x - oldOrigin.x, newOrigin.y - oldOrigin.y, newOrigin.z - oldOrigin.z);
		//System.out.println("X=" + points3D.X + ", dif=" + difference.X);
		points3D.x += difference.x;
		points3D.y += difference.y;
		points3D.z += difference.z;
		return points3D;
	}

	//These are to make the above functions workable with arrays of 3D points
	public static Vector3d[] RotateX(Vector3d[] points3D, double degrees)
	{
		for (int i = 0; i < points3D.length; i++)
		{
			points3D[i] = RotateX(points3D[i], degrees);
		}
		return points3D;
	}

	public static Vector3d[] RotateY(Vector3d[] points3D, double degrees)
	{
		for (int i = 0; i < points3D.length; i++)
		{
			points3D[i] = RotateY(points3D[i], degrees);
		}
		return points3D;
	}

	public static Vector3d[] RotateZ(Vector3d[] points3D, double degrees)
	{
		for (int i = 0; i < points3D.length; i++)
		{
			points3D[i] = RotateZ(points3D[i], degrees);
		}
		return points3D;
	}

	public static Vector3d[] Translate(Vector3d[] points3D, Vector3d oldOrigin, Vector3d newOrigin)
	{

		for (int i = 0; i < points3D.length; i++)
		{
			//System.out.println("point3D[" + i + "]=" + points3D[i]);
			points3D[i] = Translate(points3D[i], oldOrigin, newOrigin);
		}
		return points3D;
	}


	public static String distFormat(double n){
		String s = new String("");
		DecimalFormat distFormat = new DecimalFormat(" 0.000E00;-0.000E00");
		DecimalFormat degreeFormat = new DecimalFormat("000.00");
		NumberFormatter distFormatter = new NumberFormatter(distFormat);
		NumberFormatter degreeFormatter = new NumberFormatter(degreeFormat);

		
		try{
			s = distFormatter.valueToString(n);
		}
		catch(ParseException e){
			s = new String("string parse error");
		}
		return(s);
	
	}
    
    
    public static Vector3d multiply(Vector3d v1, Vector3d v2, Vector3d vr){
        
        return vr;
    }

	
	public static void display(Matrix3d tMat){
		System.out.println("+(  x=          y=         z=         w=");
		System.out.println("(x=" + distFormat(tMat.m00) + ", " + distFormat(tMat.m01) + ", " + distFormat(tMat.m02) + ", " + distFormat(tMat.m02));
		System.out.println("(y=" + distFormat(tMat.m10) + ", " + distFormat(tMat.m11) + ", " + distFormat(tMat.m12) + ", " + distFormat(tMat.m12));
		System.out.println("(z=" + distFormat(tMat.m20) + ", " + distFormat(tMat.m21) + ", " + distFormat(tMat.m22) + ", " + distFormat(tMat.m22));
		//System.out.println("(w=" + distFormat(tMat.m30) + ", " + distFormat(tMat.m31) + ", " + distFormat(tMat.m32) + ", " + distFormat(tMat.m32));
	}
	
	
}


