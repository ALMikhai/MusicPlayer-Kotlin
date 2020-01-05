package src.UI

import Model
import Music
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import src.Mp3Metadata
import src.Mp3MetadataFromBD
import src.MusicDBController

class MusicFromDirectoryTable {
    companion object {
        fun createTable(list : ObservableList<Mp3MetadataFromBD>) : TableView<Mp3MetadataFromBD> {
            var table = TableView(list)

            val TitleColumn: TableColumn<Mp3MetadataFromBD, String> = TableColumn("Title")
            TitleColumn.prefWidth = 460.0
            TitleColumn.cellValueFactory = PropertyValueFactory<Mp3MetadataFromBD, String>("Title")
            table.columns.add(TitleColumn)

            val AuthorColumn: TableColumn<Mp3MetadataFromBD, String> = TableColumn("Author")
            AuthorColumn.prefWidth = 200.0
            AuthorColumn.cellValueFactory = PropertyValueFactory<Mp3MetadataFromBD, String>("Author")
            table.columns.add(AuthorColumn)

            val AlbumeColumn: TableColumn<Mp3MetadataFromBD, String> = TableColumn("Albume")
            AlbumeColumn.prefWidth = 200.0
            AlbumeColumn.cellValueFactory = PropertyValueFactory<Mp3MetadataFromBD, String>("Albume")
            table.columns.add(AlbumeColumn)

            val GenreColumn: TableColumn<Mp3MetadataFromBD, String> = TableColumn("Genre")
            GenreColumn.prefWidth = 200.0
            GenreColumn.cellValueFactory = PropertyValueFactory<Mp3MetadataFromBD, String>("Genre")
            table.columns.add(GenreColumn)

            val DurationColumn: TableColumn<Mp3MetadataFromBD, String> = TableColumn("Duration")
            DurationColumn.prefWidth = 100.0
            DurationColumn.cellValueFactory = PropertyValueFactory<Mp3MetadataFromBD, String>("Duration")
            table.columns.add(DurationColumn)

            return table
        }

        fun init(model: Model) : VBox {
            if (model.MusicMetadataList != null)
                model.MusicMetadataList!!.clear()

            model.MusicMetadataList = MusicDBController().selectAll()
            var table = createTable(model.MusicMetadataList!!)

            val musicSelectionModel = table.selectionModel
            musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                model.player.addNewMusic(Music("file:/" + newValue.Path.replace(" ", "%20").replace('\\', '/')))
            }

            var vBox = VBox()
            var hBox = HBox()
            var textField =  TextField()
            var searchButton = Button()
            searchButton.text = "Search"
            searchButton.onAction = EventHandler {
                var newMusicMetaDataList : ObservableList<Mp3MetadataFromBD> = FXCollections.observableArrayList()
                model.MusicMetadataList!!.forEach {
                    if (it.Albume.contains(textField.text) ||
                        it.Author.contains(textField.text) ||
                        it.Genre.contains(textField.text) ||
                        it.Title.contains(textField.text) ||
                        it.Path.contains(textField.text)) {
                        newMusicMetaDataList.add(it)
                    }
                }
                vBox.children.remove(table)
                table = createTable(newMusicMetaDataList)
                table.selectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                    model.player.addNewMusic(Music("file:/" + newValue.Path.replace(" ", "%20").replace('\\', '/')))
                }
                vBox.children.add(table)
            }

            hBox.children.addAll(textField, searchButton)
            vBox.children.addAll(hBox, table)

            return vBox
        }
    }
}