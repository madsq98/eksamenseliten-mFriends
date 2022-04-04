package easv.oe.mfriends

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import easv.oe.mfriends.data.BEFriend
import easv.oe.mfriends.data.FriendService
import kotlinx.android.synthetic.main.activity_friendlist.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var friendsList : FriendService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_friendlist)

        FriendService.initialize(this)

        setListFriendsAdapter()

        AddFriendButton.setOnClickListener {
            val newBundle = Bundle()

            startEditFriendActivity(newBundle)
        }
    }

    private fun startEditFriendActivity(b: Bundle) {
        val newIntent = Intent(this, EditFriendActivity::class.java)
        newIntent.putExtras(b)
        startActivity(newIntent)
    }

    private fun setListFriendsAdapter() {
        friendsList = FriendService.get()
        val getAllObserver = Observer<List<BEFriend>>{ friends ->
            val adapter = FriendAdapter(this, friends.toTypedArray())
            lvFriends.adapter = adapter
        }

        friendsList.getAll().observe(this, getAllObserver)
        lvFriends.setOnItemClickListener { _,_,pos, _ -> onFriendClick(pos) }
    }

    private fun onFriendClick(position: Int) {
        val clickedFriend = lvFriends.getItemAtPosition(position) as BEFriend
        val newBundle = Bundle()
        newBundle.putInt("editFriendId", clickedFriend.id)

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