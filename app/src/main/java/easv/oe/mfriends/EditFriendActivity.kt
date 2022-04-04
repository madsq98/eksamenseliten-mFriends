package easv.oe.mfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import easv.oe.mfriends.model.Friends
import kotlinx.android.synthetic.main.activity_edit_friend.*

class EditFriendActivity : AppCompatActivity() {
    var friendsList : Friends = Friends()
    var isEditMode : Boolean = false
    var editFriendId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if(intent.extras != null) {
            val b = intent.extras!!
            friendsList = b.getSerializable("friendList") as Friends

            val editId = b.getInt("editFriendId")
            if(editId != null && editId > 0) {
                isEditMode = true
                editFriendId = editId
            }
        }

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_edit_friend)

        GoBackButton.setOnClickListener {
            endEditFriendActivity()
        }

        SaveFriendButton.setOnClickListener {
            val newName = FriendName.text.toString()
            val newPhone = FriendPhone.text.toString()
            val newIsFavorite = IsFavorite.isChecked

            if(newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(this, "Name and/or phone cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                val newFriend = friendsList.addFriend(newName, newPhone, newIsFavorite)
                Toast.makeText(this, "Friend " + newFriend.name + " was saved with ID " + newFriend.id, Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    endEditFriendActivity()
                },2000)
            }
        }
    }

    private fun endEditFriendActivity() {
        val i = Intent()
        val b = Bundle()
        b.putSerializable("friendList",friendsList)
        i.putExtras(b)
        setResult(RESULT_OK, i)
        finish()
    }
}