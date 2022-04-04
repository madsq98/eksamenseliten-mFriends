package easv.oe.mfriends.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.io.Serializable
import java.util.concurrent.Executors


class FriendService private constructor(private val context: Context) : Serializable {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "friends-database"
    ).build()

    private val friendDao = db.friendDao()

    private val executor = Executors.newFixedThreadPool(2)

    fun getAll(): LiveData<List<BEFriend>> = friendDao.getAll()

    fun addFriend(name: String, phoneNumber: String, email: String, imageUrl: String, isFavorite: Boolean) {
        val newFriend = BEFriend(0, name, phoneNumber, email, imageUrl, isFavorite)

        executor.execute { friendDao.insert(newFriend) }
    }

    fun getFriendById(id : Int): LiveData<BEFriend> {
        return friendDao.getById(id)
    }

    fun updateFriend(id: Int, obj: BEFriend): Boolean {
        obj.id = id

        executor.execute { friendDao.update(obj) }

        return true
    }

    fun deleteFriend(id: Int) {
        executor.execute { friendDao.delete(BEFriend(id,"","","","", false)) }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var Instance: FriendService? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = FriendService(context)
        }

        fun get(): FriendService {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Friend Repository not initialized")
        }
    }
}