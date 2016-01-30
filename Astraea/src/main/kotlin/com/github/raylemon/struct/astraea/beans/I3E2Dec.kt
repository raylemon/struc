package com.github.raylemon.struct.astraea.beans

import com.github.raylemon.struct.funktions.*
import java.math.BigInteger

/**
 * Created by big04 on 17-09-15.
 */
class I3E2Dec : Exercise() {
    private val bSource = octOrHex()
    private var num = getAnyFloat16()
    private var source = formatValue()
        get() {
            if ( num == 0f) {
                field = source.clearString().replace(" ", "")
                val big = BigInteger(source, bSource)
                num = java.lang.Float.intBitsToFloat(big.toInt())
            }
            return formatValue()
        }

    private fun formatValue(): String {
        var v = Float16.toString(num, bSource)
        return v.formatMessage(bSource, bSource)
    }

    override val mSol = Float16 toPolynom num
    override val mExtra: String?
        get() = if (Float16.fromFloat(num) in 0x001..0x03FF) "Dénormalise" else num.toString()

    override fun toString() = "$source => (base10)"
}
