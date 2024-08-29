package com.example.thebulletin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.thebulletin.models.Article

@Database(
    entities = [Article::class],
    version = 2
)
@TypeConverters(Converters::class
)
abstract class ArticleDatabase :RoomDatabase(){
    abstract fun getarticleDao(): ArticleDao

    companion object{
        @Volatile //other threat can immediately see when thread changes
        private var instance:ArticleDatabase?=null //instance of our database
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db").fallbackToDestructiveMigration().build()
    }
}