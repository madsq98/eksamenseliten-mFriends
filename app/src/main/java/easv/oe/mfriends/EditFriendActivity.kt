package easv.oe.mfriends

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import easv.oe.mfriends.data.BEFriend
import easv.oe.mfriends.data.FriendService
import kotlinx.android.synthetic.main.activity_edit_friend.*
import java.io.ByteArrayOutputStream


class EditFriendActivity : AppCompatActivity() {
    private lateinit var friendsList: FriendService
    var isEditMode : Boolean = false
    var editFriendId : Int = 0
    val REQUEST_IMAGE_CAPTURE = 1
    var editFriendObject: BEFriend? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val errorMessage = "No application found to handle action!"
        if(intent.extras != null) {
            val b = intent.extras!!

            val editId = b.getInt("editFriendId")
            if(editId != null && editId > 0) {
                isEditMode = true
                editFriendId = editId
            }
        }

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_edit_friend)

        friendsList = FriendService.get()

        DeleteFriendButton.visibility = View.GONE
        ActionsBar.visibility = View.GONE

        if(isEditMode) {
            val getOneObserver = Observer<BEFriend>{ friend ->
                if(isEditMode) {
                    editFriendObject = friend
                    val img = friend.imageUrl
                    if(img.isNotEmpty())
                        FriendImage.setImageBitmap(StringToBitMap(img))

                    FriendName.setText(friend.name)
                    FriendPhone.setText(friend.phone)
                    FriendEmail.setText(friend.email)
                    IsFavorite.isChecked = friend.isFavorite

                    DeleteFriendButton.visibility = View.VISIBLE
                    ActionsBar.visibility = View.VISIBLE
                }
            }

            friendsList.getFriendById(editFriendId).observe(this, getOneObserver)
        }

        //Handler for Back Button
        GoBackButton.setOnClickListener {
            endEditFriendActivity()
        }

        //Handler for Take Photo Button
        TakeImageButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        //Handler for Delete Friend Button
        DeleteFriendButton.setOnClickListener {
            isEditMode = false

            friendsList.deleteFriend(editFriendId)

            Toast.makeText(
                this,
                "Friend was deleted",
                Toast.LENGTH_SHORT
            ).show()

            Handler(Looper.getMainLooper()).postDelayed({
                endEditFriendActivity()
            }, 1500)
        }

        //Handler for Save Friend Button
        SaveFriendButton.setOnClickListener {
            val newName = FriendName.text.toString()
            val newPhone = FriendPhone.text.toString()
            val newEmail = FriendEmail.text.toString()
            var newImage = ""
            val newIsFavorite = IsFavorite.isChecked

            if(newName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and/or phone cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                newImage += BitMapToString(FriendImage.drawable.toBitmap())
                if(!isEditMode) {
                    val newFriend = friendsList.addFriend(newName, newPhone, newEmail, newImage, newIsFavorite)
                    Toast.makeText(
                        this,
                        "Friend was saved",
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        endEditFriendActivity()
                    }, 1500)
                } else {
                    val newObj = BEFriend(editFriendId, newName, newPhone, newEmail, newImage, newIsFavorite)
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



        //Handler for Call Friend Button
        FriendCallButton.setOnClickListener {
            val phoneNumber = editFriendObject?.phone
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
            val phoneNumber = editFriendObject?.phone
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
            val email = editFriendObject?.email
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            FriendImage.setImageBitmap(imageBitmap)
        }
    }

    private fun endEditFriendActivity() {
        finish()
    }

    private fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val arr: ByteArray = baos.toByteArray()
        return Base64.encodeToString(arr, Base64.DEFAULT)
    }

    private fun StringToBitMap(image: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}