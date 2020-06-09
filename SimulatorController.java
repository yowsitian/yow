package kangaroo.simulation.FX;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import kangaroo.simulation.animal.Kangaroo;
import kangaroo.simulation.utils.Assets;
import kangaroo.simulation.utils.Camera;
import kangaroo.simulation.utils.Handler;
import kangaroo.simulation.world.World;

public class SimulatorController implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private Canvas canvas;
    
    public static int canvasWidth;
    public static int canvasHeight;
    public static ArrayList<Kangaroo> kangaroos;
    public static World world;
    public static boolean start;
    public static boolean hasInitialized;
    
    private Camera camera;
    private Handler handler;
    
    private GraphicsContext graphic;
    private boolean isRunning;
    private int speed;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateCanvasDimension();
        init();
        run();
    }
    
    public void init() {
        kangaroos = new ArrayList<>();
        graphic = canvas.getGraphicsContext2D();
        isRunning = true;
        speed = 60;
        
        handler = new Handler(this);
        camera = new Camera(handler, 0 ,0);
//        Assets.init();
    }
    
    public void mapInit() {
        world = new World();
        try {
            Scanner pointInput = new Scanner(new FileInputStream("pointConfig.txt"));
            Scanner kangarooInput = new Scanner(new FileInputStream("kangarooConfig.txt"));
            
            while(pointInput.hasNextLine()) {
                Integer[] pointsInfo = stringToInt(pointInput.nextLine().split(" "));
                if(pointsInfo.length == 4) {
                    int ID = pointsInfo[0];
                    world.addPoint(ID);
                    world.getPoint(ID).setInfo(pointsInfo[1], pointsInfo[2], pointsInfo[3]);
                }else {
                    world.addPath(pointsInfo[0], pointsInfo[1], pointsInfo[2]);
                }
            }
            
            while(kangarooInput.hasNextLine()) {
                String[] kangaroosInfo = kangarooInput.nextLine().split(" ");
                kangaroos.add(new Kangaroo(Integer.parseInt(kangaroosInfo[0]), Integer.parseInt(kangaroosInfo[1]), kangaroosInfo[2].charAt(0) , world));
            }
            
            world.thresholdValue(KangarooGeneratorController.currentThreshold);
            world.displayPointsInfo();
            hasInitialized = true;
            System.out.println("Has initialized");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimulatorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        new AnimationTimer() {
            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            double delta = 0.0;
            double nanoTime = 1000000000.0 / speed;
            int updates = 0;
            int frames = 0;
            
            @Override
            public void handle(long now) {
                if(start && !hasInitialized) mapInit();
                
                delta += (now - lastTime) / nanoTime;
                lastTime = now;
                
                if(delta >= 1.0) {
                    update();
                    updates ++;
                    delta--;
                }
                
                draw();
                frames++;
                if(System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    System.out.println("UPS: " + updates + "\t" + "FPS: " + frames);
                    updates = 0;
                    frames = 0;
                }
            }
        }.start();
    }
    
    public void update() {
        
    }
    
    public void draw() {
        Image point = new Image("/resources/simulator/point.png");
        graphic.drawImage(point, 50, 50, 32, 32);
    }
    
    public void updateCanvasDimension() {
        canvasWidth = (int) canvas.getWidth();
        canvasHeight = (int) canvas.getHeight();
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    private Integer[] stringToInt(String[] arr) {
        Integer[] num = new Integer[arr.length];
        for(int i = 0; i < arr.length; i++) {
            num[i] = Integer.parseInt(arr[i]);
        }
        return num;
    }
    
    public Camera getCamera() {
            return camera;
    }

    public static int getWidth() {
        return canvasWidth;
    }

    public static int getHeight() {
        return canvasHeight;
    }
}
