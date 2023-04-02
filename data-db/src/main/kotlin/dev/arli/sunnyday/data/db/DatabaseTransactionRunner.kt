package dev.arli.sunnyday.data.db

interface DatabaseTransactionRunner {
    suspend operator fun <T> invoke(block: suspend () -> T): T
}
