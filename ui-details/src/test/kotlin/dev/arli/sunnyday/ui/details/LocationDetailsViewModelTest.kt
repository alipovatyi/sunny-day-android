package dev.arli.sunnyday.ui.details

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

        viewModel = LocationDetailsViewModel()
    }

    afterEach {
        Dispatchers.resetMain()
    }
})
