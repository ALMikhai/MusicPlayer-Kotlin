// 1)Добавить возможность открытия нескольких музыкальных файлов
// 2)Добавить возможность добавлять новые музыкальные файлы
// 3)Добавить компонент таблицы для отображения списка музыкальных файлов
// -1 столбец - номер трека
// -2 столбец - название файла
// -3 столбец - длина файла в секундах
// -По двойному клику на файл из списка должно начаться воспроизведение выбранного трека
// -Трек который играет должен визуально отличаться в списке
// 4)Добавить кнопки “Next” и “Prev” для переключения треков
// 5)Добавить возможность перемотки треков, нажатием на слайдер
// 6)Добавить слайдер для изменения громкости
// 7)Подгрузка музыки из папки.
// TODO 8)Вынести в отдельные функции добавление одной песни и папки с песнями(рефакторинг всего кода).
// TODO 9) Сделать удаление на кнопку delete.
// TODO 10) Сохранение плей-листа и подгрузка при след. запуске.

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import javafx.util.Duration
import java.io.File

class MPlayer : Application() {
    internal var windowHeight = 700.0
    internal var windowWidth = 550.0

    internal var musicPlaying : Music? = null
    internal var musicSelected : Music? = null
    internal var observableList : ObservableList<Music> = FXCollections.observableArrayList()
    internal var tableViewMusic : TableView<Music> = TableView(observableList)

    internal var selectedFile: File? = null
    internal var mPlayer: MediaPlayer? = null
    private var checkDurationMediaPlayer : MediaPlayer? = null

    internal var musicTimer : Text = Text()
    internal lateinit var musicSlider: Slider
    internal lateinit var volumeSlider : Slider

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
                var duration = checkDurationMediaPlayer!!.stopTime.toSeconds()
                music.setDuration((duration / 60).toInt().toString() + '.' + (duration % 60).toInt().toString())
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

        if(musicSelected != null && (musicPlaying != musicSelected || musicPlaying == null)){
            mPlayer?.stop()
            mPlayer?.dispose()
            mPlayer = MediaPlayer(musicSelected?.getMedia())
            musicSelected!!.setName("-> " + musicSelected!!.getName())
            if(musicPlaying != null) musicPlaying!!.setName(musicPlaying!!.getName().substringAfter(' '))
            musicPlaying = musicSelected
            musicSlider.value = 0.0
        }

        mPlayer?.play()
    }

    fun setNextMusic(){
        if(mPlayer == null) return
        musicSelected = observableList[(observableList.indexOf(musicPlaying) + 1) % observableList.count()]
        setMusicNow()
    }

    fun setPrevMusic(){
        if(mPlayer == null) return
        musicSelected = if(observableList.indexOf(musicPlaying) == 0)
            observableList[observableList.count() - 1]
        else
            observableList[(observableList.indexOf(musicPlaying) - 1)]

        setMusicNow()
    }

    fun deleteMusicNow(){
        if(mPlayer?.media == musicSelected?.getMedia()) {
            mPlayer?.stop()
            mPlayer?.dispose()
            mPlayer = null
            musicSlider.value = 0.0
        }

        observableList.remove(musicSelected)
    }

    fun getImage(path:String) : ImageView{
        val input = javaClass.getResourceAsStream(path)
        val image = Image(input)

        return ImageView(image)
    }

    fun updateSlider(){
        if(mPlayer != null){
            try{
                println(musicSlider.value)
                mPlayer!!.seek(Duration((mPlayer!!.stopTime.toMillis() / 100 * musicSlider.value)))
            }catch (e : MediaException){
                e.printStackTrace()
            }
        }
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

                                val pauseButton = object : Button("", getImage("resources/images/pause.png")){
                                    init {
                                        setOnAction {
                                            mPlayer?.pause()
                                        }
                                    }
                                }

                                val stopButton = object : Button("", getImage("resources/images/stop.png")) {
                                    init {
                                        setOnAction {
                                            mPlayer?.stop()
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

                                val nextButton = object : Button("", getImage("resources/images/next.png")){
                                    init {
                                        setOnAction {
                                            setNextMusic()
                                        }
                                    }
                                }

                                val prevButton = object : Button("", getImage("resources/images/prev.png")){
                                    init {
                                        setOnAction {
                                            setPrevMusic()
                                        }
                                    }
                                }

                                children.addAll(prevButton, playButton, pauseButton, nextButton, stopButton, deleteButton)
                            }
                        }
                        children.add(hbox)

                        val musicSelectionModel = tableViewMusic.selectionModel

                        musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                            musicSelected = newValue
                            println(musicSelected?.getName())
                        }

                        children.add(Label("Playlist"))

                        var pathColumn : TableColumn<Music, String> = TableColumn("Name")
                        pathColumn.minWidth = 450.0
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

                musicSlider = object : Slider(){
                    init {
                        onMouseClicked = EventHandler {
                            updateSlider()
                        }

                        minWidth = windowWidth
                    }
                }

                val sliderBox = object : VBox() {
                    init {
                        val volumeAndName = object :  HBox() {
                            init {
                                alignment = Pos.BOTTOM_LEFT

                                volumeSlider = object : Slider(){
                                    init {
                                        min = 0.0
                                        max = 1.0

                                        minHeight = 40.0

                                        orientation = Orientation.VERTICAL

                                        isShowTickMarks = true
                                        isShowTickLabels = true
                                        majorTickUnit = 0.25

                                        onMouseDragged = EventHandler {
                                            mPlayer?.volume  = volumeSlider.value
                                        }
                                    }
                                }
                                children.add(volumeSlider)
                                children.add(musicTimer)
                            }
                        }

                        children.add(volumeAndName)
                        children.add(musicSlider)
                    }
                }

                bottom = sliderBox
            }
        }

        Thread(Runnable {
            while (true) {
                if (mPlayer != null) {
                    try {
                        val currentTime = mPlayer?.currentTime!!.toSeconds()

                        var allTime = mPlayer?.stopTime!!.toSeconds()

                        var timeNow = currentTime * 100.0 / allTime

                        musicTimer.text =
                            (currentTime / 60).toInt().toString() + "." + (currentTime % 60).toInt().toString() + " / " + musicPlaying?.getDuration() + " " + musicPlaying?.getName()

                        musicSlider.value = timeNow
                        volumeSlider.value = mPlayer!!.volume
                        println("Cur time " + timeNow)
                    }catch (e : MediaException){
                        mPlayer?.dispose()
                        mPlayer = null
                    }
                }else{
                    musicTimer.text = ""
                }

                try {
                    Thread.sleep(1000)
                    tableViewMusic.refresh()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

        val scene = Scene(root, windowWidth, windowHeight)

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