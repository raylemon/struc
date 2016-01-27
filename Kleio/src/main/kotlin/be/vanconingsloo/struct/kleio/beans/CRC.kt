package be.vanconingsloo.struct.kleio.beans

import be.vanconingsloo.struct.funktions.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by big04 on 13-12-15.
 */
@XmlRootElement
class CRC(assessNumber: Int) : Exercise(assessNumber) {

    private var binMsg = generateMessage()
    val mDivisor = SimpleStringProperty(be.vanconingsloo.struct.funktions.getDivisor())
    val mEncoded = SimpleBooleanProperty(nextBoolean())
    val mBaseSource = SimpleIntegerProperty(octOrHex())
    val mBaseDest = SimpleIntegerProperty(octOrHex())
    val mMessage = SimpleStringProperty(binMsg.formatMessage(newBase = mBaseSource.value))

    override val mSolution = SimpleStringProperty(computeCrc(binMsg, mDivisor.value).formatMessage(newBase = mBaseDest.value))

    override fun toString() = "${mMessage.value} à ${if (mEncoded.value) "encoder" else "décoder"} [diviseur : ${mDivisor.value}"

    private fun computeCrc(dividend: String, divisor: String): String {
        val divis = mDivisor.value.map { it.toString().toInt() }.toIntArray()
        val length = dividend.length + divisor.length - 1
        val crc = IntArray(length)
        var rem = IntArray(length)
        /*Calcul CRC*/
        val div = IntArray(length)
        for (i in dividend.indices) div[i] = dividend[i].toString().toInt()
        System.arraycopy(div, 0, rem, 0, div.size)
        rem = divide(divis, rem)
        for (i in div.indices) crc[i] = div[i] xor rem[i]
        return StringBuilder().apply {
            for (aCrc in crc) this.append(aCrc)
        }.toString().substring(binMsg.length)
    }

    private fun divide(divis: IntArray, rem: IntArray): IntArray {
        var curr = 0
        while (true) {
            for (i in divis.indices) rem[curr + i] = rem[curr + i] xor divis[i]
            while (rem[curr] == 0 && curr != rem.size - 1) curr++
            if ((rem.size - curr) < divis.size) break
        }
        return rem
    }

    init {
        mDivisor.addListener { observableValue, old, new ->
            if (!new.matches("[01]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un format binaire !",
                        ButtonType.OK).show()
                mDivisor.value = old
            } else {
                mSolution.value = computeCrc(binMsg, new).formatMessage(newBase = mBaseDest.value)
            }
        }

        mMessage.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mMessage.value = old
            } else {
                binMsg = new.clearString().toInt(mBaseSource.value).toBinaryString()
                mSolution.value = computeCrc(binMsg, mDivisor.value)
                mMessage.value = new.formatMessage(mBaseSource.value, mBaseSource.value)
            }
        }

        mBaseSource.addListener { observableValue, old, new ->
            mMessage.value = mMessage.value.formatMessage(new.toInt(), old.toInt())
        }

        mBaseDest.addListener { observableValue, old, new ->
            mSolution.value = computeCrc(binMsg, mDivisor.value).formatMessage(newBase = new.toInt())
        }
    }

    constructor() : this(0)
}