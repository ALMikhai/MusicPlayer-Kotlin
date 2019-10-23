import javafx.application.Application
import javafx.stage.Stage

open class MPlayer : Application() {
    private var view = View()

    @Throws(Exception::class)
    override fun start(primaryStage: Stage){
        view.start()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MPlayer::class.java)
        }
    }
}