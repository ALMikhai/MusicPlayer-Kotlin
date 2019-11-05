import UI.MusicTableGeneration
import UI.SpectrumChartGeneration
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.canvas.Canvas
import javafx.scene.chart.AreaChart
import javafx.scene.chart.BarChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.control.TableView
import javafx.scene.layout.VBox
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.util.Duration
import java.io.File

open class Model() {
    protected var primaryStage = Stage()

    protected var musicPlaying : Music? = null
    protected var selectedFile: File? = null
    protected var mPlayer: MediaPlayer? = null
    protected var checkDurationMediaPlayer : MediaPlayer? = null
    internal var musicSelected : Music? = null
    internal var observableList : ObservableList<Music> = FXCollections.observableArrayList()
    internal var spectrumData = XYChart.Series<String, Number>()

    protected val fileChooser = FileChooser()
    protected val folderChooser = DirectoryChooser()

    protected var tableViewMusic : TableView<Music> = MusicTableGeneration.init(this)
    internal lateinit var mainBlock : VBox
    internal lateinit var musicTimer : Text
    internal lateinit var musicName : Text
    internal lateinit var musicSlider : Slider
    internal lateinit var volumeSlider : Slider
    internal var spectrumBarChart = SpectrumChartGeneration.init()
}