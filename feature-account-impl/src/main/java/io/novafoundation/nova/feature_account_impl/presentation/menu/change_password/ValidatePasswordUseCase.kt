package io.novafoundation.nova.feature_account_impl.presentation.menu.change_password

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.view.InputFieldView
import io.novafoundation.nova.feature_account_impl.R
import java.util.regex.Pattern

class ValidatePasswordUseCase(private val resourceManager: ResourceManager) {

     fun validate(password: String, passwordConfirm: String): Pair<InputFieldView.InputFieldData, InputFieldView.InputFieldData> {
        val passwordState = InputFieldView.InputFieldData()
        val confirmState = InputFieldView.InputFieldData()
        val validateResult = validatePasswordRules(password)
        passwordState.apply {
            isError = !validateResult.first
            isSuccess = validateResult.first
            hint = validateResult.second
        }

        if (passwordConfirm.isNotEmpty()) {
            val confirmResult = passwordMatch(password, passwordConfirm)
            confirmState.apply {
                isError = !confirmResult.first
                isSuccess = confirmResult.first
                hint = confirmResult.second
            }
        }

        return Pair(passwordState, confirmState)
    }

    private val passwordRegexes: Array<Pattern> = arrayOf(

        Pattern.compile(".*[A-Z].*"),
        Pattern.compile(".*[a-z].*"),
        Pattern.compile(".*\\d.*"),
        Pattern.compile(".*[~!].*"),
    )

    private fun validatePasswordRules(text: String): Pair<Boolean, SpannableString> {
        val colorTertiary = resourceManager.getColor(R.color.contentTertiary)
        val colorRed = resourceManager.getColor(R.color.red500)
        val colorYellow = resourceManager.getColor(R.color.yellow500)
        val colorBlue = resourceManager.getColor(R.color.blue500)

        val colorGreen = resourceManager.getColor(R.color.green500)

        var strength = 0
        var isValid = false
        val span = if (text.length < 8) {
            isValid = false
            getColorSpan(resourceManager.getString(R.string.text_short), colorRed)
        } else {
            isValid = true
            passwordRegexes.forEach {
                if (it.matcher(text).matches()) {
                    strength++
                }
            }
            when (strength) {
                0 -> {
                    getColorSpan(resourceManager.getString(R.string.poor), colorRed)
                }
                1 -> {
                    getColorSpan(resourceManager.getString(R.string.poor), colorRed)
                }
                2 -> {
                    getColorSpan(resourceManager.getString(R.string.fair), colorYellow)

                }
                3 -> {
                    getColorSpan(resourceManager.getString(R.string.good), colorBlue)

                }
                4 -> {
                    getColorSpan(resourceManager.getString(R.string.excellent), colorGreen)

                }
                else -> {
                    getColorSpan(resourceManager.getString(R.string.poor), colorRed)
                }
            }
        }
        val passwordText = getColorSpan(resourceManager.getString(R.string.password_string), colorTertiary)
        val resultText = SpannableString(TextUtils.concat(passwordText, " ", span))
        return Pair(isValid, resultText)
    }

    private fun getColorSpan(text: String, color: Int): SpannableString {
        val span = SpannableString(text)
        val foregroundColorSpan = ForegroundColorSpan(color)
        span.setSpan(foregroundColorSpan, 0, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return span
    }

    private fun passwordMatch(text1: String, text2: String): Pair<Boolean, SpannableString> {
        return if (text1 == text2) {
            val hint =
                SpannableString(resourceManager.getString(R.string.its_a_match))
            Pair(true, SpannableString(hint))
        } else {
            val hint = resourceManager.getString(R.string.password_do_not_match)
            Pair(false, SpannableString(hint))
        }
    }
}
