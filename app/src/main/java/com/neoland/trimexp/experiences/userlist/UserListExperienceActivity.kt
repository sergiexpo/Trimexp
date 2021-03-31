package com.neoland.trimexp.experiences.userlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding
import com.neoland.trimexp.databinding.ActivityUserListExperienceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserListExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListExperienceBinding
    private var fragment = UserListExperiencesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewIconBack.setOnClickListener {
            goBackUserListExperienceActivity()
        }

        lifecycleScope.launch {
            lifecycleScope.async(Dispatchers.IO)  {
                changeFragment(fragment)
            } .await()
            fragment.getExperiencesUserList()
        }



    }

    private fun goBackUserListExperienceActivity(){
        onBackPressed()
    }


    private fun changeFragment(fragment : Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayoutExperiences.id, fragment)
        fragmentTransaction.commit()
    }

}