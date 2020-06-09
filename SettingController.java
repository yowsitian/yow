/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kangaroo.simulation.FX;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import static kangaroo.simulation.FX.HomeController.audioPlayer;
import kangaroo.simulation.audio.AudioPlayer;

/**
 * FXML Controller class
 *
 * @author user
 */
public class SettingController extends HomeController implements Initializable {
    ObservableList<String> selectMusic=FXCollections.observableArrayList("Default","Music 1","Music 2","Music 3");
    
    @FXML
    private Slider volumeSlider;


    @FXML
    private ChoiceBox selectionBox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        volumeSlider.setValue(audioPlayer.getVolume()*100);
        String temp=audioPlayer.getPath();
        if(temp.equals("Music_1"))
            temp="Default";
        else if(temp.equals("Music_2"))
            temp="Music 1";
        else if(temp.equals("Music_3"))
            temp="Music 2";
        else 
            temp="Music 3";
        selectionBox.setValue(temp);
        selectionBox.setItems(selectMusic);
    }    
    @FXML
    private Button saveChanges;
    @FXML
    void saveBMusic(MouseEvent event) {
        double vol=audioPlayer.getVolume();
        String temp=selectionBox.getValue().toString();
        audioPlayer.stop();
        if(temp.equals("Music 1"))
            temp="Music_2";
        else if(temp.equals("Music 2"))
            temp="Music_3";
        else if(temp.equals("Music 3"))
            temp="Music_4";
        else 
            temp="Music_1";
        audioPlayer = new AudioPlayer(temp, true);
        audioPlayer.play(true, AudioPlayer.INFINITY);
        audioPlayer.setVolume(vol);
        
    }

    @FXML
    void adjustVolume(MouseEvent event) {
        audioPlayer.setVolume(volumeSlider.getValue()/100.0);
    }

}
