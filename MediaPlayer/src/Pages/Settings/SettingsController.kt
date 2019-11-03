package Pages.Settings

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class SettingsController(){
    private lateinit var _stage : SettingsStage

    fun setMainModel(mainStage : SettingsStage){
        _stage = mainStage
    }

    fun onApplyClick(){
        // TODO Change directory.
        println(_stage.directoryPathTextBox.text)
    }
}
