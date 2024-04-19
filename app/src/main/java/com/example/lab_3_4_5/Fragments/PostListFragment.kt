package com.example.lab_3_4_5.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3_4_5.Adapters.PostAdapter
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.R
import com.example.lab_3_4_5.databinding.FragmentPostListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentPostListBinding
    private val adapter = PostAdapter()
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("Posts")

    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentPostListBinding.inflate(layoutInflater)
    }

    private fun fetchPostsData() {
        firebaseRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clearPosts()

                for (postSnap in snapshot.children) {
                    val post = postSnap.getValue(Post::class.java)!!
                    adapter.addPost(post)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("post_list", "Failed to read value.", error.toException())
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_list, container, false)

        fetchPostsData()

        val rcv = view.findViewById<RecyclerView>(R.id.PostsRecyclerView)
        rcv?.layoutManager = LinearLayoutManager(context)
        rcv?.adapter = adapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}