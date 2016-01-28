package com.github.raylemon.struct.astraea.beans

import com.github.raylemon.struct.funktions.Float16
import com.github.raylemon.struct.funktions.formatMessage
import com.github.raylemon.struct.funktions.getAnyFloat16
import com.github.raylemon.struct.funktions.octOrHex

class Dec2I3E : Exercise() {
    private var source = getAnyFloat16()
    private var bDest = octOrHex()

    override val mSol: String
        get() {
            val v = Float16.toString(source, bDest)
            return v.formatMessage(bDest, bDest)
        }
    override val mExtra = null

    override fun toString() = "$source => (base $bDest)"

}