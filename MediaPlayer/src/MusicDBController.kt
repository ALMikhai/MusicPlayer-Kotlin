package src

import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement


class MusicDBController {
    private var _connectionString = "jdbc:sqlite:DataBase/MusicDataBase.db"
    private lateinit var _connection : Connection

    init {
        connect()
        create()
    }

    fun connect() {
        _connection = DriverManager.getConnection(_connectionString)
    }

    fun create() {
        val sql = ("CREATE TABLE IF NOT EXISTS Music (\n" +
                "    Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    Title TEXT NOT NULL,\n" +
                "    Author TEXT NOT NULL,\n" +
                "    Album TEXT NOT NULL,\n" +
                "    Genre TEXT NOT NULL,\n" +
                "    Duration TEXT NOT NULL,\n" +
                "    Path TEXT NOT NULL\n" +
                ");")

        execute(sql)
    }

    fun insert(metadata : Mp3Metadata) {
        val sql = (
                "INSERT INTO Music (Title,Author,Album,Genre,Duration,Path)\n" +
                "VALUES( '${metadata.Title}', '${metadata.Author}', '${metadata.Album}', '${metadata.Genre}', '${metadata.Duration}', '${metadata.Path}');"
                )
        execute(sql)
    }

    fun selectAll() : ObservableList<Mp3MetadataFromBD> {
        val sql = (
                "SELECT * FROM Music;"
                )

        var metadataList : ObservableList<Mp3MetadataFromBD> = FXCollections.observableArrayList()

        try {
            val stmt: Statement = _connection.createStatement()
            var result = stmt.executeQuery(sql)
            val columns: Int = result.getMetaData().getColumnCount()
            while (result.next()) {
                metadataList.add(Mp3MetadataFromBD(
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7)))
//                for (i in 1..columns) {
//
//                    if (i == 7)
//                        print(result.getString(i).toString() + "\t")
//                }
//                println()
            }
        } catch (e: SQLException) {
            println(e.message)
        }

        return metadataList;
    }

    fun deleteAll() {
        val sql = (
                "DELETE FROM Music;"
                )

        execute(sql)
    }

    private fun execute(command: String) {
        try {
            val stmt: Statement = _connection.createStatement()
            stmt.execute(command)
        } catch (e: SQLException) {
            println(e.message)
        }
    }
}