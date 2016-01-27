package be.vanconingsloo.struct.kleio.kontroller

import be.vanconingsloo.struct.kleio.beans.Hamming
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.StringConverter

/**
 * Created by big04 on 16-12-15.
 */
class HammingKontroller : TableKontroller<Hamming>() {

    @FXML private lateinit var hamming_colAssessNumber: TableColumn<Hamming, Number>
    @FXML private lateinit var hamming_colConversion: TableColumn<Hamming, Boolean>
    @FXML private lateinit var hamming_colMessage: TableColumn<Hamming, String>
    @FXML private lateinit var hamming_colSourceBase: TableColumn<Hamming, Number>
    @FXML private lateinit var hamming_colDestBase: TableColumn<Hamming, Number>
    @FXML private lateinit var hamming_colSolution: TableColumn<Hamming, String>
    @FXML private lateinit var hamming_colErrorPos: TableColumn<Hamming, Number>


    override fun makeItem(): Hamming = Hamming(0)

    override fun generateRandomItem(assessNumber: Int): Hamming = Hamming(assessNumber)

    override fun initialize() {
        hamming_colAssessNumber.setCellValueFactory { it.value.mAssessNumber }
        hamming_colConversion.setCellValueFactory { it.value.mIsEncoded }
        hamming_colMessage.setCellValueFactory { it.value.mMessage }
        hamming_colSourceBase.setCellValueFactory { it.value.mBaseSource }
        hamming_colDestBase.setCellValueFactory { it.value.mBaseDest }
        hamming_colSolution.setCellValueFactory { it.value.mSolution }
        hamming_colErrorPos.setCellValueFactory { it.value.mErrorPos }

        hamming_colConversion.cellFactory = ComboBoxTableCell.forTableColumn(
                object : StringConverter<Boolean>() {
                    override fun toString(obj: Boolean?): String = if (obj!!) "encodage" else "décodage"

                    override fun fromString(string: String?): Boolean = string.equals("encodage", true)

                }, true, false
        )

        hamming_colSourceBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 16)
        hamming_colDestBase.cellFactory = ComboBoxTableCell.forTableColumn(8, 16)
        hamming_colMessage.cellFactory = TextFieldTableCell.forTableColumn()
    }
}