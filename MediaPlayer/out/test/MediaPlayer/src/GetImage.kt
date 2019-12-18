import javafx.scene.image.Image
import javafx.scene.image.ImageView

class GetImage {
    companion object {
        fun getImage(path: String): ImageView {
            val input = javaClass.getResourceAsStream(path)
            val image = Image(input)

            return ImageView(image)
        }
    }
}