/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Calendar;
import javax.swing.JFrame;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.JPanel;
import javax.vecmath.Vector3d;
import my.app.nodes.Node;
import my.app.nodes.PlanetNode;
import my.app.scene.controls.Control;
import my.app.scene.controls.OrbitControl;

/**
 *
 * @author Peter
 */
public class OrbitTest extends JPanel implements Runnable{

    /**
     * @param args the command line arguments
     */
    static JFrame frame;
    public static void main(String[] args) {
        // TODO code application logic here
        frame = new JFrame("OrbitTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultLookAndFeelDecorated(true);
		//frame.setMinimumSize(new Dimension(4 * 300, 3 * 300));//
		frame.setBounds(150, 50, 1000, 750);//x, y, w, h
		//setSize(new Dimension(4 * 200, 3 * 200));	//600, 800
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
        
        OrbitTest app = new OrbitTest();
        frame.getContentPane().add(app);
        
        app.init();
        app.start();
    }
    private Node rootNode = new Node("rootNode");
	private Image offscreenImage;
	private Graphics offscr;
    private Thread ticker;
    private boolean running = false;
    private long fps = 30L;
    //private float tpf = 0f;
    private long lastTime = 0L;
    private double year = 365.25d;
    private double month = year/12d;
    private long time = 0L;
    private Calendar calendar;
    private boolean trailing = true;
    private boolean clearScreen = true;
    
    OrbitTest(){
        //super.setDimension(new Dimension(500, 500));
        rootNode = addBodies(rootNode);
        setFPS(30);
        setVisible(true);
    }
    
        public void init(){
            System.out.println("init()");
            setBackground(Color.black);
    		setBounds(50, 50, 500, 500);//x, y, w, h
            //doResize();
            calendar = Calendar.getInstance();
            time = Calendar.getInstance().getTimeInMillis();
            doMakeOffscr();
            
        }
        
        public void start(){
		if (ticker==null || !ticker.isAlive()){
			ticker = new Thread(this);
        }
			ticker.start();
			running = true;
        }
        
        
        public void stop(){
            running = false;
        }
        
        
        public void setFPS(long fps){
            this.fps = fps;
        }
        
        
        public float getTPF(){
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long tpf = currentTime - lastTime;
            lastTime = currentTime;
            //System.out.println("OrbitTest.getTPF() " + "currentTime=" + currentTime + ", lastTime=" + lastTime + ",tpf=" + tpf);
            return (tpf);
        }
        
        
        public void run(){
            while (running){
                input();
                move(getTPF());
 				//repaint();
                draw();
				try{
					ticker.sleep(1000/fps);
				} catch (InterruptedException e){}
               
            }
        }
        
        
        public void input(){
            
        }
        
        
        public void move(float tpf){
            //System.out.println("OrbitTest.move() ");
            time += 0.00001;
            for (Object object : rootNode.getChildren()){
            //for (int i = 0;i < planetNode.getChildrenCount();i++){
                //PlanetNode child = (PlanetNode)rootNode.getChild(i);
                move2((PlanetNode)object, new Vector3d(), tpf);
            }
        }
        
        
        public void move2(PlanetNode node, Vector3d pTrans, float tpf){
            //node.move(tpf);
            for (Object control : node.getControls()){
                Vector3d translation = ((OrbitControl)control).getTimeTranslation(time, pTrans);
                ((OrbitControl)control).update(tpf);
            }
            //
            for (Object object : node.getChildren()){
            //for (int i = 0;i < node.getChildrenCount();i++){
                //PlanetNode child = (PlanetNode)node.getChild(i);
                move2((PlanetNode)object, node.getWorldTranslation(), tpf);
            }
                
            
        }
        
        
        public void draw(){
            //System.out.println("OrbitTest.draw() ");
            update(this.getGraphics());
            
        }
        
        
        public void paintObjects(Graphics g){//offscr
            for (Object object : rootNode.getChildren()){
            //for (int i = 0;i < planetNode.getChildrenCount();i++){
                //PlanetNode child = (PlanetNode)rootNode.getChild(i);
                paintObjects2((PlanetNode)object, new Vector3d(), g);
            }
        }
        
        
        private void paintObjects2(PlanetNode planetNode, Vector3d pTrans, Graphics g){//offscr
            double radius = planetNode.getRadius();
            Vector3d translation = planetNode.getTranslation(pTrans);
            Color color = planetNode.getColor();
            g.setColor(color);
            g.fillOval((int)(translation.x - (radius/2d)), (int)(translation.z - (radius/2d)), (int)(radius), (int)(radius));
            //
            for (Object object : planetNode.getChildren()){
            //for (int i = 0;i < planetNode.getChildrenCount();i++){
                //PlanetNode child = (PlanetNode)rootNode.getChild(i);
                paintObjects2((PlanetNode)object, translation, g);
            }
        }


	@Override
	public void update(Graphics g){
  		g.setColor(getBackground());
       	g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
		paint(g);
	}
	
    
	@Override
	public void paint(Graphics g){
		//super.paint(g);
        //System.out.println("painting");
        if (offscr == null){
            System.out.println("OrbitTest.doMakeOffscr() " + "w=" + getWidth() + ",h=" + getHeight());
            offscreenImage = createImage(getWidth(), getHeight());
            offscr = offscreenImage.getGraphics();
        }
        if (clearScreen){
            offscr.setColor(getBackground());
            offscr.fillRect(0,0, getWidth(), getHeight());
            offscr.setColor(getForeground());
            clearScreen = trailing;
        }

        offscr.translate(getWidth()/2, getHeight()/2);
        //
        paintObjects(offscr);
        //
        offscr.translate(-getWidth()/2, -getHeight()/2);
        g.drawImage(offscreenImage, 0, 0, null);
	}//end paint
    
    
    public void doResize(){
        double fw = 1d, fh = 1d;//changes the length of a line by a factor
        double r = getWidth();

        int ex = 0, ey = 0;			//end of line
        double dw = 0d, dh = 0d;

        //System.out.println("RadarPanel.paint()");
        //System.out.println("RadarPanel.paint()-(myinit=" + myinit + ")");
        //System.out.println("Repainting radar (width=" + getWidth() + ", height=" + getHeight() + ")");
        dw = (double) getWidth();
        dh = (double) getHeight();
        r = getWidth();//or height

        if (getWidth() < getHeight()){
            fh = dh/dw;
            r = getWidth()/2 * 0.8;//half width/height * 0.8
        }
        if (getWidth() > getHeight()){
            fw = dw/dh;
            r = getHeight()/2 * 0.8;//half width/height * 0.8
        }

        //create offscr if necessary
    }
    
    
    public void doMakeOffscr(){
        if (offscr == null){
            System.out.println("OrbitTest.doMakeOffscr() " + "w=" + getWidth() + ",h=" + getHeight());
            offscreenImage = createImage(getWidth(), getHeight());
            offscr = offscreenImage.getGraphics();
        }
        
    }
    
    
    private Node addBodies(Node rootNode){
        //for (Spatial child:(Spatial[])((Node) parent).getChildren().toArray()){
        //for (String[] s : body){
        //for (int i = 0;i < body[0].length;i++){
        //    PlanetNode p = getBody(body[i]);
        //    rootNode.add(p);
        //}
        PlanetNode sun = new PlanetNode(body[0]);
        sun.addControl(new OrbitControl(body[0][0], Double.valueOf(body[0][2]), Double.valueOf(body[0][3]), Double.valueOf(body[0][4])));
        PlanetNode earth = new PlanetNode(body[1]);
        earth.addControl(new OrbitControl(body[1][0], Double.valueOf(body[1][2]), Double.valueOf(body[1][3]), Double.valueOf(body[1][4])));
        PlanetNode moon = new PlanetNode(body[2]);
        moon.addControl(new OrbitControl(body[2][0], Double.valueOf(body[2][2]), Double.valueOf(body[2][3]), Double.valueOf(body[2][4])));
        
        rootNode.add(sun);
        sun.add(earth);
        earth.add(moon);
        return rootNode;
    }
    
    
    private PlanetNode getBody(String[] data){
        PlanetNode p = null;
            
        return p;
    }
    
    
    String[][] body = new String[][]
    {//  name        parent            a       e       i       T         m       r
        {"Sun",      "rootNode",     "0",    "0",    "0",    "0",   "1000", "50"},
        {"Earth",    "Sun",        "200",  "0.1",    "5",   "10",    "100",  "25"},
        {"Moon",     "Earth",       "50",  "0.1",    "5",    "0",     "50",   "5"},
    };


        
}
