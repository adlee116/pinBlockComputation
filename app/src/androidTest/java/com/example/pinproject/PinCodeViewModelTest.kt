package com.example.pinproject

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PinCodeViewModelTest {

    private val viewModel: PinCodeViewModel = PinCodeViewModel()
    private val fourLengthString = "1234"

    @Test
    fun shortPinIsInvalid() {
        assert(viewModel.invalidPinLength("111"))
    }

    @Test
    fun longPinIsInvalid() {
        assert(viewModel.invalidPinLength("1234567890123"))
    }

    @Test
    fun fourDigitsIsValid() {
        assert(!viewModel.invalidPinLength(fourLengthString))
    }

    @Test
    fun twelveDigitsIsValid() {
        assert(!viewModel.invalidPinLength("123456789012"))
    }

    @Test
    fun inputIntsStillRemain() {
        val generatedFourString = viewModel.generatePin(fourLengthString)
        assertTrue(generatedFourString[2] == 1)
        assertTrue(generatedFourString[3] == 2)
        assertTrue(generatedFourString[4] == 3)
        assertTrue(generatedFourString[5] == 4)

        val sixLengthString = "123456"
        val generatedSixString = viewModel.generatePin(sixLengthString)
        assertTrue(generatedSixString[2] == 1)
        assertTrue(generatedSixString[3] == 2)
        assertTrue(generatedSixString[4] == 3)
        assertTrue(generatedSixString[5] == 4)
        assertTrue(generatedSixString[6] == 5)
        assertTrue(generatedSixString[7] == 6)

        val twelveLengthString = "123456789012"
        val generatedTwelveString = viewModel.generatePin(twelveLengthString)
        assertTrue(generatedTwelveString[2] == 1)
        assertTrue(generatedTwelveString[3] == 2)
        assertTrue(generatedTwelveString[4] == 3)
        assertTrue(generatedTwelveString[5] == 4)
        assertTrue(generatedTwelveString[6] == 5)
        assertTrue(generatedTwelveString[7] == 6)
        assertTrue(generatedTwelveString[8] == 7)
        assertTrue(generatedTwelveString[9] == 8)
        assertTrue(generatedTwelveString[10] == 9)
        assertTrue(generatedTwelveString[11] == 0)
        assertTrue(generatedTwelveString[12] == 1)
        assertTrue(generatedTwelveString[13] == 2)
    }

    @Test
    fun generatePinCreatesCorrectLength() {
        val generatedFourString = viewModel.generatePin(fourLengthString)
        assertTrue(generatedFourString.size == 16)
    }

    @Test
    fun remainingCharsAreRandomNumbers() {
        val generatedFourString = viewModel.generatePin(fourLengthString)
        for(i in 6..15) {
            assertTrue(generatedFourString[i] in 10..15)
        }
    }

    @Test
    fun randomNumbersBetweenTenAndFifteen() {
        repeat(10) {
            assertTrue(viewModel.generateRandomFill() in 10..15)
        }
    }

    @Test
    fun pinArrayCreatedAsExpected() {
        val generatedPinArray = viewModel.generateCurrentPinArray(fourLengthString)
        assertThat(generatedPinArray, equalTo(mutableListOf(1,2,3,4)))
    }

    @Test
    fun combineWorksAsExpected() {
        val formatInt = 2
        val lengthInt = 5
        val remainingList = mutableListOf(1, 2, 3, 4, 5, 11, 12, 13, 14, 15, 11, 12, 13, 14)
        val combinedList = viewModel.combinePinLengthFormat(formatInt, lengthInt, remainingList)
        assertThat(combinedList, equalTo(mutableListOf(2, 5, 1, 2, 3, 4, 5, 11, 12, 13, 14, 15, 11, 12, 13, 14)))
    }

    @Test
    fun getPanNumberReturnsCorrectPanNumber() {
        assertThat(viewModel.getPanNumber(), equalTo("0000222233334444"))
    }

    @Test
    fun compareReturnsNullWhenEitherIsTooShortOrLong(){
        val shortPan = "111122223333444"
        val correctPan = "1111222233334444"
        val longPan = "11112222333344445"
        val shortPin = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        val correctPin = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val longPin = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)

        assertTrue(viewModel.comparePinAndPanAndGenerateXor(shortPin, correctPan) == null)
        assertTrue(viewModel.comparePinAndPanAndGenerateXor(longPin, correctPan) == null)
        assertTrue(viewModel.comparePinAndPanAndGenerateXor(correctPin, shortPan) == null)
        assertTrue(viewModel.comparePinAndPanAndGenerateXor(correctPin, longPan) == null)
        assertTrue(viewModel.comparePinAndPanAndGenerateXor(shortPin, correctPan) == null)

        assertTrue(viewModel.comparePinAndPanAndGenerateXor(correctPin, correctPan) != null)
    }

    @Test
    fun correctlyComparingPinAndPan() {
        val pin1 = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6)
        val pan1 = "1234567890123456"
        assertThat(viewModel.comparePinAndPanAndGenerateXor(pin1, pan1), equalTo("0000000000000000"))

        val pan2 = "2345678901234567"
        assertThat(viewModel.comparePinAndPanAndGenerateXor(pin1, pan2), equalTo("1111111111111111"))

        val pan3 = "1030507091103050"
        assertThat(viewModel.comparePinAndPanAndGenerateXor(pin1, pan3), equalTo("0101010101010101"))
    }

    @Test
    fun correctXorGivenWhenComparingDetails() {
        assertThat(viewModel.comparePinAndPanDetails(1, "1"), equalTo(0))
        assertThat(viewModel.comparePinAndPanDetails(1, "12"), equalTo(1))
        assertThat(viewModel.comparePinAndPanDetails(9, "9"), equalTo(0))
        assertThat(viewModel.comparePinAndPanDetails(5, "4"), equalTo(1))
    }
}