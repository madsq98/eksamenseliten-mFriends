package easv.oe.mfriends.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FriendDao {
    @Query("SELECT * FROM BEFriend ORDER BY id")
    fun getAll(): LiveData<List<BEFriend>>

    @Query("SELECT * FROM BEFriend WHERE id = (:id)")
    fun getById(id: Int): LiveData<BEFriend>

    @Insert
    fun insert(obj: BEFriend)

    @Update
    fun update(obj: BEFriend)

    @Delete
    fun delete(obj: BEFriend)
}