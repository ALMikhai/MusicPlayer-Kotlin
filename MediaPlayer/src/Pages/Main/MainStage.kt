package Pages.Main

import Pages.Settings.SettingsController
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.chart.AreaChart
import javafx.scene.chart.BarChart
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage

class MainStage : Stage() {
    private var loader : FXMLLoader = FXMLLoader()

    init {
        loader.location = javaClass.getResource("Main.fxml")
        var mainScene = loader.load<Parent>()
        val controller = loader.getController<MainController>()
        controller.mainBlock = (mainScene.lookup("#mainVBox") as VBox)
        controller.musicSlider = (mainScene.lookup("#musicSlider") as Slider)
        controller.volumeSlider = (mainScene.lookup("#volumeSlider") as Slider)
        controller.musicTimer = (mainScene.lookup("#musicTimer") as Text)
        controller.musicName = (mainScene.lookup("#musicName") as Text)
        controller.spectrumChart = (mainScene.lookup("#spectrumChart") as AreaChart<String, Number>)
        controller.spectrumBarChart = (mainScene.lookup("#spectrumBarChart") as BarChart<String, Number>)
        controller.init()

        scene = Scene(mainScene)
    }
}