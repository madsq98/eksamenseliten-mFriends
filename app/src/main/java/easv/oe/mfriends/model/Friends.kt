package easv.oe.mfriends.model

import java.io.Serializable


class Friends : Serializable {
    val mFriends = mutableListOf<BEFriend>(
        BEFriend(1,"Simon", "123", "test1@mail.dk", true),
        BEFriend(2,"Dennis", "1234","test2@mail.dk", false),
        BEFriend(3,"Mina", "12345","test3@mail.dk", true),
        BEFriend(4,"Emil", "12345678","test4@mail.dk", true),
        BEFriend(5,"Mads", "23456789","test5@mail.dk", true),
        BEFriend(6,"Martin", "87654321","test6@mail.dk", false),
        BEFriend(7,"Mike", "12121212","test7@mail.dk", true),
        BEFriend(8,"Trine", "123","test8@mail.dk", true),
        BEFriend(9,"Mathias", "1234","test9@mail.dk", false),
        BEFriend(10,"Rasmus", "12345","test10@mail.dk", true),
        BEFriend(11,"Christian", "12345678","test11@mail.dk", true),
        BEFriend(12,"Peter", "23456789","test12@mail.dk", true),
        BEFriend(13,"Anders", "87654321","test13@mail.dk", false),
        BEFriend(14,"Mikkel", "12121212","test14@mail.dk", true),
        BEFriend(15,"Flemming", "123","test15@mail.dk", true),
        BEFriend(16,"Jonas", "1234","test16@mail.dk", false),
        BEFriend(17,"Frederik", "12345","test17@mail.dk", true),
        BEFriend(18,"Mantas", "12345678","test18@mail.dk", true),
        BEFriend(19,"Michael", "23456789","test19@mail.dk", true),
        BEFriend(20,"Jens", "87654321","test20@mail.dk", false),
        BEFriend(21,"Jan", "12121212","test21@mail.dk", true)
    )

    fun getAll(): Array<BEFriend> = mFriends.toTypedArray()

    private fun getNextId(): Int {
        return if(mFriends.size > 0)
            mFriends[mFriends.size - 1].id + 1
        else
            1
    }

    private fun getIndexFromId(id : Int): Int {
        for(i in 0..mFriends.size) {
            if(mFriends[i].id == id)
                return i
        }

        return -1
    }


    fun addFriend(name: String, phoneNumber: String, email: String, isFavorite: Boolean): BEFriend {
        val newId = getNextId()
        val newFriend = BEFriend(newId, name, phoneNumber, email, isFavorite)
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

    fun updateFriend(id: Int, obj: BEFriend): Boolean {
        val index = getIndexFromId(id)

        if(index >= 0) {
            mFriends[index] = obj
            return true
        }

        return false
    }

    fun deleteFriend(id: Int): Boolean {
        val index = getIndexFromId(id)

        if(index >= 0) {
            mFriends.removeAt(index)
            return true
        }

        return false
    }
}