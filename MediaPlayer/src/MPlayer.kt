import Pages.Main.MainStage
import javafx.application.Application
import javafx.stage.Stage

open class MPlayer : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage){
        var stage = Stage()
        stage.scene = MainStage().scene
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MPlayer::class.java)
        }
    }
}