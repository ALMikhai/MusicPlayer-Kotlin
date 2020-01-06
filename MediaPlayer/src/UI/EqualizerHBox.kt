package src.UI

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.MediaPlayer
import org.apache.poi.hslf.record.Slide
import src.Player
import javax.swing.plaf.SliderUI

class EqualizerHBox {
    companion object {
        lateinit var hBox : HBox

        fun getBox() : HBox {
            if (!::hBox.isInitialized) {
                hBox = init()
            }
            return hBox
        }

        fun update(player: Player) {
            hBox.children.forEachIndexed { index, node ->
                var vBox = (node as VBox)
                var slider = (vBox.children[0] as Slider)
                var label = (vBox.children[1] as Label)
                slider.value = player.player.audioEqualizer.bands[index].gain
                slider.max = player.player.audioEqualizer.bands[index].centerFrequency
                slider.valueProperty().addListener { _ ->
                    player.player.audioEqualizer.bands[index].gain = slider.value
                    label.text = (slider.value.toInt().toString() + " Hz")
                }
            }
        }

        private fun init() : HBox {
            val hBox = HBox()
            repeat(10) {
                val vBox = VBox()
                val label = Label()
                val slider = Slider()
                slider.orientation = Orientation.VERTICAL
                slider.maxHeight = 200.0
                slider.padding = Insets(0.0, 10.0, 0.0, 10.0)
                slider.min = 0.0
                slider.isShowTickLabels = true
                vBox.children.addAll(slider, label)
                hBox.children.add(vBox)
            }
            return hBox
        }
    }
}