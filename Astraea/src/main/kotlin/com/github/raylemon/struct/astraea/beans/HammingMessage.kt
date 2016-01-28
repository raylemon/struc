package com.github.raylemon.struct.astraea.beans

import com.github.raylemon.struct.funktions.*

/**
 * Created by big04 on 17-09-15.
 */

public class HammingMessage() : Exercise() {
    private var message = ""
        get() {
            if (binMsg.isEmpty()) {
                val num = field.clearString().toInt(bSource)
                binMsg = num.toBinaryString()
            }
            val s = binMsg.formatMessage(newBase = bSource)
            if (field.isEmpty()) field = s
            return s
        }
    private var bSource = octOrHex()
    private var bDest = octOrHex()
    private var encoded = nextBoolean()

    private var binMsg = generateMessage()

    override val mSol: String
        get() = if (encoded) encodeMessage().formatMessage(newBase = bDest) else decodeMessage().formatMessage(newBase = bDest)

    override val mExtra = getErrorPos().toString()

    override fun toString() = "Message à ${if (encoded) "encoder" else "décoder"} = $message = ? ($bDest)"

    private fun decodeMessage(): String {
        val power = getParityCount()
        val error = getErrorPos()
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

    private fun encodeMessage(): String {
        val msg = binMsg.map { Integer.valueOf(it.toString()) }.toIntArray()
        val bits = generateCode(msg)
        return bits.joinToString(separator = "").reversed()
    }

    private fun generateCode(msg: IntArray): IntArray {
        var i = 0.0
        var parity = 0.0
        while (i < msg.size) if (Math.pow(2.0, parity) == i + parity + 1) parity++ else i++ //Recherche du nombre de bits à ajouter
        val bits = IntArray(msg.size + parity.toInt()) // tableau de Hamming
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

    private fun getErrorPos(): Int {
        val parity = getParityCount()
        val msg = binMsg.reversed().map { Integer.valueOf(it.toString()) }.toIntArray() // conversion du message en tableau
        var par = IntArray(parity) // stockage des valeurs des bits de parité
        var syndrome = ""
        var power = 0
        while (power < parity) {
            for (i in msg.indices) {
                //extraction des bits de parité
                var k = i + 1
                val s = k.toBinaryString()
                val bit = ((s.toInt() ) / Math.pow(10.0, power.toDouble()).toInt()) % 10
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
}