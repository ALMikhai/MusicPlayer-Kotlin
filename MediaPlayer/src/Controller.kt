import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Stage

class Controller(private var model: Model, primaryStage: Stage) {

    var addNewMediaEvent = EventHandler<ActionEvent>(){
        model.selectedFile = model.fileChooser.showOpenDialog(primaryStage)
        if (model.selectedFile != null) {
            var url = model.selectedFile!!.toURI()
            println(url.toString())
            var listNewMusic : ArrayList<Music> = arrayListOf()
            listNewMusic.add(model.addNewMusic(url.toString()))
            model.checkMusicDuration(listNewMusic)
        }
    }

    var addNewFolderEvent = EventHandler<ActionEvent>(){
        model.selectedFile = model.folderChooser.showDialog(primaryStage)
        if (model.selectedFile != null) {
            var uri = model.selectedFile!!.toURI().toString()
            var paths = FolderReader(uri.substringAfter('/').replace("%20", " ")).read()
            var listNewMusic : ArrayList<Music> = arrayListOf()

            paths.forEach {
                var expansion =  it.substringAfterLast('.')
                if(expansion == "mp3" || expansion == "wav"){
                    listNewMusic.add(model.addNewMusic((uri + it.substringAfterLast('\\')).replace(" ", "%20")))
                }
            }
            model.checkMusicDuration(listNewMusic)
        }
    }

    var musicSliderClickEvent = EventHandler<javafx.scene.input.MouseEvent>() {
        model.updateSlider()
    }

    var volumeSliderDraggedEvent = EventHandler<javafx.scene.input.MouseEvent>() {
        model.mPlayer?.volume  = model.volumeSlider.value
    }

    var keyPressEvent = EventHandler<KeyEvent>() {
        when(it.code){
            KeyCode.DELETE -> model.deleteMusicNow()
        }
    }

    companion object {
        fun installControl(view: View) {
            installTableControl(view)
        }

        private fun installTableControl(view: View){
            val musicSelectionModel = view.tableViewMusic.selectionModel
            musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                view.musicSelected = newValue
                println(view.musicSelected?.getName())
            }

            view.tableViewMusic.setOnMouseClicked {e ->
                if(e.clickCount == 2){
                    view.setMusicNow()
                }
            }
        }
    }
}