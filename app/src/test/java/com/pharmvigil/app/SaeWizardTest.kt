package com.pharmvigil.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SaeWizardTest {

    private fun evaluateRegulatoryClock(
        isDeath: Boolean,
        isLifeThreatening: Boolean,
        isHospitalization: Boolean,
        isDisability: Boolean,
        isRelated: Boolean,
        isExpected: Boolean
    ): Triple<Boolean, Boolean, String> {
        val isSerious = isDeath || isLifeThreatening || isHospitalization || isDisability
        val isSusar = isSerious && isRelated && !isExpected
        val isFatalOrLifeThreateningSusar = isSusar && (isDeath || isLifeThreatening)

        val clock = when {
            isFatalOrLifeThreateningSusar -> "7-DAY"
            isSusar -> "15-DAY"
            else -> "NO-EXPEDITED-CLOCK"
        }

        return Triple(isSerious, isSusar, clock)
    }

    @Test
    fun testFatalSusarTriggers7DayClock() {
        val (isSerious, isSusar, clock) = evaluateRegulatoryClock(
            isDeath = true,
            isLifeThreatening = false,
            isHospitalization = true,
            isDisability = false,
            isRelated = true,
            isExpected = false
        )
        assertTrue(isSerious)
        assertTrue(isSusar)
        assertEquals("7-DAY", clock)
    }

    @Test
    fun testHospitalizationSusarTriggers15DayClock() {
        val (isSerious, isSusar, clock) = evaluateRegulatoryClock(
            isDeath = false,
            isLifeThreatening = false,
            isHospitalization = true,
            isDisability = false,
            isRelated = true,
            isExpected = false
        )
        assertTrue(isSerious)
        assertTrue(isSusar)
        assertEquals("15-DAY", clock)
    }

    @Test
    fun testExpectedSaeIsNotSusar() {
        val (isSerious, isSusar, clock) = evaluateRegulatoryClock(
            isDeath = false,
            isLifeThreatening = false,
            isHospitalization = true,
            isDisability = false,
            isRelated = true,
            isExpected = true // Listed in IB
        )
        assertTrue(isSerious)
        assertFalse(isSusar)
        assertEquals("NO-EXPEDITED-CLOCK", clock)
    }
}
