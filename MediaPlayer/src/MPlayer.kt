// 1)Добавить возможность открытия нескольких музыкальных файлов
// 2)Добавить возможность добавлять новые музыкальные файлы
// 3)Добавить компонент таблицы для отображения списка музыкальных файлов
// -1 столбец - номер трека
// -2 столбец - название файла
// -3 столбец - длина файла в секундах
// -По двойному клику на файл из списка должно начаться воспроизведение выбранного трека
// TODO -Трек который играет должен визуально отличаться в списке
// TODO 4)Добавить кнопки “Next” и “Prev” для переключения треков
// TODO 5)Добавить возможность перемотки треков, нажатием на слайдер
// TODO 6)Добавить слайдер для изменения громкости
// 7)Подгрузка музыки из папки (Эта сука не вмещает больше 63 треков).
// TODO 8)Вынести в отдельные функции добавление одной песни и папки с песнями.

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.io.File

class MPlayer : Application() {
    internal var musicNow : Music? = null
    internal var observableList : ObservableList<Music> = FXCollections.observableArrayList()
    internal var tableViewMusic : TableView<Music> = TableView(observableList)

    internal var selectedFile: File? = null
    internal var mplayer: MediaPlayer? = null
    private var checkDurationMediaPlayer : MediaPlayer? = null

    internal var musicTimer : Text = Text()
    internal var musicSlider: Slider = Slider()

    fun addNewMusic(url:String) : Music{
        var music = Music(url)
        observableList.add(music)
        return music
    }

    fun checkMusicDuration(musics: ArrayList<Music>){
        if(musics.isNotEmpty()){
            var music = musics.first()
            checkDurationMediaPlayer = MediaPlayer(music.getMedia())

            checkDurationMediaPlayer!!.onReady = Runnable {
                music.setDuration(checkDurationMediaPlayer!!.cycleDuration.toString())
                musics.remove(music)
                checkDurationMediaPlayer!!.dispose()
                checkMusicDuration(musics)
            }
        }
    }

    fun setMusicNow(){
        if(observableList.isEmpty()){
            return
        }

        if(musicNow != null && mplayer?.media != musicNow?.getMedia()) {
            mplayer?.stop()
            mplayer = MediaPlayer(musicNow?.getMedia())
            musicSlider.value = 0.0
        }

        mplayer?.play()
    }

    fun deleteMusicNow(){
        if(mplayer?.media == musicNow?.getMedia()) {
            mplayer?.stop()
            mplayer?.dispose()
            mplayer = null
            musicSlider.value = 0.0
        }

        observableList.remove(musicNow)
    }

    fun getImage(path:String) : ImageView{
        val input = javaClass.getResourceAsStream(path)
        val image = Image(input)
        val imageView = ImageView(image)

        return imageView
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = object : BorderPane() {
            init {
                val filenameLabel = Label("Media control")
                val fileChooser = FileChooser()
                fileChooser.title = "Open File"
                fileChooser.extensionFilters.addAll(
                    ExtensionFilter("Audio Files", "*.wav", "*.mp3")
                )

                val folderChooser = DirectoryChooser()
                folderChooser.title = "Open Folder"

                val vbox = object : VBox() {
                    init {
                        children.add(filenameLabel)
                        val hbox = object : HBox() {
                            init {

                                val playButton = object : Button("", getImage("resources/images/play.png")){
                                    init {
                                        setOnAction {
                                            setMusicNow()
                                        }
                                    }
                                }

                                val pauseButton = object : Button("", getImage("resources/images/pause1.png")){
                                    init {
                                        setOnAction {
                                            mplayer?.pause()
                                        }
                                    }
                                }

                                val stopButton = object : Button("", getImage("resources/images/stop1.png")) {
                                    init {
                                        setOnAction {
                                            mplayer?.stop()
                                        }
                                    }
                                }

                                val deleteButton = object : Button("Delete"){
                                    init {
                                        setOnAction {
                                            deleteMusicNow()
                                        }
                                    }
                                }

                                children.addAll(playButton, pauseButton, stopButton, deleteButton)
                            }
                        }
                        children.add(hbox)

                        val musicSelectionModel = tableViewMusic.selectionModel

                        musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                            musicNow = newValue
                            println(musicNow?.getName())
                        }

                        children.add(Label("Playlist"))

                        var pathColumn : TableColumn<Music, String> = TableColumn("Name")
                        pathColumn.minWidth = 350.0
                        pathColumn.cellValueFactory = PropertyValueFactory<Music, String>("Name")
                        tableViewMusic.columns.add(pathColumn)

                        var durationColumn : TableColumn<Music, String> = TableColumn("Duration")
                        durationColumn.cellValueFactory = PropertyValueFactory<Music, String>("Duration")
                        tableViewMusic.columns.add(durationColumn)

                        tableViewMusic.setOnMouseClicked {e ->
                            if(e.clickCount == 2){
                                setMusicNow()
                            }
                        }
                        children.add(tableViewMusic)
                    }
                }
                center = vbox

                val menubar = object : MenuBar() {
                    init {
                        val menu = object : Menu("File") {
                            init {
                                val addMediaButton = object : MenuItem("Add media") {
                                    init {
                                        setOnAction {
                                            selectedFile = fileChooser.showOpenDialog(primaryStage)
                                            if (selectedFile != null) {
                                                var url = selectedFile!!.toURI()
                                                println(url.toString())
                                                var listNewMusic : ArrayList<Music> = arrayListOf()
                                                listNewMusic.add(addNewMusic(url.toString()))
                                                checkMusicDuration(listNewMusic)
                                            }
                                        }
                                    }
                                }
                                val addMediaFromFolderButton = object  : MenuItem("Add from folder"){
                                    init{
                                        setOnAction {
                                            selectedFile = folderChooser.showDialog(primaryStage)
                                            if (selectedFile != null) {
                                                var uri = selectedFile!!.toURI().toString()
                                                var paths = FolderReader(uri.substringAfter('/')).read()
                                                var listNewMusic : ArrayList<Music> = arrayListOf()

                                                paths.forEach {
                                                    var expansion =  it.substringAfterLast('.')
                                                    if(expansion == "mp3" || expansion == "wav"){
                                                         listNewMusic.add(addNewMusic((uri + it.substringAfterLast('\\')).replace(" ", "%20")))
                                                    }
                                                }
                                                checkMusicDuration(listNewMusic)
                                            }
                                        }
                                    }
                                }
                                items.addAll(addMediaButton, addMediaFromFolderButton)
                            }
                        }
                        menus.add(menu)
                    }
                }
                top = menubar

                val sliderBox = object : VBox() {
                    init {
                        children.add(musicTimer)
                        children.add(musicSlider)
                    }
                }

                bottom = sliderBox
            }
        }

        Thread(Runnable {
            while (true) {
                if (mplayer != null) {
                    val currentTime = mplayer?.currentTime!!.toSeconds()

                    var allTime =  mplayer?.stopTime!!.toSeconds()

                    var timeNow = currentTime * 100.0 / allTime

                    musicTimer.text = (currentTime / 60).toInt().toString() + "." + (currentTime % 60).toInt().toString() + " / " + musicNow?.getDuration() + " ~ " + musicNow?.getName()

                    musicSlider.value = timeNow
                    println("Cur time " + timeNow)
                }
                try {
                    Thread.sleep(900)
                    tableViewMusic.refresh()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

        val scene = Scene(root, 500.0, 700.0)

        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MPlayer::class.java)
        }
    }
}