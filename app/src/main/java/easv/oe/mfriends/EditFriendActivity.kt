package easv.oe.mfriends

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import easv.oe.mfriends.model.BEFriend
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

        DeleteFriendButton.visibility = View.GONE
        ActionsBar.visibility = View.GONE

        if(isEditMode) {
            val editFriendObject = friendsList.getFriendById(editFriendId)!!

            FriendName.setText(editFriendObject.name)
            FriendPhone.setText(editFriendObject.phone)
            FriendEmail.setText(editFriendObject.email)
            IsFavorite.isChecked = editFriendObject.isFavorite

            DeleteFriendButton.visibility = View.VISIBLE
            ActionsBar.visibility = View.VISIBLE
        }

        //Handler for Back Button
        GoBackButton.setOnClickListener {
            endEditFriendActivity()
        }

        //Handler for Delete Friend Button
        DeleteFriendButton.setOnClickListener {
            friendsList.deleteFriend(editFriendId)
            endEditFriendActivity()
        }

        //Handler for Save Friend Button
        SaveFriendButton.setOnClickListener {
            val newName = FriendName.text.toString()
            val newPhone = FriendPhone.text.toString()
            val newEmail = FriendEmail.text.toString()
            val newIsFavorite = IsFavorite.isChecked

            if(newName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and/or phone cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                if(!isEditMode) {
                    val newFriend = friendsList.addFriend(newName, newPhone, newEmail, newIsFavorite)
                    Toast.makeText(
                        this,
                        "Friend " + newFriend.name + " was saved with ID " + newFriend.id,
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        endEditFriendActivity()
                    }, 1500)
                } else {
                    val newObj = BEFriend(editFriendId, newName, newPhone, newEmail, newIsFavorite)
                    var finishString = "An error occured! Try again later."
                    if(friendsList.updateFriend(editFriendId, newObj)) {
                        finishString = "Friend " + newName + " with ID " + editFriendId + " was updated!"
                    }

                    Toast.makeText(this, finishString, Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        endEditFriendActivity()
                    }, 1500)
                }
            }
        }

        val errorMessage = "No application found to handle action!"

        //Handler for Call Friend Button
        FriendCallButton.setOnClickListener {
            val phoneNumber = friendsList.getFriendById(editFriendId)?.phone
            val uri = "tel:" + phoneNumber

            val callIntent: Intent = Uri.parse(uri).let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }

            try {
                startActivity(callIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        //Handler for SMS Friend Button
        FriendSMSButton.setOnClickListener {
            val phoneNumber = friendsList.getFriendById(editFriendId)?.phone
            val uri = "smsto:" + phoneNumber

            val messageIntent: Intent = Uri.parse(uri).let { number ->
                Intent(Intent.ACTION_SENDTO, number)
            }

            messageIntent.putExtra(Intent.EXTRA_TEXT, "From Awesome Friends List: ")

            try {
                startActivity(messageIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        //Handler for Email Friend Button
        FriendEmailButton.setOnClickListener {
            val email = friendsList.getFriendById(editFriendId)?.email
            val uri = "mailto:" + email

            val mailIntent: Intent = Uri.parse(uri).let { mail ->
                Intent(Intent.ACTION_SENDTO, mail)
            }

            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "From Awesome Friends List: ")

            try {
                startActivity(mailIntent)
            } catch(e: ActivityNotFoundException) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
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