package com.github.raylemon.struct.kleio.backend

import com.github.raylemon.struct.kleio.MainApp
import com.github.raylemon.struct.kleio.beans.Exercise
import com.github.raylemon.struct.kleio.beans.Instr
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.style.StyleTypeDefinitions
import java.io.File
import java.time.format.DateTimeFormatter


/**
 * Created by big04 on 21-12-15.
 */
class OdfWriter(val assessNumber: Int, val printSolution: Boolean) {

    private val doc = TextDocument.newTextDocument()

    fun buildHeader(instructions: Instr) {
        with(doc.header.addTable(2, 4)) {
            getCellByPosition(0, 0).setImage(MainApp::class.java.getResource("imgs/logo.png").toURI())
            getCellRangeByPosition(0, 0, 0, 1).merge()
            getCellByPosition(1, 0).apply {
                stringValue = "Structure des ordinateurs"
                setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.CENTER)
            }

            getCellByPosition(1, 1).apply {
                stringValue = instructions.mTitle.value
                setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.CENTER)
            }

            getCellByPosition(2, 0).stringValue = "Nom : "

            getCellByPosition(2, 1).stringValue = "Prénom : "

            getCellByPosition(3, 0).apply {
                stringValue = "/${instructions.mPoints.value.toString()}"
                setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.RIGHT)
            }

            getCellByPosition(3, 1).apply {
                stringValue = if (instructions.mChoice.value.equals("devoir", ignoreCase = true)) instructions.mDateDep.value.format(DateTimeFormatter.ofPattern("dd MM YYYY"))
                else instructions.mDateDoc.value.format(DateTimeFormatter.ofPattern("dd MM YYYY"))
                setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.RIGHT)
            }
            columnIterator.forEach { it.setUseOptimalWidth(if (it.columnIndex != 2) true else false) }
        }
        doc.addParagraph(instructions.mInstructions.value)
    }

    fun <T : Exercise> writeParagraph(instructions: String, exercise: List<T>) {
        doc.addParagraph(instructions).applyHeading()
        val pi = doc.addParagraph(null)
        exercise.forEach { pi.appendTextContent(it.toString() + if (printSolution) "${it.mSolution.value}\n" else "\n") }
    }

    fun save(path: File) = doc.save(path)
}