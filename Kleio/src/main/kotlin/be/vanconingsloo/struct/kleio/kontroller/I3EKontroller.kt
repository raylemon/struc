package be.vanconingsloo.struct.kleio.kontroller

import be.vanconingsloo.struct.kleio.beans.I3E
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter

/**
 * Created by big04 on 22-11-15.
 */
class I3EKontroller() : TableKontroller<I3E>() {

    @FXML private lateinit var i3e_colAssessNumber: TableColumn<I3E, Number>
    @FXML private lateinit var i3e_colFloatNumber: TableColumn<I3E, String>
    @FXML private lateinit var i3e_colSrcBase: TableColumn<I3E, Number>
    @FXML private lateinit var i3e_colIsDecimal: TableColumn<I3E, Boolean>
    @FXML private lateinit var i3e_colDecimal: TableColumn<I3E, Number>
    @FXML private lateinit var i3e_colDestBase: TableColumn<I3E, Number>
    @FXML private lateinit var i3e_colSolution: TableColumn<I3E, String>


    override fun makeItem(): I3E = I3E(0, true)

    override fun generateRandomItem(assessNumber: Int): I3E = I3E(assessNumber)

    @FXML override fun initialize() {
        i3e_colAssessNumber.setCellValueFactory { it.value.mAssessNumber }
        i3e_colFloatNumber.setCellValueFactory { it.value.mFloatNumber }
        i3e_colSrcBase.setCellValueFactory { it.value.mBaseDest }
        i3e_colIsDecimal.setCellValueFactory { it.value.mIsDecimal }
        i3e_colDecimal.setCellValueFactory { it.value.mDecimal }
        i3e_colDestBase.setCellValueFactory { it.value.mBaseDest }
        i3e_colSolution.setCellValueFactory { it.value.mSolution }

        i3e_colFloatNumber.cellFactory = TextFieldTableCell.forTableColumn()
        i3e_colSrcBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 16)
        i3e_colIsDecimal.cellFactory = ComboBoxTableCell.forTableColumn(object : StringConverter<Boolean>() {
            override fun toString(obj: Boolean?): String = if (obj!!) "dec => float" else "float => dec"

            override fun fromString(string: String?): Boolean = string.equals("dec => float", true)

        }, true, false)
        i3e_colDestBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 10, 16)
        i3e_colDecimal.cellFactory = TextFieldTableCell.forTableColumn(object : StringConverter<Number>() {
            override fun toString(obj: Number?): String? {
                if (obj?.toString() == "NaN") return "NaN"
                else return NumberStringConverter().toString(obj)
            }

            override fun fromString(string: String?): Number? {
                when (string) {
                    "+inf" -> return Float.POSITIVE_INFINITY
                    "-inf" -> return Float.NEGATIVE_INFINITY
                    "NaN" -> return Float.NaN
                    else -> return NumberStringConverter().fromString(string)
                }
            }
        })
    }
}

