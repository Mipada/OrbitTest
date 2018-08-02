/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app.nodes;

import java.util.ArrayList;
import my.app.scene.controls.*;

/**
 *
 * @author Peter
 */
public class Node {
    String name;
    ArrayList list = new ArrayList();
    ArrayList controlList = new ArrayList();
    
    public Node(){
        this(null);
    }
    
    public Node(String name){
        this.name = name;
    }
    
    
    public String getName(){
        return name;
    }
    
    
    public boolean add(Object object){
        return (list.add(object));
    }

    
    public boolean remove(Object object){
        return (list.remove(object));
    }
    
    
    
    public int getChildrenCount(){
        return list.size();
    }
    
    
    public Object[] getChildren(){
        return list.toArray();
    }
    
    
    public Object get(String name){
        Object[] array = list.toArray();
        for (Object object : array){
            if (((Node)object).getName().equals(name)){
                return object;
            }
        }
        return null;
    }

    
    public Object get(Object item){
        Object[] array = list.toArray();
        for (Object object : array){
            if (((Node)object).equals(item)){
                return object;
            }
        }
        return null;
    }

    
    public Object getChild(int i){
        return list.get(i);
    }

    
    public boolean addControl(OrbitControl control){
        control.setSpatial(this);
        return (controlList.add(control));
    }
    
    
    public Object[] getControls(){
        return (controlList.toArray());
    }

    
    public boolean remove(OrbitControl control){
        return (controlList.remove(control));
    }

    
}
