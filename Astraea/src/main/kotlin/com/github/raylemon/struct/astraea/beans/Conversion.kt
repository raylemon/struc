package com.github.raylemon.struct.astraea.beans

import com.github.raylemon.struct.funktions.anyValue
import com.github.raylemon.struct.funktions.nextInt
import com.github.raylemon.struct.funktions.toInt

/**
 * Created by big04 on 16-09-15.
 */
class Conversion : Exercise() {
    private var err = ""

    private var mSource = nextInt(19) + 2
        set(value) = if (value == mDest) field = value + 2 else field = value

    private var mDest = nextInt(19) + 2
        set(value) = if (value == mSource) field = value + 2 else field = value

    private var n = nextInt(Byte.MAX_VALUE.toInt())

    private var source: String
        get() = n.anyValue(mSource)
        set(value) {
            try {
                n = value.toInt(mSource)
                if (n < Byte.MIN_VALUE || n > Byte.MAX_VALUE) err = "Overflow"
            } catch (e: NumberFormatException) {
                err = "Impossible"
            }
        }
    override val mSol: String
        get() = if (err == "") n.anyValue(mDest) else err
    override val mExtra: String?
        get() = null

    override fun toString() = "$source = ? ($mDest)"


}