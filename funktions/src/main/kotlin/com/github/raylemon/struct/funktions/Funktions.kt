package com.github.raylemon.struct.funktions

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*


/**
 * Bibliothèque de fonctions
 *
 * @author Ray Lemon
 * @property random Générateur de nombres
 */

private val random = Random()

/**
 * *Extension de String*
 * Analyse le texte et le transforme en nombre selon la base indiquée
 * @return un nombre formaté selon la base
 * @throws NumberFormatException si le texte n'est pas convertissable
 */
fun String.toInt(base: Int) = Integer.parseInt(this, base)

/**
 * *Extension de String*
 * Nettoie un texte des siqnes superflus (0, 0x,b…)
 * @return le texte nettoyé
 */
fun String.clearString(): String = this.replace("^(b|0x?)".toRegex(), "")
        .replace("(\\(\\d*\\))$".toRegex(), "")
        .replace("\\s".toRegex(), "")

/**
 * *Extension de String*
 * Formate le message selon les bases indiquées
 * @return le message formaté
 */
fun String.formatMessage(newBase: Int, oldBase: Int = 2): String {
    val i = if (this == "0") BigInteger.ZERO.toInt() else BigInteger(this.clearString(), oldBase).toInt()
    var v: String
    when (newBase) {
        2 -> v = Integer.toBinaryString(i)
        8 -> v = Integer.toOctalString(i)
        10 -> return i.toString()
        16 -> v = Integer.toHexString(i).toUpperCase()
        else -> v = Integer.toString(i, newBase).toUpperCase()
    }
    v = v.reversed().replace("(.{4})(?!$)".toRegex(), "$1 ")
    return when (newBase) {
        2 -> "b" + v.reversed()
        8 -> "0" + v.reversed()
        16 -> "0x" + v.reversed()
        else -> v.reversed()
    }
}

/**
 * Ajoute les sigles de convention de nommage
 * @return la valeur du byte formaté avec sa convention de nommage
 */
fun Int.anyValue(base: Int): String {
    return when {
        this < -256 || this > 255 -> "Overflow"
        base == 2 -> "b" + Integer.toBinaryString(this and 255).padStart(8, '0')
        base == 8 -> "0" + Integer.toOctalString(this and 255)
        base == 16 -> "0x" + Integer.toHexString(this and 255).toUpperCase()
        else -> Integer.toString(this, base).toUpperCase() + " ($base)"
    }
}

/**
 * Génère un nombre flottant au format IEE16
 * @return un nombre flottant équivalent au format demi-précision
 */
fun getAnyFloat16(): Float {
    try {
        val tmp = Math.random()//random.nextInt()
        val b = tmp * (2 * Float16.INFINITY + 1 ) - Float16.INFINITY
        val v = Float16.toFloat(b.toInt())
        val bd = BigDecimal(v.toString())
        return bd.setScale(5, RoundingMode.HALF_EVEN).toFloat()
    } catch (e: Exception) {
        return if (random.nextBoolean()) Float.POSITIVE_INFINITY else Float.NEGATIVE_INFINITY
    }
}

/**
 * Génère une base aléatoire
 * @return 8 ou 16
 */
fun octOrHex() = if (random.nextBoolean()) 8 else 16

/**
 * Génère un message aléatoire
 * @return le message formaté en binaire
 */
fun generateMessage(): String {
    val MAX = 10000
    val MIN = 5000
    val f = random.nextFloat() * (MAX - MIN) + MIN
    val i = java.lang.Float.floatToIntBits(f) and 32767
    return Integer.toBinaryString(i)
}

/**
 * Génère un diviseur pour le CRC
 * @return un texte binaire représentant le diviseur
 */
fun getDivisor() = Integer.toBinaryString(random.nextInt(100))

/**
 * génère un nombre aléatoire
 * @param upper la borne supérieure
 * @return un nombre aléatoire compris entre [0 et upper[
 */
fun nextInt(upper: Int) = random.nextInt(upper)

/**
 * génère un boolean
 * @return un boolean
 */
fun nextBoolean() = random.nextBoolean()

fun Int.toBinaryString() = Integer.toBinaryString(this)