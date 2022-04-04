package com.easv.oe.friends.Model


class Friends {

    val mFriends = arrayOf<BEFriend>(
        BEFriend("Simon", "123", true),
        BEFriend("Dennis", "1234", false),
        BEFriend("Mina", "12345", true),
        BEFriend("Emil", "12345678", true),
        BEFriend("Mads", "23456789", true),
        BEFriend("Martin", "87654321", false),
        BEFriend("Mike", "12121212", true),
        BEFriend("Trine", "123", true),
        BEFriend("Mathias", "1234", false),
        BEFriend("Rasmus", "12345", true),
        BEFriend("Christian", "12345678", true),
        BEFriend("Peter", "23456789", true),
        BEFriend("Anders", "87654321", false),
        BEFriend("Mikkel", "12121212", true),
        BEFriend("Flemming", "123", true),
        BEFriend("Jonas", "1234", false),
        BEFriend("Frederik", "12345", true),
        BEFriend("Mantas", "12345678", true),
        BEFriend("Michael", "23456789", true),
        BEFriend("Jens", "87654321", false),
        BEFriend("Jan", "12121212", true)
    )

    fun getAll(): Array<BEFriend> = mFriends


    fun getAllNames(): Array<String> = mFriends.map { p -> p.name }.toTypedArray()


}