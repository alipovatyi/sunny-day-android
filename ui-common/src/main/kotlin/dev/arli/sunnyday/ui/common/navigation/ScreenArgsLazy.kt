@file:Suppress("Filename")

package dev.arli.sunnyday.ui.common.navigation

import android.annotation.SuppressLint
import androidx.collection.ArrayMap
import androidx.lifecycle.SavedStateHandle
import java.lang.reflect.Method
import kotlin.reflect.KClass

private val methodMap = ArrayMap<KClass<out ScreenArguments>, Method>()

class ScreenArgsLazyImpl<Args : ScreenArguments>(
    private val screenArgsClass: KClass<Args>,
    private val savedStateHandle: SavedStateHandle
) : Lazy<Args> {

    private var cached: Args? = null

    override val value: Args
        get() {
            var args = cached
            if (args == null) {
                val method = methodMap[screenArgsClass] ?: screenArgsClass.java.getMethod(
                    ScreenArguments.Companion::fromStateHandle.name,
                    SavedStateHandle::class.java
                ).also { method ->
                    methodMap[screenArgsClass] = method
                }

                @SuppressLint("BanUncheckedReflection") // needed for method.invoke
                @Suppress("UNCHECKED_CAST")
                args = method.invoke(null, savedStateHandle) as Args
                cached = args
            }
            return args
        }

    override fun isInitialized(): Boolean = cached != null
}
