package com.github.raylemon.struct.astraea.beans

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleStringProperty

/**
 * Created by big04 on 16-09-15.
 */
abstract class Exercise {
    abstract val mSol: String
    abstract val mExtra: String?
    abstract override fun toString(): String
}

class ExerciseWrapper(val bean: Exercise) {
    val question = ReadOnlyStringWrapper(bean.toString())
    val attempt = SimpleStringProperty()
    val solution = ReadOnlyStringWrapper(bean.mSol)
    val extra = ReadOnlyStringWrapper(bean.mExtra)
}