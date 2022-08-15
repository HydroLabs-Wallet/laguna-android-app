package io.novafoundation.nova.core_db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val AddHistoryMarkersToAssets_13_15 = object : Migration(13, 15) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE assets ADD COLUMN hasIncomeHistory TEXT DEFAULT null")
    }
}
