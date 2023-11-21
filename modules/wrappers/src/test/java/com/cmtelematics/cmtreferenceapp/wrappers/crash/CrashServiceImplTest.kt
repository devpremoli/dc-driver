package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.net.Uri
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.wrappers.getTestDispatcherProvider
import com.cmtelematics.sdk.PassThruRequester
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.slot
import io.reactivex.Observable
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

internal class CrashServiceImplTest : BaseMockkTest() {

    @MockK
    private lateinit var passThruRequester: PassThruRequester

    @RelaxedMockK
    private lateinit var uri: Uri

    @BeforeEach
    fun setup() {
        mockkStatic(Uri::class)

        every { Uri.parse(any()) } returns uri
        every { passThruRequester.request(any(), any(), any(), any(), any(), any()) } returns Observable.just("")
    }

    @Test
    fun `request method of passThruRequester should be called`() = runTest {
        val sut = createSut()
        sut.updateCrash(DEFAULT_CRASH_ID, true, null)

        coVerify { passThruRequester.request(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `updateCrash should send the request with Json which verified field is true if crash is verified`() = runTest {
        val sut = createSut()
        sut.updateCrash(DEFAULT_CRASH_ID, true, Instant.now())

        val jsonSlot = slot<String>()
        coVerify { passThruRequester.request(any(), any(), any(), any(), capture(jsonSlot), any()) }
        val crashResponseJson = jsonSlot.captured
        assertThat(crashResponseJson).contains("\"user_verified\":true")
    }

    @Test
    fun `updateCrash should send the request with Json which verificationDateTime field is current date if crash occurred now`() =
        runTest {
            val sut = createSut()
            val currentDate = Instant.now()
            sut.updateCrash(DEFAULT_CRASH_ID, true, currentDate)

            val jsonSlot = slot<String>()
            coVerify { passThruRequester.request(any(), any(), any(), any(), capture(jsonSlot), any()) }
            val crashResponseJson = jsonSlot.captured
            assertThat(crashResponseJson).contains("\"user_verified_datetime\":${currentDate.epochSecond}")
        }

    private fun TestScope.createSut() = CrashServiceImpl(
        dispatcherProvider = getTestDispatcherProvider(StandardTestDispatcher(testScheduler)),
        passThruRequester = passThruRequester
    )

    companion object {
        private const val DEFAULT_CRASH_ID = 111L
    }
}
