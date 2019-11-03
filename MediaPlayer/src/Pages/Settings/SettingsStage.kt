package Pages.Settings

import Model
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.stage.Stage

class SettingsStage(model : Model) : Stage() {
    var mainModel : Model = model
    var directoryPathTextBox : TextField

    init {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("Settings.fxml")

        val settingScene = loader.load<Parent>() // Get scene.
        scene = Scene(settingScene)
        val controller = loader.getController<SettingsController>() // Get and set controller.
        controller.setMainModel(this)
        directoryPathTextBox = (settingScene.lookup("#directoryPathTextBox") as TextField) // Get text box.
    }
}