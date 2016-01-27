package be.vanconingsloo.struct.astraea

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

/**
 * Created by big04 on 16-09-15.
 */
public class MainApp : Application() {
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(javaClass.getResource("/main.fxml"))
        val root = loader.load<Parent>()
        primaryStage?.run {
            title = "Astraea"
            scene = Scene(root)
            show()
            icons.add(Image(MainApp::class.java.getResourceAsStream("/icon.png")))
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(MainApp::class.java, *args)
}