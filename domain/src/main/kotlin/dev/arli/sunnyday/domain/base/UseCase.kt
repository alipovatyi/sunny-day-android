package dev.arli.sunnyday.domain.base

interface OutUseCase<out Output> {
    operator fun invoke(): Output
}

interface SuspendInOutUseCase<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output
}
