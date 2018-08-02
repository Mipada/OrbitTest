/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app.nodes;

import java.awt.Color;
import javax.vecmath.Vector3d;

/**
 *
 * @author Peter
 */
public class PlanetNode extends MassNode{
    public double mass = 10d;
    public double radius = 10d;
    double radial = Math.random() * 360d;
    double step = 0.01d;
    protected Color color;
    
    public PlanetNode(String[] data){
        super(data);
        mass = (double)Double.valueOf(data[m_]);
        radius = (double)Double.valueOf(data[r_]);
        if (name.equals("Sun")){
            radial = 0d;
            color = Color.yellow;
        }
        else if (name.equals("Earth")){
            //step = 365.25d/24d/60d/60d/60d;
            step = 0.001d;
            color = Color.blue;
            
        }
        else if (name.equals("Moon")){
            //step = 28d/24d/60d/60d/60d;;
            step = 0.01d;
            color = Color.GRAY;
            
        }
    }
    
    
    public void move(float tpf){
        super.move(tpf);
        radial += (step * tpf);
        if (radial > 360d)
            radial -= 360d;
        else if (radial < 0)
            radial += 360d;
        //System.out.println("PlanetNode.move() " + "(" + name + ")" + " radial=" + radial);
    }
    
    
    public Vector3d getTranslation(Vector3d pTrans){
        double x, y, z;
		//Vector3f v = new Vector3f((float) Math.sin(rad) * semiMajorAxis, (float)-Math.sin(rad) * inclination, (float) Math.cos(rad) * semiMinorAxis);
        double rad = radial * (Math.PI/180d);
        localTranslation.set(Math.sin(rad) * a, -Math.sin(rad) * i, Math.cos(rad) * b);
        worldTranslation.set(pTrans.x + localTranslation.x, pTrans.y + localTranslation.y, pTrans.z + localTranslation.z);
        return worldTranslation;
    }


    public double getRadius(){
        return radius;
    }
    
    
    public Color getColor(){
        return color;
    }
    
    
}
