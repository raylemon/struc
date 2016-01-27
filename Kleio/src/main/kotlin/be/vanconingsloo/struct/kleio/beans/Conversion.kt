package be.vanconingsloo.struct.kleio.beans

import be.vanconingsloo.struct.funktions.anyValue
import be.vanconingsloo.struct.funktions.clearString
import be.vanconingsloo.struct.funktions.nextInt
import be.vanconingsloo.struct.funktions.toInt
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by big04 on 18-11-15.
 */

@XmlRootElement
class Conversion(assessNumber: Int,
                 bSrc: Int = nextInt(19) + 2,
                 bDst: Int = nextInt(19) + 2,
                 private var number: Int = nextInt(Byte.MAX_VALUE.toInt())) : Exercise(assessNumber) {

    val mBaseSource = SimpleIntegerProperty(if (bSrc == bDst) bSrc + 2 else bSrc)

    val mBaseDest = SimpleIntegerProperty(if (bSrc == bDst) bDst + 2 else bDst)

    val mNumberSource = SimpleStringProperty(number.anyValue(bSrc))

    override val mSolution = SimpleStringProperty().apply {
        if (number < Byte.MIN_VALUE || number > Byte.MAX_VALUE) value = "Overflow"
        else value = number.anyValue(mBaseDest.value)
    }

    override fun toString(): String = "${mNumberSource.value} = ? (${mBaseDest.value})"

    init {
        mNumberSource.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mNumberSource.value = old
            } else {
                try {
                    number = new.clearString().toInt(mBaseSource.value)
                    if (number < Byte.MIN_VALUE || number > Byte.MAX_VALUE) mSolution.value = "Overflow"
                } catch (nfe: NumberFormatException) {
                    mSolution.value = "Impossible"
                }
            }
        }

        mBaseSource.addListener { observableValue, old, new -> mNumberSource.value = number.anyValue(new.toInt()) }

        mBaseDest.addListener { observableValue, old, new -> mSolution.value = number.anyValue(new.toInt()) }
    }

    constructor() : this(0)
}