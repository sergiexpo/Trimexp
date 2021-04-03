package com.neoland.trimexp.users.favourites

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.neoland.trimexp.databinding.FragmentFavouriteuserslistBinding
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteUsersListFragment : Fragment(), FavouriteUsersListAdapterInterface {

    private lateinit var binding: FragmentFavouriteuserslistBinding
    private lateinit var model : FavouriteUsersListFragmentViewModel
    private lateinit var adapter: FavouriteUsersListAdapter
    private var userId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(FavouriteUsersListFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouriteuserslistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch(Dispatchers.Main) {
            model.loadPreferences("TAG_EMAIL")?.let{ email ->
                if (email.isNotEmpty()) {
                    val user = model.getUser(email)
                    userId = user.userId
                    userId?.let{showExperiencesUserlist(it)}
                } else {
                    model.loadPreferences("TAG_EMAIL_TEMPORAL")?.let { email_temp ->
                        if (email_temp.isNotEmpty()) {
                            val user = model.getUser(email_temp)
                             userId = user.userId
                            userId?.let{showExperiencesUserlist(it)}
                        } else {
                            Toast.makeText(binding.root.context, "Please, log in the app", Toast.LENGTH_LONG).show()
                            //Mejor finalizar el fragment y reedirigir
                        }
                    }
                }
            }
        }


    }


    private fun createRecyclerView(userId: Int) {

        adapter = FavouriteUsersListAdapter(this, userId)
        binding.recyclerViewLisFavouriteUserr.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerViewLisFavouriteUserr.adapter = adapter
    }

    private fun observeModelUsers(){
        lifecycleScope.launch {
            model.usersFavorites.observe(viewLifecycleOwner) {
                adapter.updateFavouriteUsers(it)
            }
        }
    }




    fun getUsersFavouriteList(userId: Int) {
        model.getUsersFavourites(userId)
    }

    fun showExperiencesUserlist(userId: Int) {
        getUsersFavouriteList(userId)
        createRecyclerView(userId)
        observeModelUsers()
    }

    fun searchUsersInFavouriteList(text: String){
        model.filterUsersInFavoriteList(text)
    }

    fun reloadFavouriteUsersList(){
        userId?.let {
            showExperiencesUserlist(it)
        }
    }


    override fun onItemClick(userFavorite: User) {
        val builder = AlertDialog.Builder(binding.root.context)

        builder.setMessage("User will be removed from your favorites. Are you sure?")

        builder.setPositiveButton("Yes"){ dialog, id ->
            userId?.let {
                model.deleteFavorite(it, userFavorite.userId)
            }

        }
        builder.setNegativeButton("Cancel"){ dialog, id ->
        }
        builder.create()
        builder.show()
    }


}