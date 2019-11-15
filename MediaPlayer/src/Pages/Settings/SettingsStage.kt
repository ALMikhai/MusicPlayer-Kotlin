package Pages.Settings

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import javafx.stage.WindowEvent
import src.SettingsModel

    class SettingsStage(var settings : SettingsModel) : Stage() {
    var directoryPathLabel : Label

    init {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("Settings.fxml")

        val settingScene = loader.load<Parent>() // Get scene.
        scene = Scene(settingScene)

        val controller = loader.getController<SettingsController>() // Get and set controller.
        controller.setMainModel(this)

        directoryPathLabel = (settingScene.lookup("#directoryPathLabel") as Label) // Get text box.
        directoryPathLabel.text = settings.mainMusicDirectory.get()

        onCloseRequest = EventHandler<WindowEvent> {
            settings.save()
        }

        directoryPathLabel.onMouseClicked = EventHandler {
            val directoryChooser = DirectoryChooser()
            var directory = directoryChooser.showDialog(this)
            if(directory != null){
                directoryPathLabel.text = directory.toURI().toString()
            }
        }
    }
}