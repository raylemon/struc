package com.github.raylemon.struct.kleio.beans

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.StringProperty

/**
 * Created by big04 on 20-11-15.
 */
abstract class Exercise(assessNumber: Int) {
    val mAssessNumber = SimpleIntegerProperty(assessNumber)

    abstract val mSolution: StringProperty

    abstract override fun toString(): String

}