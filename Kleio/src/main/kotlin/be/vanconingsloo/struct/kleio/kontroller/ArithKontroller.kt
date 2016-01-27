package be.vanconingsloo.struct.kleio.kontroller

import be.vanconingsloo.struct.kleio.beans.Arith
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell

/**
 * Created by big04 on 20-11-15.
 */
class ArithKontroller() : TableKontroller<Arith>() {

    @FXML private lateinit var arith_colAssessNumber: TableColumn<Arith, Number>
    @FXML private lateinit var arith_colSrcBase1: TableColumn<Arith, Number>
    @FXML private lateinit var arith_colSrcNumber1: TableColumn<Arith, String>
    @FXML private lateinit var arith_colSrcBase2: TableColumn<Arith, Number>
    @FXML private lateinit var arith_colSrcNumber2: TableColumn<Arith, String>
    @FXML private lateinit var arith_colDestBase: TableColumn<Arith, Number>
    @FXML private lateinit var arith_colSolution: TableColumn<Arith, String>
    @FXML private lateinit var arith_colOperator: TableColumn<Arith, Char>

    override fun makeItem(): Arith = Arith(assessNumber = 0, num1 = 0, num2 = 0)

    override fun generateRandomItem(assessNumber: Int): Arith = Arith(assessNumber)

    @FXML override fun initialize() {
        table.isEditable = true
        arith_colAssessNumber.setCellValueFactory { it.value.mAssessNumber }
        arith_colSolution.setCellValueFactory { it.value.mSolution }
        arith_colOperator.setCellValueFactory { it.value.mOperator }
        arith_colSrcBase1.setCellValueFactory { it.value.mBaseSrc1 }
        arith_colSrcBase2.setCellValueFactory { it.value.mBaseSrc2 }
        arith_colSrcNumber1.setCellValueFactory { it.value.mNumberSrc1 }
        arith_colSrcNumber2.setCellValueFactory { it.value.mNumberSrc2 }
        arith_colDestBase.setCellValueFactory { it.value.mBaseDest }

        arith_colDestBase.cellFactory = ComboBoxTableCell.forTableColumn(2, 8, 10, 16)
        arith_colOperator.cellFactory = ComboBoxTableCell.forTableColumn('+', '-', '×', '÷')
        arith_colSrcBase1.cellFactory = ComboBoxTableCell.forTableColumn(2, 8, 10, 16)
        arith_colSrcBase2.cellFactory = ComboBoxTableCell.forTableColumn(2, 8, 10, 16)
        arith_colSrcNumber1.cellFactory = TextFieldTableCell.forTableColumn()
        arith_colSrcNumber2.cellFactory = TextFieldTableCell.forTableColumn()
    }
}

