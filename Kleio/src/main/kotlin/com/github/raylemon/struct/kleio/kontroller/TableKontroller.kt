package com.github.raylemon.struct.kleio.kontroller

import com.github.raylemon.struct.kleio.beans.Exercise
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TextField

/**
 * Created by big04 on 18-11-15.
 */
abstract class TableKontroller<T : Exercise> {
    @FXML internal lateinit var tfStatement: TextField
    @FXML internal lateinit var table: TableView<T>

    @FXML fun doRemoveLine() {
        val line = table.selectionModel.selectedItem
        table.items.remove(line)
    }

    @FXML fun doClearTable() {
        table.items.clear()
    }

    @FXML fun doAddLine() {
        table.items.add(makeItem())
    }

    abstract fun makeItem(): T

    abstract fun generateRandomItem(assessNumber: Int): T

    abstract fun initialize()
}