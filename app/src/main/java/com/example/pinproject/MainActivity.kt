package com.example.pinproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity

//TODO Add view Model correctly


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClicks()

        //TODO Add onTextListener to enable/disable submit button
    }

    private fun setOnClicks() {
        submitPinButton.setOnClickListener {
            val currentPin = pinNumberInputDisplay.text.toString()
            if (viewModel.invalidPinLength(currentPin)) {
                toast("Chuck some more characters in there please buddy")
            } else {
                val pinAndPan = viewModel.getPinAndPan(currentPin)
                pinAndPan.second?.let { pan ->
                    val pinAndPanText =
                        viewModel.comparePinAndPanAndGenerateXor(pinAndPan.first, pan)
                    pinAndPanText?.let { generatedText ->
                        blockNumber.text = generatedText
                        blockContainer.visibility = View.VISIBLE
                    } ?: run { toast("Pan or Pin were not the correct length") }
                } ?: run { toast("Pan number is not of required length") }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}