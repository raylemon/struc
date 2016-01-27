package be.vanconingsloo.struct.astraea.beans

import be.vanconingsloo.struct.funktions.anyValue
import be.vanconingsloo.struct.funktions.clearString
import be.vanconingsloo.struct.funktions.nextInt

class Arith : Exercise() {
    private var bDest = getBase()
    private var bSource1 = getBase()
    private var bSource2 = getBase()
    private var n1 = nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE
    private var n2 = nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE
    private var operator = when (nextInt(4)) {
        1 -> '-'
        2 -> '*'
        3 -> '/'
        else -> '+'
    }
    private var number1 = ""
        get() = n1.anyValue(bSource1)

    private var number2 = ""
        get() = n2.anyValue(bSource2)

    private fun getBase(): Int {
        val base = nextInt(4) + 1
        if (base == 2) return 10 else return Math.pow(2.0, base.toDouble()).toInt()
    }

    override val mSol: String
        get() {
            try {
                number1 = number1.clearString()
                number2 = number2.clearString()
            } catch (e: NumberFormatException) {
                return "Impossible"
            }
            if (operator == '/') {
                n1 = Math.abs(n1)
                n2 = Math.abs(n2)
                if (n1 < n2) {
                    val n = n2
                    n2 = n1
                    n1 = n
                }
                number1 = n1.anyValue(bSource1)
                number2 = n2.anyValue(bSource2)
            }
            val res = compute()
            if (res < Byte.MIN_VALUE || res > Byte.MAX_VALUE) return "OVF"
            else return res.anyValue(bDest)
        }

    private fun compute(): Int = when (operator) {
        '+' -> n1 + n2
        '-' -> n1 - n2
        '*' -> n1 * n2
        '/' -> n1 / n2
        else -> 0
    }

    override val mExtra = null

    override fun toString() = "$number1 $operator $number2"

}