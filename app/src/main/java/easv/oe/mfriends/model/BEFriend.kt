package easv.oe.mfriends.model

import java.io.Serializable

data class BEFriend(var id: Int, var name: String, var phone: String, var isFavorite: Boolean ) : Serializable {
}