package com.cmtelematics.cmtreferenceapp.common.test

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestMainDispatcherExtension::class)
open class BaseMockkTest {
    @BeforeEach
    fun setupMockk() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @AfterEach
    fun tearDownMockk() {
        clearAllMocks()
        unmockkAll()
    }
}
