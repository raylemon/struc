package com.github.raylemon.struct.kleio.beans

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.time.LocalDate

/**
 * Created by big04 on 18-12-15.
 */

class Instr() {

    val mTitle = SimpleStringProperty("")
    val mInstructions = SimpleStringProperty("")
    val mDateDoc = SimpleObjectProperty<LocalDate>(LocalDate.now())
    val mDateDep = SimpleObjectProperty<LocalDate>()
    val mChoice = SimpleStringProperty("Interro")
    val mPoints = SimpleIntegerProperty(20)

    public fun checkIsValid(): Boolean {
        if (mTitle.value.isNullOrEmpty()) {
            alert("Titre manquant")
            return false
        }
        if (mChoice.value.isNullOrEmpty()) {
            alert("Choix manquant")
            return false
        }
        if (mPoints.value == null) {
            alert("Pas de points")
            return false
        }
        if (mDateDoc.value == null) {
            alert("Aucune date")
            return false
        }
        if (mInstructions.value.isNullOrEmpty()) {
            alert("Consignes manquantes")
            return false
        }
        return true
    }

    private fun alert(message: String) = Alert(Alert.AlertType.WARNING, message, ButtonType.OK).show()


}
