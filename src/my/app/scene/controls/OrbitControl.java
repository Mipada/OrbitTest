/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package lib.my.app.controls;
package my.app.scene.controls;

//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.control.AbstractControl;
//import com.jme3.scene.control.Control;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.text.NumberFormatter;
import javax.vecmath.Vector3d;
//import javax.vecmath.Vector3f;
import my.app.nodes.MassNode;
//import lib.my.app.utilities.Utils;
import my.app.nodes.Node;


/**
 *
 * @author Peter
 */
//a semi-major axis
//b semi-minor-axis
//e eccentricity
//Om + w = II (argument of Perihelion)
//L(t) = II + M(t)
//i inclination
//t time

public class OrbitControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    static public int X = 0;
    static public int Y = 1;
    static public int Z = 2;
    
    static int count = 0;
           int index = 0;
    static int a_ = 0;
    static int e_ = 1;
    static int Ω_ = 2;
    static int II_ = 3;
    static int L_ = 4;
    static int i_ = 5;
           String name = "";
    static int num = 10;//number of bodies
    static String[] bodyName = new String[num];
    static double[][] orbit = new double[num][6];
    static int UP = 1;//to adjusst for different 3D libs
    
    //arguements
    private double a = 0d;//semiMajorAxis
    private double e = 0d;//eccentricity
    private double T = 1L;//time at perihelion
    //calculate
    private double b = 0d;//semiMinorAxis
    private double f = 0d;//the distance from the centre of the orbit to the Sun
    private double P = 365.25d;//period (days?)
    private double i = 0d;//inclination
    private double Ω = 0d;//longitude of ascending node
    private double ω = 0d;//angle between ascending node and perihelion
    private double II = 0d;//II = Ω + ω (0 for Earth)
    private double L = 0d;//L(t) = II + M(t)
    //for 3D
	protected Vector3d localTranslation = new Vector3d();//world trans
	protected Vector3d worldTranslation = new Vector3d();//world trans
    protected boolean changed = false;
	protected Vector3d u = new Vector3d(1d, 0d, 0d);//perihelion (right)
	protected Vector3d w = new Vector3d(0d, 1d, 0d);//up
	protected Vector3d v = new Vector3d(0d, 0d, 0d);//positive y/z axis
    protected Vector3d α = new Vector3d(1d, 0d, 0d);
    //
    Vector3d VStar = new Vector3d();
    Vector3d V0 = new Vector3d();
    Vector3d V1 = new Vector3d();
    double M0 = 0d;
    double M = 0d;//???????????
    double E = 0d;//???????????????
    //
    protected double angleOfAscendingNode = 0d;//Autumnal equinox
	protected Vector3d ascendingNode = new Vector3d(0d, 0d, 0d);
    protected double angleOfDescendingNode = 180d;//Vernal equinox
	protected Vector3d descendingNode = new Vector3d(0d, 0d, 0d);
    protected double angleOfPariapsis = 180d;
	protected Vector3d pariapsis = new Vector3d(0d, 0d, 0d);
    protected double angleOfApoapsis = 180d;
	protected Vector3d apoapsis = new Vector3d(0d, 0d, 0d);
    
	DecimalFormat degreeFormat = new DecimalFormat("000.00◦");
	NumberFormatter degreeFormatter = new NumberFormatter(degreeFormat);
	DecimalFormat distanceFormat = new DecimalFormat("00.00o");
	NumberFormatter distanceFormatter = new NumberFormatter(distanceFormat);

    //private double rad;//angle from parent
    //private double time = 0d;//current time
	

    //plane is x, z (up is sun polor up/down)
    public OrbitControl(String name){
        this(name, 0d, 0d, 0d, 0d, 0d);
    }
    
    
    //plane is x, z (up is sun polor up/down)
    public OrbitControl(String name, double semiMajorAxis, double eccentricity, double inclination){
        this(name, semiMajorAxis, eccentricity, inclination, 0d, 0d);
    }
    
    
    public OrbitControl(String[] data){
        this(data[0]);
    }
    
    
    public OrbitControl(String name, double semiMajorAxis, double eccentricity, double inclination, double longitudeOfAscendingNode, double argumentOfPerihelion){
        this.name = name;
        bodyName[index] = this.name;
        this.a = semiMajorAxis;
        this.e = eccentricity;
        this.i = inclination;
        this.Ω = longitudeOfAscendingNode;
        this.ω = argumentOfPerihelion;
        //
        index = count++;
        calculateOrbitElements();
    }
    
    
    //Calculate;
    //mean anomaly (M)
    //eccentric anomaly (E)
    public void calculateOrbitElements(){

        b = a * Math.sqrt(1d - (e * e));
        f = (a * e);//average distance (kilomters?), AUs for now
        P = Math.sqrt(Math.pow(a, 3));//Newton's 3rd law
        II = Ω + ω;
        //
        T = 1L;//You will divide by this so you can't use 0
        M0 = 1d;
        System.out.println("OrbitControl.getTimeTranslation()" + "(" + name + ")" + ",T=" + T + ",P=" + P);
        M = (2d * Math.PI * ((0d/T)/P));//???????????
        E = M + 0d;//???????????????
/*
        if (name.equals("Sun")){
            T = 1L;
            P = 1L;
        }
        else if (name.equals("Earth")){
            T = 1L;
            P = 365.25d;
        }
        else if (name.equals("Moon")){
            T = 0L - (long)((M0/360d) * P);
            P = 28d;
        }
        else{
            T = 0L - (long)((M0/360d) * P);
            P = 28d;
        }
*/
        calculateOrbitElements2();
    }
    
    
    public void updateOrbitElements(){
        //set new parameters due to thrust
        //i.e., set new a, b, e
        //calculateOrbitElements2();
    }
    
    
    //calculate the 3D elements
    public void calculateOrbitElements2(){
        //
        //vectors
        if (UP == Y){
            u = new Vector3d(1d, 0d, 0d);
            w = new Vector3d(1d, 0d, 0d);
            α = new Vector3d(Math.cos(Ω), 0d, Math.sin(Ω));
            α.normalize();
            v.set(u.x * w.x, u.y * w.y, u.z * w.z);
            α.cross(V1, VStar);
            v.cross(α, V1);
            
            
        }
        else if (UP == Z) {//up = 2
            u = new Vector3d(1d, 0d, 0d);
            w = new Vector3d(0d, 1d, 0d);
            α = new Vector3d(Math.cos(Ω), Math.sin(Ω), 0d);
            α.normalize();
            v.set(u.x * w.x, u.y * w.y, u.z * w.z);
            α.cross(V1, VStar);
            v.cross(α, V1);
        }
        else{
            System.out.println("bad UP code (only 1 (Y axis) or 2 (Z axis))");
            System.exit(1);
        }
        System.out.println("OribtControl.calculateElements2() " + "α=" + α + ",v=" + v);
        //
        //orbit table
        //a, e, Ω, II, L, i
        orbit[index][a_] = a;
        orbit[index][e_] = e;
        orbit[index][Ω_] = Ω;
        orbit[index][II_] = II;
        orbit[index][L_] = L;
        orbit[index][i_] = i;
        
    }
    
    
    //Plotting in 3D
    //you can change the return signiture to return Vector3ds or Vector3fs
    //Solor system plane is x,z (with +y going out the sun's north pole)
    
    //Get the translation at a certain time
    //you can change the return signiture to return Vector3ds or Vector3fs
    //Solor system plane is x,z (with +y going out the sun's north pole)
    //@Param time
    //@Param array for return values (can be changed to Vector3f, Vector3d
    //@Param pTrans coordinate of parent
    public Vector3d getTimeTranslation(double T0, Vector3d pTrans){
        double x, y, z;
        //if (name.equals("Sun")){
        //    return worldTranslation;
        //}
        //
        double M0 = 360d * (T0 - T)/P;
        T = T0 - (M0/360d) * P;//seconds
        //
        if (UP == 1){
            //α = new Vector3d(Math.cos(Ω), 0d, Math.sin(Ω));
        }
        else{
            //α = new Vector3d(Math.cos(Ω), Math.sin(Ω), 0d);
        }
        v.x = w.x * u.x;
        v.y = w.y * u.y;
        v.z = w.z * u.z;
        localTranslation.set(v.x, v.y, v.z);
        worldTranslation.add(pTrans, localTranslation);
        
        changed = true;
        if (name.equals("Earth")) System.out.println("OrbitControl.getTimeTranslation() " + "(" + name + ") " + worldTranslation);
        return worldTranslation;
    }
    
    
    static public void printTable(){
        System.out.println("Planet           a        e          Ω          Π         L        i");
        for (int i = 0;i < testData.length;i++){
            String s = getPlanetData(i);
            System.out.println(s);
        }
    }
    
    
    //Class Formatter
    //◦
    static private String getPlanetData(int i){
        //Class Formatter
        //System.out.println("name=" + bodyName[i]);
		String s = String.format("%-8s    %6.3f    %5.3f    %6.1f◦    %6.2f◦    %5.1f◦    %4.2f◦", testData[i][0], orbit[i][0], orbit[i][1], orbit[i][2], orbit[i][3], orbit[i][4], orbit[i][5]);
        
        return s;
    }

    
    public void setIndex(int i){
        this.index = i;
    }
    
    
    static public void setTable(){
        for (int j = 0;j < testData.length;j++){
            bodyName[j] = testData[j][0];
        }
        for (int j = 0;j < testData.length;j++){
            for (int i = 0;i < testData[0].length - 1;i++){
                orbit[j][i] = Double.valueOf(testData[j][i + 1]);
            }
        }
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        //System.out.println("OribtControl.controlUpdate() " + "(" + name + ")");
        if (changed){
            ((MassNode)spatial).setTranslation(worldTranslation);
            changed = false;
        }
    }
    
    
    static String[][] testData = 
    {
//        {"Planet", "a", "e", "Ω", "Π", "L", "i"}, 
        {"Mercury", "0.387", "0.206", "48.3", "77.46", "252.3", "7.00"}, //◦
        {"Venus", "0.723", "0.007", "76.7", "131.6", "182.0", "3.39"}, 
        {"Earth", "1.000", "0.017", "00.00", "102.9", "100.5", "0.00"}, 
        {"Mars", "1.524", "0.093", "49.6", "336.1", "355.4", "1.85"}, 
        {"Jupiter", "5.203", "0.048", "100.4", "14.3", "34.4", "1.30"}, 
        {"Saturn", "9.555", "0.056", "113.7", "93.1", "50.1", "2.49"}, 
        {"Uranus", "19.22", "0.046", "74.0", "173.0", "314.1", "0.77"}, 
        {"Neptune", "30.11", "0.009", "131.8", "48.1", "304.3", "1.77"} 
    };

/*
Planet a e Ω Π L i
Mercury 0.387 0.206 48.3◦ 77.46◦ 252.3◦ 7.00◦
Venus 0.723 0.007 76.7◦ 131.6◦ 182.0◦ 3.39◦
Earth 1.000 0.017 − 102.9◦ 100.5◦ 0.00◦
Mars 1.524 0.093 49.6◦ 336.1◦ 355.4◦ 1.85◦
Jupiter 5.203 0.048 100.4◦ 14.3◦ 34.4◦ 1.30◦
Saturn 9.555 0.056 113.7◦ 93.1◦ 50.1◦ 2.49◦
Uranus 19.22 0.046 74.0◦ 173.0◦ 314.1◦ 0.77◦
Neptune 30.11 0.009 131.8◦ 48.1◦ 304.3◦ 1.77◦
    
*/
    
/*
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        OrbitControl control = new OrbitControl(name, a, e, i, Ω, ω);
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
*/
    
}
