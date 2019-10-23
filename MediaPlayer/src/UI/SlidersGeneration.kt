package UI

import Controller
import Model
import View
import javafx.geometry.Orientation
import javafx.scene.control.Slider

class SlidersGeneration {
    companion object{
        fun musicSlider(model: Model) : Slider{
            return object : Slider(){
                init {
                    onMouseClicked = model.controller.musicSliderClickEvent
                    minWidth = model.windowWidth
                }
            }
        }

        fun volumeSlider(model: Model) : Slider{
            return object : Slider(){
                init {
                    min = 0.0
                    max = 1.0

                    minHeight = 40.0

                    orientation = Orientation.VERTICAL

                    isShowTickMarks = true
                    isShowTickLabels = true
                    majorTickUnit = 0.25

                    onMouseDragged = model.controller.volumeSliderDraggedEvent
                }
            }
        }
    }
}