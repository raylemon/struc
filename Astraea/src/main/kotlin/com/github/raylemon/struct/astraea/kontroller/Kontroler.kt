package com.github.raylemon.struct.astraea.kontroller

import com.github.raylemon.struct.astraea.beans.*
import com.github.raylemon.struct.funktions.clearString
import javafx.application.Platform
import javafx.beans.binding.StringBinding
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTableCell

/**
 * Created by big04 on 16-09-15.
 */
class Kontroler() {
    @FXML lateinit var table: TableView<ExerciseWrapper>
    @FXML lateinit var colResponse: TableColumn<ExerciseWrapper, String>
    @FXML lateinit var colExercise: TableColumn<ExerciseWrapper, String>
    @FXML lateinit var colSolution: TableColumn<ExerciseWrapper, String>
    @FXML lateinit var colCorrect: TableColumn<ExerciseWrapper, String>
    @FXML lateinit var colExtra: TableColumn<ExerciseWrapper, String>
    @FXML lateinit var lblEnonce: Label
    private var type = Type.NOTHING

    @FXML fun doClose() = Platform.exit()
    @FXML fun doGenerate() = generate()

    @FXML fun doSetConvert() = setup(Type.CONVERSION, msg = """Convertissez. Les nombres binaires commencent par "b" et sont écrits sur 8 bits.
                    Les nombres octaux commencent par "0" et les nombres hexadécimaux commencent par 0x""")

    @FXML fun doSetArith() = setup(Type.ARITH, msg = """Calculez. Les nombres binaires sont signés sur 8 bits. La convention d'écriture s'applique (b, 0, 0x)""")

    @FXML fun doSetd2f() = setup(Type.D2F, msg = """Convertissez ces nombres décimaux en flottant (16 bits).
                    La structure du nombre est S(1) E(5) M(10), b=15
                    La convention d'écriture s'applique (0 ou 0x).""")

    @FXML fun doSetf2d() = setup(Type.F2D, msg = """Convertissez ces nombres flottants (16 bits) en décimal.
                    La structure du nombre est S(1) E(5) M(10), b=15
                    La réponse est notée en tant que polynôme ±(2^x + …)""", extraMsg = "Valeur")

    @FXML fun doSetCrc() = setup(Type.CRC, msg = """Calculez le CRC et indiquez uniquement le CRC
				La convention d'écriture s'applique (0 ou 0x).""")

    @FXML fun doSetHamming() = setup(Type.HAMMING, msg = """Encodez ou décodez le message via Hamming
				La convention d'écriture s'applique (0 ou 0x).""", extraMsg = "Position de l'erreur")

    @FXML fun doShowSolutions() {
        if (colResponse.isEditable) {
            val b = Alert(Alert.AlertType.WARNING, "Vous ne pourrez plus ajouter de réponses !\n.Voulez-vous continuer ?", ButtonType.YES, ButtonType.NO).showAndWait()
            if (b.isPresent && b.get() == ButtonType.YES) {
                colResponse.isEditable = false
                colSolution.isVisible = true
                colExtra.isVisible = true
            } else {
                colSolution.isVisible = !colSolution.isVisible
                colExtra.isVisible = !colExtra.isVisible
            }
        }
    }

    @FXML fun initialize() {
        table.isEditable = true
        colCorrect.setCellValueFactory {
            object : StringBinding() {
                override fun computeValue(): String? {
                    when {
                        it.value.attempt.value == null -> return ""
                        it.value.attempt.value.clearString() == it.value.solution.value.clearString() -> return "Correct !"
                        else -> return "Vérifiez votre réponse"
                    }
                }

                init {
                    super.bind(it.value.attempt)
                }
            }
        }
        colSolution.setCellValueFactory { it.value.solution }
        colSolution.isVisible = false

        colResponse.setCellValueFactory { it.value.attempt }
        colResponse.cellFactory = TextFieldTableCell.forTableColumn()

        colExercise.setCellValueFactory { it.value.question }

        colExtra.setCellValueFactory { it.value.extra }
    }


    private fun generate() {
        var col = FXCollections.observableArrayList<ExerciseWrapper>()
        if (type == Type.NOTHING) Alert(Alert.AlertType.WARNING, "Choisissez une catégorie", ButtonType.OK).showAndWait()
        else for (i in 1..10) {
            when (type) {
                Type.CONVERSION -> col.add(ExerciseWrapper(Conversion()))
                Type.ARITH -> col.add(ExerciseWrapper(Arith()))
                Type.D2F -> col.add(ExerciseWrapper(Dec2I3E()))
                Type.F2D -> col.add(ExerciseWrapper(I3E2Dec()))
                Type.CRC -> col.add(ExerciseWrapper(CrcMessage()))
                Type.HAMMING -> col.add(ExerciseWrapper(HammingMessage()))
                else -> return
            }
        }
        table.items.addAll(col)
        colResponse.isEditable = true
        colSolution.isVisible = false
        colExtra.isVisible = false
    }

    private fun setup(type: Type, msg: String, extraMsg: String = "") {
        this.type = type
        lblEnonce.text = msg
        table.items.clear()
        colSolution.isVisible = false
        colExtra.isVisible = false
        colExtra.text = extraMsg
        generate()
    }
}

