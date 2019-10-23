package UI

import Controller
import Model
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import View
import javafx.scene.control.MenuBar
import javafx.stage.Stage

class MenuBarGeneration {
    companion object{
        fun init(model: Model) : MenuBar {
            return object : javafx.scene.control.MenuBar() {
                init {
                    val menu = object : Menu("File") {
                        init {
                            val addMediaButton = object : MenuItem("Add media") {
                                init {
                                    onAction = model.controller.addNewMediaEvent
                                }
                            }
                            val addMediaFromFolderButton = object  : MenuItem("Add from folder"){
                                init{
                                    onAction = model.controller.addNewFolderEvent
                                }
                            }
                            items.addAll(addMediaButton, addMediaFromFolderButton)
                        }
                    }
                    menus.add(menu)
                }
            }
        }
    }
}