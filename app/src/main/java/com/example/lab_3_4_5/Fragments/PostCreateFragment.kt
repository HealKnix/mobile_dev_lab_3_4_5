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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lab_3_4_5.Adapters.PostAdapter
import com.example.lab_3_4_5.Models.Place
import com.example.lab_3_4_5.Models.Post
import com.example.lab_3_4_5.Models.User
import com.example.lab_3_4_5.R
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date
import kotlin.random.Random

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
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("Posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun findNotEmptyArea(areas: List<Place>): Place {
        var area = areas[Random.nextInt(0, areas.size-1)]
        if (area.areas.isEmpty()) {
            area = findNotEmptyArea(areas)
        }
        return area
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_create, container, false)

        val createPostBtn = view.findViewById<Button>(R.id.create_post_btn)
        val postTitle = view.findViewById<TextView>(R.id.textViewTitleText)
        val postDescription = view.findViewById<TextView>(R.id.textViewDescriptionText)

        createPostBtn.setOnClickListener {
            if (postTitle.text.isEmpty())
                postTitle.error = "Пустое поле!"
            if (postDescription.text.isEmpty())
                postDescription.error = "Пустое поле!"

            if (postTitle.text.isEmpty() || postDescription.text.isEmpty())
                return@setOnClickListener

            var postId = 0

            if (adapter.getPosts().isNotEmpty())
                postId = adapter.getPosts().last().id + 1

            val url = "https://api.hh.ru/areas"
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                url,
                { result ->
                    val gson = Gson()
                    val jsonInput = result.trimIndent()
                    val type = object : TypeToken<List<Place>>() {}.type

                    val areas: List<Place> = gson.fromJson(jsonInput, type)
                    val area = findNotEmptyArea(areas)
                    val randomCityId = Random.nextInt(0, areas.size - 1)
                    val cities = area.areas
                    val cityName = cities[randomCityId].name

                    val newPost = Post(
                        postId,
                        postTitle.text.toString(),
                        postDescription.text.toString(),
                        User.getCurrentUser()?.id ?: -1,
                        mutableListOf<Int>(),
                        0,
                        Date().toLocaleString(),
                        Date().toLocaleString(),
                        cityName
                    )

                    firebaseRef.child(postId.toString()).setValue(newPost)
                        .addOnCompleteListener {
                            Toast.makeText(context, "Пост '${postTitle.text}' создан", Toast.LENGTH_SHORT).show()
                            replaceFragment(PostListFragment())
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Ошибка создания поста", Toast.LENGTH_SHORT).show()
                        }
                },
                { error ->
                    Toast.makeText(context, "Ошибка запроса к API", Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
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