package com.github.raylemon.struct.kleio.beans

import com.github.raylemon.struct.funktions.anyValue
import com.github.raylemon.struct.funktions.clearString
import com.github.raylemon.struct.funktions.nextInt
import com.github.raylemon.struct.funktions.toInt
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Arith(assessNumber: Int,
            private var num1: Int = nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE,
            private var num2: Int = nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE
) : Exercise(assessNumber) {

    val mBaseSrc1 = SimpleIntegerProperty(getBase())
    val mBaseSrc2 = SimpleIntegerProperty(getBase())
    val mBaseDest = SimpleIntegerProperty(getBase())
    val mOperator = SimpleObjectProperty<Char>(when (nextInt(4)) {
        1 -> '-'
        2 -> '×'
        3 -> '÷'
        else -> '+'
    })
    val mNumberSrc1 = SimpleStringProperty(num1.anyValue(mBaseSrc1.value))
    val mNumberSrc2 = SimpleStringProperty(num2.anyValue(mBaseSrc2.value))

    override val mSolution = SimpleStringProperty().apply {
        if (mOperator.value == '÷') {
            num1 = Math.abs(num1)
            num2 = Math.abs(num2)
            if (num1 < num2) {
                val n = num2
                num2 = num1
                num1 = n
            }
            mNumberSrc1.value = num1.anyValue(mBaseSrc1.value)
            mNumberSrc2.value = num2.anyValue(mBaseSrc2.value)
        }
        val res = compute()
        if (res < Byte.MIN_VALUE || res > Byte.MAX_VALUE) value = "Ovf"
        else value = res.anyValue(mBaseDest.value)
    }

    private fun compute(): Int {
        return when (mOperator.value) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '×' -> num1 * num2
            '÷' -> try {
                num1 / num2
            } catch (ae: ArithmeticException) {
                return Int.MAX_VALUE
            }
            else -> 0
        }
    }

    override fun toString() = "${mNumberSrc1.value} ${mOperator.value} ${mNumberSrc2.value}"

    private fun getBase(): Int {
        val base = nextInt(4) + 1
        if (base == 2) return 10 else return Math.pow(2.0, base.toDouble()).toInt()
    }

    init {
        mOperator.addListener { observableValue, old, new -> mSolution.value = compute().anyValue(mBaseDest.value) }

        mNumberSrc1.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F-]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mNumberSrc1.value = old
            } else {
                try {
                    num1 = new.clearString().toInt(mBaseSrc1.value)
                    mSolution.value = compute().anyValue(mBaseDest.value)
                } catch (nfe: NumberFormatException) {
                    mSolution.value = "Impossible"
                }
            }
        }

        mNumberSrc2.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F-]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mNumberSrc2.value = old
            } else {
                try {
                    num2 = new.clearString().toInt(mBaseSrc2.value)
                    mSolution.value = compute().anyValue(mBaseDest.value)
                } catch (nfe: NumberFormatException) {
                    mSolution.value = "Impossible"
                }
            }
        }

        mBaseSrc1.addListener { observableValue, old, new -> mNumberSrc1.value = num1.anyValue(new.toInt()) }
        mBaseSrc2.addListener { observableValue, old, new -> mNumberSrc2.value = num2.anyValue(new.toInt()) }
        mBaseDest.addListener { observableValue, old, new -> mSolution.value = compute().anyValue(new.toInt()) }
    }

    constructor() : this(0)
}
