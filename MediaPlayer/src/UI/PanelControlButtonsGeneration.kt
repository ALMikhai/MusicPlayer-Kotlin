package UI

import Model
import javafx.scene.control.Button
import javafx.scene.layout.HBox

class PanelControlButtonsGeneration {

    companion object {
        fun init(model: Model): HBox {
            return object : HBox() {
                init {
                    val playButton = object : Button("", GetImage.getImage("resources/images/play.png")) {
                        init {
                            setOnAction {
                                // TODO По идее все сет актионы должны быть в контроллере(уточни).
                                model.setMusicNow()
                            }
                        }
                    }

                    val pauseButton = object : Button("", GetImage.getImage("resources/images/pause.png")) {
                        init {
                            setOnAction {
                                model.mPlayer?.pause()
                            }
                        }
                    }

                    val stopButton = object : Button("", GetImage.getImage("resources/images/stop.png")) {
                        init {
                            setOnAction {
                                model.mPlayer?.stop()
                            }
                        }
                    }

                    val nextButton = object : Button("", GetImage.getImage("resources/images/next.png")) {
                        init {
                            setOnAction {
                                model.setNextMusic()
                            }
                        }
                    }

                    val prevButton = object : Button("", GetImage.getImage("resources/images/prev.png")) {
                        init {
                            setOnAction {
                                model.setPrevMusic()
                            }
                        }
                    }

                    children.addAll(prevButton, playButton, pauseButton, nextButton, stopButton)
                }
            }
        }
    }
}