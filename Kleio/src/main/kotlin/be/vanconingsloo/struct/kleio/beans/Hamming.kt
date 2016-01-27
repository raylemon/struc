package be.vanconingsloo.struct.kleio.beans

import be.vanconingsloo.struct.funktions.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by big04 on 16-12-15.
 */
@XmlRootElement
class Hamming(assessNumber: Int) : Exercise(assessNumber) {

    private var binMsg = generateMessage()

    val mBaseSource = SimpleIntegerProperty(octOrHex())
    val mBaseDest = SimpleIntegerProperty(octOrHex())
    val mIsEncoded = SimpleBooleanProperty(nextBoolean())
    val mErrorPos = SimpleIntegerProperty(getPosError())
    val mMessage = SimpleStringProperty(binMsg.formatMessage(newBase = mBaseSource.value))

    override val mSolution = SimpleStringProperty(
            (if (mIsEncoded.value) encodeMessage() else decodeMessage()).formatMessage(newBase = mBaseDest.value)
    )

    override fun toString(): String = "Message à ${if (mIsEncoded.value) "encoder" else "décoder"} => ${mMessage.value} =? (${mBaseDest.value})"

    private fun encodeMessage(): String {
        val msg = binMsg.map { it.toString().toInt() }.toIntArray()
        val bits = generateCode(msg)
        return bits.joinToString(separator = "").reversed()
    }

    private fun decodeMessage(): String {
        val power = getParityCount()
        val error = getPosError()
        var msg = StringBuilder(binMsg.reversed())
        try {
            if (error != 0) {
                val c = msg[error - 1]
                val d = if (c == '1') '0' else '1'
                msg.setCharAt(error - 1, d)
            }
            for (i in power - 1..0) msg.deleteCharAt(Math.pow(2.0, i.toDouble()).toInt() - 1)
        } catch (e: StringIndexOutOfBoundsException) {
            return "Erreur - ${e.message}"
        }
        return msg.reverse().toString()
    }

    private fun generateCode(msg: IntArray): IntArray {
        var i = 0.0
        var parity = 0.0
        while (i < msg.size) if (Math.pow(2.0, parity) == i + parity + 1) parity++ else i++
        val bits = IntArray(msg.size + parity.toInt()) //tableau de Hamming
        var k = 0
        var j = 0.0
        i = 1.0
        while (i <= bits.size) {
            if (Math.pow(2.0, j) == i) {
                bits[i.toInt() - 1] = 2 //remplissage des inconnus par des 2
                j++
            } else bits[k + j.toInt()] = msg[k++]
            i++
        }
        i = 0.0
        while (i < parity) {
            bits[Math.pow(2.0, i).toInt() - 1] = getParity(bits, i.toInt())
            i++
        }
        return bits
    }

    private fun getParity(bits: IntArray, power: Int): Int {
        var parity = 0
        for (i in bits.indices) {
            if (bits[i] != 2) {
                //si la case ne contient pas 2, on récupère l'index et on le convertit en binaire
                val s = (i + 1).toBinaryString()
                val x = (s.toInt() / Math.pow(10.0, power.toDouble()).toInt()) % 10
                if (x == 1 && bits[i] == 1) parity = (parity + 1) % 2
            }
        }
        return parity
    }

    private fun getPosError(): Int {
        val parity = getParityCount()
        val msg = binMsg.reversed().map { it.toString().toInt() }.toIntArray() //conversion du message en tableau
        var par = IntArray(parity) //stockage des valeurs des bits de parité
        var syndrome = ""
        var power = 0
        while (power < parity) {
            for (i in msg.indices) {
                //extraction des bits de parité
                var k = i + 1
                val s = k.toBinaryString()
                val bit = ((s.toInt()) / Math.pow(10.0, power.toDouble()).toInt())
                if (bit == 1 && msg[i] == 1) par[power] = (par[power] + 1) % 2
            }
            syndrome = par[power].toString() + syndrome
            power++
        }
        return syndrome.toInt(2)
    }

    private fun getParityCount(): Int {
        var parity = 0.0
        while (Math.pow(2.0, parity) <= binMsg.length) parity++
        return parity.toInt()
    }

    init {
        mIsEncoded.addListener { observableValue, old, new ->
            mSolution.value = (if (new) encodeMessage() else decodeMessage()).formatMessage(newBase = mBaseDest.value)
            mErrorPos.value = getPosError()
        }

        mMessage.addListener { observableValue, old, new ->
            if (!new.clearString().matches("[0-9A-F]+".toRegex())) {
                Alert(Alert.AlertType.WARNING,
                        "Texte incorrect \n Inscrire uniquement un nombre !",
                        ButtonType.OK).show()
                mMessage.value = old
            } else {
                binMsg = new.clearString().toInt(mBaseSource.value).toBinaryString()
                mSolution.value = (if (mIsEncoded.value) encodeMessage() else decodeMessage()).formatMessage(newBase = mBaseDest.value)
                mErrorPos.value = getPosError()
                mMessage.value = new.formatMessage(mBaseSource.value, mBaseSource.value)
            }
        }

        mBaseSource.addListener { observableValue, old, new ->
            mMessage.value = mMessage.value.formatMessage(new.toInt(), old.toInt())
        }

        mBaseDest.addListener { observableValue, old, new ->
            mSolution.value = (if (mIsEncoded.value) encodeMessage() else decodeMessage()).formatMessage(newBase = new.toInt())
            mErrorPos.value = getPosError()
        }
    }

    constructor() : this(0)
}