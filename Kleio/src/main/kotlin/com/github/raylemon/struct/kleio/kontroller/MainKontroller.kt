package com.github.raylemon.struct.kleio.kontroller

import com.github.raylemon.struct.kleio.backend.OdfWriter
import com.github.raylemon.struct.kleio.beans.*
import javafx.beans.binding.BooleanBinding
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.util.converter.NumberStringConverter
import java.io.File
import java.time.LocalDate

/**
 * Created by big04 on 17-11-15.
 */
class MainKontroller {

    /* FXML declarations*/

    @FXML private lateinit var tgbtConversions: ToggleButton
    @FXML private lateinit var tgbtArithmetics: ToggleButton
    @FXML private lateinit var tgbtFloat: ToggleButton
    @FXML private lateinit var tgbtCrc: ToggleButton
    @FXML private lateinit var tgbtHamming: ToggleButton
    @FXML private lateinit var tabConversions: Tab
    @FXML private lateinit var tabArithmetic: Tab
    @FXML private lateinit var tabFloat: Tab
    @FXML private lateinit var tabCrc: Tab
    @FXML private lateinit var tabHamming: Tab
    @FXML internal lateinit var slNumberAssessment: Slider
    @FXML internal lateinit var slQuestions: Slider
    @FXML private lateinit var tabInst_tfTitle: TextField
    @FXML private lateinit var tabInst_taInstructions: TextArea
    @FXML private lateinit var tabInst_dateDoc: DatePicker
    @FXML private lateinit var tabInst_cbChoice: ComboBox<String>
    @FXML private lateinit var tabInst_dateDeposit: DatePicker
    @FXML private lateinit var tabInst_tfPoints: TextField
    @FXML private lateinit var genBox: HBox

    /* Nested Kontrollers : name is {id}Controller*/

    @FXML private lateinit var convController: ConvKontroller
    @FXML private lateinit var arithController: ArithKontroller
    @FXML private lateinit var i3eController: I3EKontroller
    @FXML private lateinit var crcController: CrcKontroller
    @FXML private lateinit var hammingController: HammingKontroller

    /* Other declarations */
    var instructions = Instr()

    /* FXML functions */

    @FXML fun doPrint() {
        val dir = DirectoryChooser().apply {
            title = "Générer les documents"
        }.showDialog(null) ?: null
        dir?.let {
            if (instructions.checkIsValid()) {
                for (i in 1..slNumberAssessment.value.toInt())
                    for (j in 1 downTo 0)
                        OdfWriter(i, j == 0).apply {
                            buildHeader(instructions)
                            when {
                                !tabConversions.isDisabled -> writeParagraph<Conversion>(convController.tfStatement.text, convController.table.items.filter { it.mAssessNumber.value == i })
                                !tabConversions.isDisabled -> writeParagraph<Arith>(arithController.tfStatement.text, arithController.table.items.filter { it.mAssessNumber.value == i })
                                !tabConversions.isDisabled -> writeParagraph<I3E>(i3eController.tfStatement.text, i3eController.table.items.filter { it.mAssessNumber.value == i })
                                !tabConversions.isDisabled -> writeParagraph<CRC>(crcController.tfStatement.text, crcController.table.items.filter { it.mAssessNumber.value == i })
                                !tabConversions.isDisabled -> writeParagraph<Hamming>(hammingController.tfStatement.text, hammingController.table.items.filter { it.mAssessNumber.value == i })
                            }
                        }.save(File(dir, "${instructions.mTitle.value}${i.toString()}.odf"))
            }
        }
    }

    @FXML fun doOpen() {
        val file = FileChooser().apply {
            title = "Charger le fichier"
            extensionFilters += FileChooser.ExtensionFilter("Assessment", "*.ray")
            initialFileName = "${instructions.mTitle.value ?: LocalDate.now().toString()}.ray"
        }.showOpenDialog(null) ?: null
        file?.let {
        }
    }


    @FXML fun doSave() {
        val file = FileChooser().apply {
            title = "Sauver le fichier"
            extensionFilters += FileChooser.ExtensionFilter("Assessment", "*.ray")
            initialFileName = "${instructions.mTitle.value ?: LocalDate.now().toString()}.ray"
        }.showSaveDialog(null) ?: null
        file?.let {
        }
    }

    @FXML fun doGenerate() {
        for (i in 1..slNumberAssessment.value.toInt()) {
            for (j in 1..slQuestions.value.toInt()) when {
                tabConversions.isSelected -> convController.table.items.add(convController.generateRandomItem(i))
                tabArithmetic.isSelected -> arithController.table.items.add(arithController.generateRandomItem(i))
                tabFloat.isSelected -> i3eController.table.items.add(i3eController.generateRandomItem(i))
                tabCrc.isSelected -> crcController.table.items.add(crcController.generateRandomItem(i))
                tabHamming.isSelected -> hammingController.table.items.add(hammingController.generateRandomItem(i))
            }
        }
    }

    /* Other functions */


    /*Initialize*/
    @FXML fun initialize() {
        tabConversions.disableProperty().bind(tgbtConversions.selectedProperty().not())
        tabArithmetic.disableProperty().bind(tgbtArithmetics.selectedProperty().not())
        tabFloat.disableProperty().bind(tgbtFloat.selectedProperty().not())
        tabCrc.disableProperty().bind(tgbtCrc.selectedProperty().not())
        tabHamming.disableProperty().bind(tgbtHamming.selectedProperty().not())
        genBox.visibleProperty().bind(object : BooleanBinding() {
            init {
                bind(tgbtConversions.selectedProperty(),
                        tgbtArithmetics.selectedProperty(),
                        tgbtFloat.selectedProperty(),
                        tgbtCrc.selectedProperty(),
                        tgbtHamming.selectedProperty())
            }

            override fun computeValue(): Boolean {
                return (tgbtConversions.selectedProperty().value or
                        tgbtArithmetics.selectedProperty().value or
                        tgbtFloat.selectedProperty().value or
                        tgbtCrc.selectedProperty().value or
                        tgbtHamming.selectedProperty().value)
            }
        })

        tabInst_tfTitle.textProperty().bindBidirectional(instructions.mTitle)
        tabInst_dateDoc.valueProperty().bindBidirectional(instructions.mDateDoc)
        tabInst_dateDeposit.valueProperty().bindBidirectional(instructions.mDateDep)
        tabInst_tfPoints.textProperty().bindBidirectional(instructions.mPoints, NumberStringConverter())
        tabInst_cbChoice.valueProperty().bindBidirectional(instructions.mChoice)
        tabInst_taInstructions.textProperty().bindBidirectional(instructions.mInstructions)
    }
}