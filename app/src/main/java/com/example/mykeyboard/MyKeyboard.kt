package com.example.mykeyboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.ColorSpace.Rgb
import android.inputmethodservice.InputMethodService
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.example.mykeyboard.databinding.KeyboardLayoutBinding

class MyKeyboard : InputMethodService(){
    override fun onCreateInputView(): View {
        val keyboardBinding = KeyboardLayoutBinding.inflate(layoutInflater)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val url = sharedPreferences.getString("url", "")
         Glide.with(keyboardBinding.imageBackground.context).asGif().load(url).into(keyboardBinding.imageBackground)

        //List of buttons IDs in your layout
        val buttonIds = arrayOf(
            // Numeric buttons
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,

            // Alphabet buttons
            R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF,
            R.id.btnG, R.id.btnH, R.id.btnI, R.id.btnJ, R.id.btnK, R.id.btnL,
            R.id.btnM, R.id.btnN, R.id.btnO, R.id.btnP, R.id.btnQ, R.id.btnR,
            R.id.btnS, R.id.btnT, R.id.btnU, R.id.btnV, R.id.btnW, R.id.btnX,
            R.id.btnY, R.id.btnZ,

            // Additional buttons
            R.id.btnComma,
            R.id.btnPeriod,
            R.id.btnSpecial
        )

        for(buttonId in buttonIds){
            val button = keyboardBinding.root.findViewById<Button>(buttonId)
            button.setOnClickListener { view ->
                val inputConnection = currentInputConnection
                val character = button.text.toString()

                inputConnection?.commitText(button.text.toString(),1)

                showLargeCharacterPopup(view, character)
            }
        }

        keyboardBinding.btnBack.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL))
            return@setOnClickListener
        }

        keyboardBinding.btnEnter.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER))
            return@setOnClickListener
        }

        keyboardBinding.btnSpace.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_SPACE))
            return@setOnClickListener
        }

        return keyboardBinding.root
    }

    private fun showLargeCharacterPopup(button: View, character: String) {
        // Create the TextView for the popup
        val popupTextView = TextView(this).apply {
            text = character
            textSize = 50f
            setTextColor(resources.getColor(android.R.color.white))
            gravity = Gravity.CENTER
            setBackgroundResource(android.R.color.black)
            setPadding(20, 20, 20, 20)
        }

        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupTextView,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = false
            elevation = 8f
        }

        // Display the popup
        popupWindow.showAtLocation(
            button,
            Gravity.NO_GRAVITY,
            0,
            -200
        )

        // Fade out and dismiss the popup
        popupTextView.animate()
            .alpha(0f)
            .setDuration(500)
            .withEndAction {
                popupWindow.dismiss()
            }
            .start()
    }
}