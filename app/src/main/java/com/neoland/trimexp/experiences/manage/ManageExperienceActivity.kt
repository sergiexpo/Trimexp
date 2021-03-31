package com.neoland.trimexp.experiences.manage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.databinding.ActivityExperienceUserManageBinding


class ManageExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExperienceUserManageBinding
    private var fragment = ManageExperienceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceUserManageBinding.inflate(layoutInflater)
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
        fragmentTransaction.replace(binding.frameLayoutExperiences.id, fragment)
        fragmentTransaction.commit()

    }

}