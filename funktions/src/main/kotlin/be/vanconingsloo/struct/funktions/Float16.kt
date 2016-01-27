package be.vanconingsloo.struct.funktions

import java.text.MessageFormat
import java.util.*


/**
 * Created by big04 on 13-08-15.
 * Float16 object.
 */
public object Float16 {
    public val INFINITY: Int = 0x7C00
    // ignores the higher 16 bits
    public fun toFloat(hbits: Int): Float {
        var mant = hbits and 1023            // 10 bits mantissa
        var exp = hbits and 31744            // 5 bits exponent
        if (exp == 31744)
        // NaN/Inf
            exp = 261120                    // -> NaN/Inf
        else if (exp != 0)
        // normalized value
        {
            exp += 114688                   // exp - 15 + 127
            if (mant == 0 && exp > 115712)
            // smooth transition
                return java.lang.Float.intBitsToFloat((hbits and 32768) shl 16 or exp shl 13 or 1023)
        } else if (mant != 0)
        // && exp==0 -> subnormal
        {
            exp = 115712                    // make it normal
            do {
                mant = mant shl 1                   // mantissa * 2
                exp -= 1024                 // decrease exp by 1
            } while ((mant and 1024) == 0) // while not normal
            mant = mant and 1023                    // discard subnormal bit
        }                                     // else +/-0 -> +/-0
        return java.lang.Float.intBitsToFloat(// combine all parts
                (hbits and 32768) shl 16          // sign  << ( 31 - 15 )
                        or ((exp or mant) shl 13))         // value << ( 23 - 10 )
    }

    // returns all higher 16 bits as 0 for all results
    public fun fromFloat(fval: Float): Int {
        val fbits = java.lang.Float.floatToIntBits(fval)
        val sign = fbits.ushr(16) and 32768          // sign only
        var v = (fbits and 2147483647) + 4096 // rounded value

        if (v >= 1199570944)
        // might be or become NaN/Inf
        {
            // avoid Inf due to rounding
            if ((fbits and 2147483647) >= 1199570944) {
                // is or must become NaN/Inf
                if (v < 2139095040)
                // was value but too large
                    return sign or 31744     // make it +/-Inf
                return sign or 31744 or // remains +/-Inf or NaN
                        (fbits and 8388607).ushr(13) // keep NaN (and Inf) bits
            }
            return sign or 31743             // unrounded not quite Inf
        }
        if (v >= 947912704)
        // remains normalized value
            return sign or (v - 939524096).ushr(13) // exp - 127 + 15
        if (v < 855638016)
        // too small for subnormal
            return sign                      // becomes +/-0
        v = (fbits and 2147483647).ushr(23)  // tmp exp for subnormal calc
        return sign or (((fbits and 8388607 or 8388608) // add subnormal bit
                + (8388608.ushr(v - 102))     // round depending on cut off
                ).ushr(126 - v))   // div by 2^(1-(exp-127+15)) and >> 13 | exp=0
    }

    public fun toString(number: Float, base: Int): String = Integer.toString(fromFloat(number), base)

    public infix fun toPolynom(num: Float): String {
        if (num.isInfinite() or num.isNaN()) return num.toString()
        if (fromFloat(num) in 0x001..0x03FF) return "Dénormalisé"
        var n = Math.abs(num).toFloat()
        if (n == 0f) return "0"
        var max = log2(n).toInt()
        if (Math.pow(2.0, max.toDouble()) > n) max--
        var curr: Int
        val join = StringJoiner("+")
        do {
            curr = log2(n).toInt()
            if (Math.pow(2.0, curr.toDouble()) > n) curr--
            if (curr != 0) join.add("2^$curr")
            n -= Math.pow(2.0, curr.toDouble()).toFloat()
        } while ( n > 0 && (max - curr) < 10)

        return MessageFormat.format("{0} ({1})", if (num > 0) "+" else "-", join.toString())
    }

    private fun log2(n: Float): Float = (Math.log(n.toDouble()) / Math.log(2.0)).toFloat()
}
