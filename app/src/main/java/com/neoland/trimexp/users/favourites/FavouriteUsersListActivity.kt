package com.neoland.trimexp.users.favourites

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.databinding.ActivityUserListFavouriteusersBinding
import com.neoland.trimexp.experiences.manage.ManageExperienceFragment

class FavouriteUsersListActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityUserListFavouriteusersBinding
    private var fragment = FavouriteUsersListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListFavouriteusersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment(fragment)

        binding.imageViewIconBack.setOnClickListener {
            goBackManageExperienceActivity()
        }

        binding.swSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    fragment.searchUsersInFavouriteList(p0)
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    fragment.searchUsersInFavouriteList(p0)
                }
                return true
            }
        })

        binding.imageViewIconReload.setOnClickListener {
            fragment.reloadFavouriteUsersList()
        }



    }

    private fun goBackManageExperienceActivity() {
        onBackPressed()
    }


    private fun initFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayoutUsersFavourites.id, fragment)
        fragmentTransaction.commit()

    }

}