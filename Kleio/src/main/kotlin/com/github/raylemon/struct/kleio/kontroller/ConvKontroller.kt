package com.github.raylemon.struct.kleio.kontroller

import com.github.raylemon.struct.kleio.beans.Conversion
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell

/**
 * Created by big04 on 18-11-15.
 */
class ConvKontroller : TableKontroller<Conversion>() {

    @FXML private lateinit var conv_colAssessNumber: TableColumn<Conversion, Number>
    @FXML private lateinit var conv_colSrcBase: TableColumn<Conversion, Number>
    @FXML private lateinit var conv_colSrcNumber: TableColumn<Conversion, String>
    @FXML private lateinit var conv_colDestBase: TableColumn<Conversion, Number>
    @FXML private lateinit var conv_colSolution: TableColumn<Conversion, String>

    override fun generateRandomItem(assessNumber: Int) = Conversion(assessNumber)

    override fun makeItem() = Conversion(assessNumber = 0)

    @FXML override fun initialize() {
        table.isEditable = true
        val baseItems = FXCollections.observableArrayList<Number>().apply { addAll(2..20) }

        conv_colAssessNumber.setCellValueFactory { it.value.mAssessNumber }
        conv_colSolution.setCellValueFactory { it.value.mSolution }
        conv_colDestBase.setCellValueFactory { it.value.mBaseDest }
        conv_colSrcBase.setCellValueFactory { it.value.mBaseSource }
        conv_colSrcNumber.setCellValueFactory { it.value.mNumberSource }

        conv_colDestBase.cellFactory = ComboBoxTableCell.forTableColumn(baseItems)
        conv_colSrcBase.cellFactory = ComboBoxTableCell.forTableColumn(baseItems)
        conv_colSrcNumber.cellFactory = TextFieldTableCell.forTableColumn()
    }
}