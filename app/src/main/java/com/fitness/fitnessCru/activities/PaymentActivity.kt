package com.fitness.fitnessCru.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.fitness.fitnessCru.R

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val etCardNumber: EditText = findViewById(R.id.etCardNumber)
        val etCardDate: EditText = findViewById(R.id.etCardDate)
        val etCardCVV: EditText = findViewById(R.id.etCardCVV)
        etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // noop
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(
                        0,
                        s.length,
                        buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                        )
                    )
                }
            }
        })
        etCardDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(
                        s,
                        CARD_DATE_TOTAL_SYMBOLS,
                        CARD_DATE_DIVIDER_MODULO,
                        CARD_DATE_DIVIDER
                    )
                ) {
                    s.replace(
                        0,
                        s.length,
                        concatString(
                            getDigitArray(s, CARD_DATE_TOTAL_DIGITS),
                            CARD_DATE_DIVIDER_POSITION,
                            CARD_DATE_DIVIDER
                        )
                    )
                }
            }
        })
        etCardCVV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > CARD_CVC_TOTAL_SYMBOLS) {
                    s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length)
                }
            }
        })
    }

    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String {
        val formatted: StringBuilder = StringBuilder()
        for (i in digits.indices) {
            if (digits.get(i) != null) {
                formatted.append(digits.get(i))
                if ((i > 0) && (i < (digits.size - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider)
                }
            }
        }
        return formatted.toString()
    }

    private fun isInputCorrect(
        s: Editable,
        totalSymbols: Int,
        dividerModulo: Int,
        divider: Char
    ): Boolean {
        var isCorrect: Boolean = s.length <= totalSymbols // check size of entered string
        for (i in 0 until s.length) { // check that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect = isCorrect and (divider == s.get(i))
            } else {
                isCorrect = isCorrect and Character.isDigit(s.get(i))
            }
        }
        return isCorrect
    }

    private fun buildCorrectString(digits: CharArray, dividerPosition: Int, divider: Char): String {
        val formatted: StringBuilder = StringBuilder()
        for (i in digits.indices) {
            if (digits.get(i) != null) {
                formatted.append(digits.get(i))
                if ((i > 0) && (i < (digits.size - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider)
                }
            }
        }
        return formatted.toString()
    }

    private fun getDigitArray(s: Editable, size: Int): CharArray {
        val digits: CharArray = CharArray(size)
        var index: Int = 0
        var i: Int = 0
        while (i < s.length && index < size) {
            val current: Char = s.get(i)
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }

    companion object {
        private val TOTAL_SYMBOLS: Int = 19 // size of pattern 0000-0000-0000-0000
        private val TOTAL_DIGITS: Int = 16 // max numbers of digits in pattern: 0000 x 4
        private val DIVIDER_MODULO: Int =
            5 // means divider position is every 5th symbol beginning with 1
        private val DIVIDER_POSITION: Int =
            DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
        private val DIVIDER: Char = '-'
        private val CARD_DATE_TOTAL_SYMBOLS: Int = 5 // size of pattern MM/YY
        private val CARD_DATE_TOTAL_DIGITS: Int = 4 // max numbers of digits in pattern: MM + YY
        private val CARD_DATE_DIVIDER_MODULO: Int =
            3 // means divider position is every 3rd symbol beginning with 1
        private val CARD_DATE_DIVIDER_POSITION: Int =
            CARD_DATE_DIVIDER_MODULO - 1 // means divider position is every 2nd symbol beginning with 0
        private val CARD_DATE_DIVIDER: Char = '/'
        private val CARD_CVC_TOTAL_SYMBOLS: Int = 3
    }
}