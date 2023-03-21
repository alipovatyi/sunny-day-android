package dev.arli.sunnyday.data.db.room

import androidx.room.withTransaction
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import javax.inject.Inject

internal class RoomDatabaseTransactionRunner @Inject constructor(
    private val db: SunnyDayDatabase
) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction { block() }
    }
}
