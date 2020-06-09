package kangaroo.simulation.FX;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import kangaroo.simulation.audio.AudioPlayer;

public class HomeController implements Initializable {

    @FXML
    private VBox sideBar;
    @FXML
    private Button home;
    @FXML
    private Button simulator;
    @FXML
    private Button dashboard;
    @FXML
    private Button help;
    @FXML
    private Button setting;
    @FXML
    private Pane homeScene;
    @FXML
    private Canvas displayHome;
    @FXML
    private HBox topBar;
    @FXML
    private ImageView close;
    @FXML
    private ImageView minimize;
    @FXML
    private ImageView sideTransition;
    @FXML
    private BorderPane container;
    
    private Image bgImage;
    private static final String HOMEFXML = "/kangaroo/simulation/FX/HOME.fxml";
    private static final String POINTCONFIGFXML = "/kangaroo/simulation/FX/PointGenerator.fxml";
    private static final String KANGAROOCONFIGRFXML = "/kangaroo/simulation/FX/KangarooGenerator.fxml";
    private static final String SIMULATORFXML = "/kangaroo/simulation/FX/Simulator.fxml";
    private static final String DASHBOARDFXML = "/kangaroo/simulation/FX/Dashboard.fxml";
    private static final String HELPFXML = "/kangaroo/simulation/FX/Help.fxml";
    private static final String SETTINGFXML = "/kangaroo/simulation/FX/Setting.fxml";
    
    public static final Integer HOME = 1;
    public static final Integer SIMULATOR = 2;
    public static final Integer DASHBOARD = 3;
    public static final Integer HELP = 4;
    public static final Integer SETTING = 5;
    
    public static final Integer POINTGENERATOR = 1;
    public static final Integer KANGAROOGENRATOR = 2;
    public static final Integer SIMULATION = 3;
    public static final Integer SETTINGGENERATOR = 4;
    
    public static final Integer SCENEWIDTH = 1050;
    public static final Integer SCENEHEIGHT = 800;
    
    public static Parent homeRoot;
    public static Parent pointConfigRoot;
    public static Parent kangarooConfigRoot;
    public static Parent simulatorRoot;
    public static Parent dashboardRoot;
    public static Parent helpRoot;
    public static Parent settingRoot;
    
    public static AudioPlayer audioPlayer;
    
    public static BorderPane currentContainer;
    public static Node currentRoot;
    
    public static int currentScene = 1;
    public static int currentSimulatorScene = 1;
    
    public static boolean isExpand = false;
    public static boolean isTranslating = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext graphic = displayHome.getGraphicsContext2D();
        bgImage = new Image("/resources/background/homeBackground.jpg");
        graphic.drawImage(bgImage, (bgImage.getWidth()-displayHome.getWidth())/2, -30, displayHome.getWidth(), displayHome.getHeight(), 0, 0, displayHome.getWidth(), displayHome.getHeight());
        audioPlayer = new AudioPlayer("Music_1", true);
        audioPlayer.play(true, AudioPlayer.INFINITY);
        audioPlayer.setVolume(0.5);
        init();
    }
    
    @FXML
    private void displayHome(ActionEvent event) {
        if(currentScene == HOME || isTranslating) return;
        parallelTransition(homeRoot);
        currentScene = HOME;
    }
    
    @FXML
    private void simulatorController(ActionEvent event) {
        if(currentScene == SIMULATOR || isTranslating) return;
        
        if(currentSimulatorScene == POINTGENERATOR) {showPointGenerator(); return;}
        if(currentSimulatorScene == KANGAROOGENRATOR) {showKangarooGenerator(); return;}
        if(currentSimulatorScene == SIMULATION) showSimulator();
    }
    
    public void showPointGenerator() {
        slideTansition(pointConfigRoot, SCENEWIDTH, 0, true);
        currentScene = SIMULATOR;
        currentSimulatorScene = POINTGENERATOR;
    }
    
    public void showKangarooGenerator() {
        slideTansition(kangarooConfigRoot, SCENEWIDTH, 0, true);
        currentScene = SIMULATOR;
        currentSimulatorScene = KANGAROOGENRATOR;
    }

    public void showSimulator() {
        slideTansition(simulatorRoot, SCENEWIDTH, 0, true);
        currentScene = SIMULATOR;
        currentSimulatorScene = SIMULATION;
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        if(currentScene == DASHBOARD || isTranslating) return;
        
        currentScene = DASHBOARD;
    }

    @FXML
    private void showHelp(ActionEvent event) {
        if(currentScene == HELP || isTranslating) return;
        
        currentScene = HELP;
    }

    @FXML
    private void showSetting(ActionEvent event) {
        if(currentScene == SETTING || isTranslating) return;
        parallelTransition(settingRoot);
        currentScene = SETTING;
    }

    @FXML
    private void close(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    private void sideTranslation(MouseEvent event) {
        ObservableList<Node> childrens = sideBar.getChildren();
        if(!isExpand) {
            for(int i = 0; i < childrens.size(); i++) childrens.get(i).setTranslateX(-186);
            sideBar.setPrefWidth(64);
            sideTransition.setImage(new Image("/resources/Icons/expand.png"));
            isExpand = true;
        }else {
            for(int i = 0; i < childrens.size(); i++) childrens.get(i).setTranslateX(0);
            sideBar.setPrefWidth(250);
            sideTransition.setImage(new Image("/resources/Icons/contract.png"));
            isExpand = false;
        }
    }
    
    @FXML
    private void updateTransition(MouseEvent event) {
        if(currentScene == HOME) {
            displayHome.setWidth(homeScene.getWidth());
            displayHome.setHeight(homeScene.getHeight());
            GraphicsContext graphic = displayHome.getGraphicsContext2D();
            graphic.drawImage(bgImage, (bgImage.getWidth()-displayHome.getWidth())/2, -30, displayHome.getWidth(), displayHome.getHeight(), 0, 0, displayHome.getWidth(), displayHome.getHeight()); 
            return;
        }
    }
    
    private void init() {
        try {
            homeRoot = homeScene;
            currentContainer = container;
            pointConfigRoot = FXMLLoader.load(getClass().getResource(POINTCONFIGFXML));
            kangarooConfigRoot = FXMLLoader.load(getClass().getResource(KANGAROOCONFIGRFXML));
            simulatorRoot = FXMLLoader.load(getClass().getResource(SIMULATORFXML));
            settingRoot = FXMLLoader.load(getClass().getResource(SETTINGFXML));
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //======================================TRANSITION=======================================//
    
    public void slideTansition (Parent root, int fromValue, int toValue, boolean translateX) {
        isTranslating = true;
        if(translateX) root.translateXProperty().set(fromValue);
        else root.translateYProperty().set(fromValue);
        
        Node previousRoot = currentContainer.getCenter();
        
        currentContainer.setCenter(root);
        currentRoot = currentContainer.getCenter();
        Timeline timeLine = new Timeline();
        KeyValue keyValue = new KeyValue(((translateX)? root.translateXProperty() : root.translateYProperty()), toValue, Interpolator.EASE_IN);
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), keyValue);
        timeLine.getKeyFrames().add(keyframe);
        timeLine.play();
        
        timeLine.setOnFinished((event1) -> {
            currentContainer.getChildren().remove(previousRoot);
            isTranslating = false;
        });
    }
    
    public void parallelTransition(Node in) {
        isTranslating = true;
        Node out = currentContainer.getCenter();
        
        in.translateXProperty().set(SCENEWIDTH);
        currentContainer.setCenter(in);
        currentRoot = currentContainer.getCenter();
        TranslateTransition timeline1 = new TranslateTransition(Duration.seconds(1), in);
        TranslateTransition timeline2 = new TranslateTransition(Duration.seconds(1), out);
        timeline1.setToX(0);
        timeline2.setToX(SCENEWIDTH);
        
        ParallelTransition parallelTransition = new ParallelTransition(timeline2, timeline1);
        parallelTransition.play();
        
        parallelTransition.setOnFinished((event) -> {
            currentContainer.getChildren().remove(out);
             isTranslating = false;
        });
    }
    
    public void fadeTransition(Node node, int fromValue, int toValue) {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.seconds(1));
        fade.setNode(node);
        fade.setFromValue(fromValue);
        fade.setToValue(toValue);
        fade.play();
    }
    
    public void loadScene(String FXML) {
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource(FXML));
            Node currentScene = currentContainer.getCenter();
            currentContainer.getChildren().add(root);
            currentContainer.getChildren().remove(currentScene);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
