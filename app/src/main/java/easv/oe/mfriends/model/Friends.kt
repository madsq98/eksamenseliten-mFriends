package easv.oe.mfriends.model

import java.io.Serializable


class Friends : Serializable {

    /*
    val mFriends = mutableListOf<BEFriend>(
        BEFriend(1,"Simon", "123", true),
        BEFriend(2,"Dennis", "1234", false),
        BEFriend(3,"Mina", "12345", true),
        BEFriend(4,"Emil", "12345678", true),
        BEFriend(5,"Mads", "23456789", true),
        BEFriend(6,"Martin", "87654321", false),
        BEFriend(7,"Mike", "12121212", true),
        BEFriend(8,"Trine", "123", true),
        BEFriend(9,"Mathias", "1234", false),
        BEFriend(10,"Rasmus", "12345", true),
        BEFriend(11,"Christian", "12345678", true),
        BEFriend(12,"Peter", "23456789", true),
        BEFriend(13,"Anders", "87654321", false),
        BEFriend(14,"Mikkel", "12121212", true),
        BEFriend(15,"Flemming", "123", true),
        BEFriend(16,"Jonas", "1234", false),
        BEFriend(17,"Frederik", "12345", true),
        BEFriend(18,"Mantas", "12345678", true),
        BEFriend(19,"Michael", "23456789", true),
        BEFriend(20,"Jens", "87654321", false),
        BEFriend(21,"Jan", "12121212", true)
    )
     */

    val mFriends = mutableListOf<BEFriend>()

    fun getAll(): Array<BEFriend> = mFriends.toTypedArray()

    private fun getNextId(): Int {
        return if(mFriends.size > 0)
            mFriends[mFriends.size - 1].id + 1
        else
            1
    }

    fun addFriend(name: String, phoneNumber: String, isFavorite: Boolean): BEFriend {
        val newId = getNextId()
        val newFriend = BEFriend(newId, name, phoneNumber, isFavorite)
        mFriends.add(newFriend)

        return newFriend
    }

    fun getFriendById(id : Int): BEFriend? {
        for(friend in mFriends) {
            if(friend.id == id)
                return friend
        }

        return null
    }

    fun getFriendByIndex(index: Int): BEFriend {
        return mFriends[index]
    }
}