package uk.co.applylogic.marvel.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openpayd.data.room.dao.ResultsDao
import com.openpayd.data.room.model.ResultsModel

@Database(
        entities = [ResultsModel::class],
        version = AppDatabase.VERSION,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun supportedResultsDao(): ResultsDao

    companion object {

        private val TAG by lazy { AppDatabase::class.java.simpleName }
        const val VERSION = 1
        private const val DATABASE_NAME = "results_main.db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            Log.d(TAG, "buildDatabase()")
            return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d(TAG, "buildDatabase(): onCreate")
                            super.onCreate(db)
                        }
                    })
                    .addMigrations()
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
