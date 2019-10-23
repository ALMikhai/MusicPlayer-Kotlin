package UI

import javafx.scene.control.TableColumn
import Music
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class MusicTableGeneration {

    companion object {
        fun init(list : ObservableList<Music>) : TableView<Music>{
            var table = TableView(list)

            var pathColumn: TableColumn<Music, String> = TableColumn("Name")
            pathColumn.minWidth = 450.0
            pathColumn.cellValueFactory = PropertyValueFactory<Music, String>("Name")
            table.columns.add(pathColumn)

            var durationColumn: TableColumn<Music, String> = TableColumn("Duration")
            durationColumn.cellValueFactory = PropertyValueFactory<Music, String>("Duration")
            table.columns.add(durationColumn)

            return table
        }
    }
}