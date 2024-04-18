package com.example.lab_3_4_5.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_3_4_5.Adapters.PostAdapter
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val adapter: PostAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_create, container, false)

        val createPostBtn = view.findViewById<Button>(R.id.create_post_btn)
        val postTitle = view.findViewById<TextView>(R.id.textViewTitleText).text
        val postDescription = view.findViewById<TextView>(R.id.textViewDescriptionText).text

        createPostBtn.setOnClickListener {
            if (postTitle.toString().equals("") || postDescription.toString().equals("")) {
                Toast.makeText(context, "Одно из полей пустое!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            adapter.addPost(Post(
                adapter.itemCount + 1,
                postTitle.toString(),
                postDescription.toString(),
                0,
                false
            ))
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}