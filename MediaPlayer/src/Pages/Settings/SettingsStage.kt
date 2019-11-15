package Pages.Settings

import Model
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.stage.WindowEvent
import src.SettingsModel

class SettingsStage(var settings : SettingsModel) : Stage() {
    var directoryPathTextBox : TextField

    init {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("Settings.fxml")

        val settingScene = loader.load<Parent>() // Get scene.
        scene = Scene(settingScene)

        val controller = loader.getController<SettingsController>() // Get and set controller.
        controller.setMainModel(this)

        directoryPathTextBox = (settingScene.lookup("#directoryPathTextBox") as TextField) // Get text box.
        directoryPathTextBox.text = settings.mainMusicDirectory.get()

        onCloseRequest = EventHandler<WindowEvent> {
            settings.save()
        }
    }
}