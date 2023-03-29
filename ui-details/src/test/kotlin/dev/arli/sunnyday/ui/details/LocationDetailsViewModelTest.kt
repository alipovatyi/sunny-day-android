package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal class LocationDetailsViewModelTest : BehaviorSpec({

    lateinit var viewModel: LocationDetailsViewModel

    beforeEach {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = LocationDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("latitude" to 52.23, "longitude" to 21.01))
        )
    }

    afterEach {
        Dispatchers.resetMain()
    }
})
