package easv.oe.mfriends

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import easv.oe.mfriends.model.BEFriend
import easv.oe.mfriends.model.Friends
import kotlinx.android.synthetic.main.activity_friendlist.*
import java.io.Console

class MainActivity : AppCompatActivity() {
    var friendsList = Friends()

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                friendsList = data.extras!!.getSerializable("friendList") as Friends
            }
            setListFriendsAdapter()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_friendlist)

        setListFriendsAdapter()

        AddFriendButton.setOnClickListener {
            val newBundle = Bundle()
            newBundle.putSerializable("friendList", friendsList)

            startEditFriendActivity(newBundle)
        }
    }

    private fun startEditFriendActivity(b: Bundle) {
        val newIntent = Intent(this, EditFriendActivity::class.java)
        newIntent.putExtras(b)
        resultLauncher.launch(newIntent)
    }

    private fun setListFriendsAdapter() {
        val adapter = FriendAdapter(this, friendsList.getAll())
        lvFriends.adapter = adapter
        lvFriends.setOnItemClickListener { _,_,pos, _ -> onFriendClick(pos) }
    }

    private fun onFriendClick(position: Int) {
        println(position)
        val id = friendsList.getFriendByIndex(position).id
        val newBundle = Bundle()
        newBundle.putInt("editFriendId", id)
        newBundle.putSerializable("friendList", friendsList)

        startEditFriendActivity(newBundle)
    }

    internal class FriendAdapter(context: Context,
                                 private val friends: Array<BEFriend>
    ) : ArrayAdapter<BEFriend>(context, 0, friends)
    {
        // these colors are used to toogle the background of the list items.
        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.cell_extended, null)

            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val f = friends[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            val phoneView = resView.findViewById<TextView>(R.id.tvPhoneExt)
            val favoriteView = resView.findViewById<ImageView>(R.id.imgFavoriteExt)
            nameView.text = f.name
            phoneView.text = f.phone

            favoriteView.setImageResource(if (f.isFavorite) R.drawable.ok else R.drawable.notok)

            return resView
        }
    }
}