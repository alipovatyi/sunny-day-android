package dev.arli.sunnyday.data.device.base

import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

internal fun <TResult> mockTask(
    result: TResult? = null,
    exception: Exception? = null
): Task<TResult> {
    val mockTask = mockk<Task<TResult>>()

    val slotOnSuccessListener = slot<OnSuccessListener<TResult>>()
    val slotOnFailureListener = slot<OnFailureListener>()
    val slotOnCanceledListener = slot<OnCanceledListener>()

    every { mockTask.isCanceled } returns false
    every { mockTask.isComplete } returns true
    every { mockTask.result } returns result
    every { mockTask.exception } returns exception

    every { mockTask.addOnSuccessListener(capture(slotOnSuccessListener)) } answers {
        result?.let(slotOnSuccessListener.captured::onSuccess)
        mockTask
    }

    every { mockTask.addOnFailureListener(capture(slotOnFailureListener)) } answers {
        exception?.let(slotOnFailureListener.captured::onFailure)
        mockTask
    }

    every { mockTask.addOnCanceledListener(capture(slotOnCanceledListener)) } answers { mockTask }

    return mockTask
}
