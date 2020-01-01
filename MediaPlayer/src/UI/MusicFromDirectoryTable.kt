package src.UI

import Model
import Music
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import src.Mp3Metadata
import src.Mp3MetadataFromBD
import src.MusicDBController

class MusicFromDirectoryTable {
    companion object {
        fun init(model: Model) : TableView<Mp3MetadataFromBD> {
            if (model.MusicMetadataList != null)
                model.MusicMetadataList!!.clear()

            model.MusicMetadataList = MusicDBController().selectAll()

            val table = TableView(model.MusicMetadataList)

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

            val musicSelectionModel = table.selectionModel
            musicSelectionModel.selectedItemProperty().addListener { changed, oldValue, newValue ->
                model.player.addNewMusic(Music("file:/" + newValue.Path.replace(" ", "%20").replace('\\', '/')))
            }

            return table
        }
    }
}