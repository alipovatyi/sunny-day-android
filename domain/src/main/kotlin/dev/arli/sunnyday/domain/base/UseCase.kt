package dev.arli.sunnyday.domain.base

interface OutUseCase<out Output> {
    operator fun invoke(): Output
}
