/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app.nodes;

import javax.vecmath.Vector3d;

/**
 *
 * @author Peter
 */
public class MassNode extends Node{
    static int NAME_ = 0;
    static int PARENT_ = 1;
    static int a_ = 2;
    static int e_ = 3;
    static int i_ = 4;
    static int T_ = 5;
    static int m_ = 6;
    static int r_ = 7;
    
    Vector3d localTranslation = new Vector3d();
    Vector3d worldTranslation = new Vector3d();
    //Quaternion rotation = new Quaternion();
    Vector3d angle = new Vector3d();
    Vector3d velocity = new Vector3d();
    Vector3d angularVelocity = new Vector3d();
    double a = 0f;
    double b = 0f;
    double e = 0f;
    double i = 0f;
    //
 
    
    public MassNode(String[] data){
        super(data[NAME_]);
        a = Double.valueOf(data[a_]);
        e = Double.valueOf(data[e_]);
        i = Double.valueOf(data[i_]);
        b = a * Math.sqrt(1d - (e * e));
    }
    
    
    public Vector3d getWorldTranslation(){
        return worldTranslation;
    }
    
    
    public void setTranslation(Vector3d worldTranslation){
        this.worldTranslation = worldTranslation;
    }
    
    
    public void move(float tpf){
    }
    
    
}
