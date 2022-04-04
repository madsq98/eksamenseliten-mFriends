package easv.oe.mfriends.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BEFriend::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao() : FriendDao
}