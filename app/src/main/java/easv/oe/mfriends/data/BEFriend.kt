package easv.oe.mfriends.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BEFriend(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    var phone: String,
    var email: String,
    var imageUrl: String,
    var isFavorite: Boolean
    ) : Serializable {
}