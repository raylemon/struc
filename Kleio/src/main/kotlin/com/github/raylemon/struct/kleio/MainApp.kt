package com.github.raylemon.struct.kleio

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

/**
 * Created by big04 on 29-10-15.
 * Main class. Used to launch GUI
 */
public class MainApp : Application() {
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(MainApp::class.java.getResource("/main.fxml"))
        val root = loader.load<Parent>()
        primaryStage?.run {
            title = "Kleio"
            scene = Scene(root)
            show()
        }
        primaryStage?.icons?.add(Image(javaClass.getResourceAsStream("/imgs/kleio.png")))
    }
}

fun main(args: Array<String>) {
    Application.launch(MainApp::class.java, *args)
}