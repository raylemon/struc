package com.github.raylemon.struct.kleio.kontroller


import com.github.raylemon.struct.kleio.beans.CRC
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.StringConverter

/**
 * Created by big04 on 13-12-15.
 */
class CrcKontroller : TableKontroller<CRC>() {

    @FXML private lateinit var crc_colAssessNumber: TableColumn<CRC, Number>
    @FXML private lateinit var crc_colConversion: TableColumn<CRC, Boolean>
    @FXML private lateinit var crc_colMessage: TableColumn<CRC, String>
    @FXML private lateinit var crc_colSourceBase: TableColumn<CRC, Number>
    @FXML private lateinit var crc_colDivisor: TableColumn<CRC, String>
    @FXML private lateinit var crc_colDestBase: TableColumn<CRC, Number>
    @FXML private lateinit var crc_colSolution: TableColumn<CRC, String>

    override fun makeItem(): CRC = CRC(0)

    override fun generateRandomItem(assessNumber: Int): CRC = CRC(assessNumber)

    override fun initialize() {
        crc_colAssessNumber.setCellValueFactory { it.value.mAssessNumber }
        crc_colConversion.setCellValueFactory { it.value.mEncoded }
        crc_colMessage.setCellValueFactory { it.value.mMessage }
        crc_colSourceBase.setCellValueFactory { it.value.mBaseSource }
        crc_colDivisor.setCellValueFactory { it.value.mDivisor }
        crc_colDestBase.setCellValueFactory { it.value.mBaseDest }
        crc_colSolution.setCellValueFactory { it.value.mSolution }

        crc_colConversion.cellFactory = ComboBoxTableCell.forTableColumn(object : StringConverter<Boolean>() {
            override fun fromString(string: String?): Boolean = string.equals("encodage", true)

            override fun toString(obj: Boolean?): String = if (obj!!) "encodage" else "décodage"

        }, true, false)

        crc_colSourceBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 16)
        crc_colDestBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 16)
        crc_colMessage.cellFactory = TextFieldTableCell.forTableColumn()
        crc_colDivisor.cellFactory = TextFieldTableCell.forTableColumn()
    }


}