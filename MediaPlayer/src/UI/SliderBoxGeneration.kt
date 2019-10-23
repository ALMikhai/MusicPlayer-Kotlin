package UI

import Model
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.FocusModel
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class SliderBoxGeneration {
    companion object{
        fun init(model: Model) : VBox{
            return object : VBox() {
                init {
                    val volumeAndName = object :  HBox() {
                        init {
                            alignment = Pos.BOTTOM_LEFT
                            children.add(model.volumeSlider)
                            children.add(model.musicTimer)
                        }
                    }

                    children.add(volumeAndName)
                    children.add(model.musicSlider)
                }
            }
        }
    }
}