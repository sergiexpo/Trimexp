package com.neoland.trimexp.users.favourites

import android.os.Bundle
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

        binding.imageViewIconBack.setOnClickListener {
            goBackManageExperienceActivity()
        }


        initFragment(fragment)


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