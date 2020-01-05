package src.UI

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.media.MediaPlayer
import src.Player
import javax.swing.plaf.SliderUI

class EqualizerHBox {
    companion object {
        fun init(player: Player) : HBox {
            val hBox = HBox()
            player.player.audioEqualizer.bands.forEachIndexed { index, band ->
                val slider = Slider()
                slider.orientation = Orientation.VERTICAL
                slider.maxHeight = 200.0
                slider.padding = Insets(0.0, 10.0, 0.0, 10.0)
                slider.min = 0.0
                slider.max = band.centerFrequency
                slider.value = band.gain
                slider.valueProperty().addListener { _ ->
                    band.gain = slider.value
                }
                hBox.children.add(slider)
            }
            return hBox
        }
    }
}