package com.example.pinproject

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    //TODO Due to the shortfall in experience in regard to storing in Byte's etc, I have tried to achieve what has been asked for with
    //TODO a more basic approach. Would love to see how this is intended to be done with the 'useful code'

    // TODO implement Koin and use for viewModel injection

    // Methods are not private for ease of testing
    // TODO Better structure project to allow protection of these methods and still allow testing

    fun getPinAndPan(userInput: String): Pair<MutableList<Int>, String?> {
        return Pair(generatePin(userInput), getPanNumber())
    }

    fun invalidPinLength(pin: String): Boolean {
        return pin.length < 4 || pin.length > 12
    }

    fun generatePin(pin: String): MutableList<Int> {
        val pinArray = generateCurrentPinArray(pin)
        while (pinArray.size < 14) {
            pinArray.add(generateRandomFill())
        }
        return combinePinLengthFormat(formatInt, pin.length, pinArray)
    }

    fun generateCurrentPinArray(pin: String): MutableList<Int> {
        val pinArray = mutableListOf<Int>()
        for (i in pin.indices) {
            pinArray.add(pin[i].toString().toInt())
        }
        return pinArray
    }

    fun generateRandomFill(): Int {
        val random = Random
        return random.nextInt(10, 15)
    }

    fun combinePinLengthFormat(
        format: Int,
        length: Int,
        pinArray: MutableList<Int>
    ): MutableList<Int> {
        val additions = mutableListOf(format, length)
        pinArray.addAll(0, additions)
        return pinArray
    }

    fun getPanNumber(): String? {
        // Will this pan number ever be of different length? Would need to make this substring safe
        // This string is being judged as though the numerics will be taken as single digit numerics. Never above 9
        return "0000" + panNumber.substring(4, 16)
    }

    fun comparePinAndPanAndGenerateXor(list: MutableList<Int>, pan: String): String? {
        val combinedNumbers: MutableList<Int> = mutableListOf()
        //TODO
        //Documentation isn't clear or I am not understanding it correctly
        //https://www.eftlab.com/knowledge-base/261-complete-list-of-pin-blocks-in-payments/
        // Prepare pin states format number should be 3, XOR (3rd step) has this value as 0
        // Meaning the format number has no relevance?
        if (list.size != 16 || pan.length != 16) {
            // Returning null here isn't the best way to deal with this
            // Check sizes before giving these params to this method
            return null
        } else {
            for (i in 0 until list.size) {
                combinedNumbers.add(comparePinAndPanDetails(list[i], pan[i].toString()))
            }
        }
        val stringBuilder = StringBuilder()
        combinedNumbers.forEach {
            stringBuilder.append(it)
        }
        return stringBuilder.toString()
    }

    fun comparePinAndPanDetails(pin: Int, pan: String): Int {
        // have I understood XoR correctly here?
        return if (pin == pan.toInt()) {
            0
        } else {
            1
        }
    }

    //TODO perhaps move this out to a repository layer
    companion object {
        private const val formatInt = 3
        private const val panNumber = "1111222233334444"
    }

}