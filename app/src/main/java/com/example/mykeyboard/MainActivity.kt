package com.example.mykeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mykeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private fun isKeyBoardEnabled() : Boolean {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethodIds = inputMethodManager.enabledInputMethodList.map {it.id}
        return enabledInputMethodIds.contains("com.example.mykeyboard/.MyKeyboard")
    }
    private fun openKeyboardChooserSettings(){
        val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        im.showInputMethodPicker()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            if (isKeyBoardEnabled())
                btnEnableKeyboard.isEnabled = false
            btnEnableKeyboard.setOnClickListener {
                if (!isKeyBoardEnabled())
                    openKeyboardSettings()
            }
            btnChooseKeyboard.setOnClickListener {
                if (isKeyBoardEnabled()){
                    openKeyboardChooserSettings()
                } else Toast.makeText(this@MainActivity, "Choose the keyboard activation button", Toast.LENGTH_SHORT).show()
            }
            btnUrlSubmit.setOnClickListener {
                if (!etUrl.text.isNullOrEmpty()){
                    Glide.with(imagePreview).load(etUrl.text.toString()).into(imagePreview)
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("url", etUrl.text.toString())
                    editor.apply()
                } else {
                    Toast.makeText(this@MainActivity, "Empty Url.", Toast.LENGTH_SHORT).show()
                    etUrl.setError("Empty url.")
                }
            }
        }

    }

    private fun openKeyboardSettings() {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
        startActivity(intent)
    }
}