package be.vanconingsloo.struct.astraea.beans

import be.vanconingsloo.struct.funktions.*

/**
 * Created by big04 on 17-09-15.
 */
public class CrcMessage() : Exercise() {
    private val divisor = getDivisor()
    private var binMsg = generateMessage()
    private var encoded = nextBoolean()
    private var bSource = octOrHex()
    private var bDest = octOrHex()

    var message = ""
        get() {
            if (binMsg.isEmpty()) {
                val num = field.clearString().toInt(bSource)
                binMsg = num.toBinaryString()
            }
            val s = binMsg.formatMessage(newBase = bSource)
            if (field.isEmpty()) field = s
            return s
        }

    override val mSol: String
        get() = computeCrc(binMsg, divisor).substring(binMsg.length).formatMessage(newBase = bDest)

    private fun computeCrc(dividend: String, divisor: String): String {
        val divis = divisor.map { it.toString().toInt() /*Integer.parseInt(it.toString())*/ }.toIntArray()
        val length = dividend.length + divisor.length - 1
        val crc = IntArray(length)
        var rem = IntArray(length)
        /* Calcul CRC */
        val div = IntArray(length)
        for (i in dividend.indices) div[i] = dividend[i].toString().toInt()//Integer.valueOf(dividend[i].toString())
        System.arraycopy(div, 0, rem, 0, div.size)
        rem = divide(divis, rem)
        for (i in div.indices) crc[i] = div[i] xor rem[i]
        return StringBuilder().apply { for (aCrc in crc) this.append(aCrc) }.toString()
    }

    private fun divide(divisor: IntArray, rem: IntArray): IntArray {
        var cur = 0
        while (true) {
            for (i in divisor.indices) rem[cur + i] = (rem[cur + i] xor divisor[i])

            while (rem[cur] == 0 && cur != rem.size - 1) cur++

            if ((rem.size - cur) < divisor.size) break
        }
        return rem
    }

    override val mExtra: String? = null

    override fun toString() = "$message à ${if (encoded) "encoder" else "décoder"} [diviseur : $divisor]"
}