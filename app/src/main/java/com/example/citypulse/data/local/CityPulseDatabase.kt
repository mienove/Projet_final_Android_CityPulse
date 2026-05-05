package com.example.citypulse.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.citypulse.model.Lieux

@Database(
    entities = [Lieux::class],  // ✅ Seulement Lieux
    version = 2,
    exportSchema = false
)
abstract class CityPulseDatabase : RoomDatabase() {

    abstract fun lieuxDAO(): LieuxDAO

    companion object {
        @Volatile
        private var INSTANCE: CityPulseDatabase? = null

        fun getInstance(context: Context): CityPulseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityPulseDatabase::class.java,
                    "citypulse_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration de version 1 à 2
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE lieux ADD COLUMN estFavori INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE lieux ADD COLUMN notePersonnelle TEXT DEFAULT ''")
            }
        }
    }
}