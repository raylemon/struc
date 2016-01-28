package com.github.raylemon.struct.kleio.beans

import com.github.raylemon.struct.funktions.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.math.BigInteger
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class I3E(assessNumber: Int,
          isDecimal: Boolean = nextBoolean()) : Exercise(assessNumber) {

    private var number = getAnyFloat16()

    val mIsDecimal = SimpleBooleanProperty(isDecimal)

    val mDecimal = SimpleFloatProperty(number)

    val mBaseDest = SimpleIntegerProperty(if (mIsDecimal.value) 10 else octOrHex())

    val mBaseSrc = SimpleIntegerProperty(octOrHex())

    val mFloatNumber = SimpleStringProperty(Float16.toString(number, mBaseSrc.value).formatMessage(mBaseSrc.value, mBaseSrc.value))

    override val mSolution = SimpleStringProperty(compute())


    private fun compute() = if (mIsDecimal.value) Float16.toPolynom(number)
    else if (Float16.fromFloat(number) in 0x001..0x03FF) "Dénormalisé"
    else Float16.toString(number, mBaseDest.value).formatMessage(mBaseDest.value, mBaseDest.value)


    override fun toString() = if ( mIsDecimal.value) "${mDecimal.value} => ( ${mBaseDest.value} )" else "${mFloatNumber.value} => (10)"

    init {

        mDecimal.addListener { observableValue, old, new ->
            number = new.toFloat()
            mFloatNumber.value = Float16.toString(number, mBaseSrc.value).formatMessage(mBaseSrc.value, mBaseSrc.value)
            mSolution.value = compute()
        }

        mBaseDest.addListener { observableValue, old, new ->
            if (new.toInt() == 10) {
                mIsDecimal.value = true
            } else {
                mIsDecimal.value = false
                mBaseDest.value = new.toInt()
            }
            mSolution.value = compute()
        }

        mBaseSrc.addListener { observableValue, old, new ->
            mSolution.value = compute()
            mFloatNumber.value = Float16.toString(number, new.toInt()).formatMessage(new.toInt(), new.toInt())
        }

        mFloatNumber.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mFloatNumber.value = old
            } else {
                val s = new.clearString().replace(" ", "")
                val big = BigInteger(s, mBaseSrc.value)
                number = Float16.toFloat(big.toInt())
                mSolution.value = compute()
            }
        }

        mIsDecimal.addListener { observableValue, old, new ->
            mSolution.value = compute()
        }
    }

    constructor() : this(0)
}


