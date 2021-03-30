package com.neoland.trimexp.users.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.databinding.ActivityRegisterUserBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterUserActivity: AppCompatActivity() {

    lateinit var binding: ActivityRegisterUserBinding
    lateinit var model: RegisterUserActivityViewModel
    private var userPhoto : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(RegisterUserActivityViewModel::class.java)


        binding.imageViewIconBack.setOnClickListener {
            goBackRegisterActivity()
        }

        binding.buttonExplorer.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            resultGallery.launch(intent)
        }

        binding.buttonCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultCamera.launch(intent)
        }

        binding.buttonAddUser.setOnClickListener {
            lifecycleScope.launch{
                model.insertUser(
                        binding.editTextRegisterUserNAme.text.toString(),
                        binding.editTextRegisterUserDescription.text.toString(),
                        binding.editTextRegisterUserEmail.text.toString(),
                        binding.editTextRegisterUserPassword.text.toString(),
                        userPhoto
                )
                Toast.makeText(binding.root.context, "User has been added", Toast.LENGTH_LONG).show()
                delay(2000)
                finish()
            }
        }

    }


    private fun goBackRegisterActivity(){
        onBackPressed()
    }

    private val resultGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let { uri ->
                contentResolver?.let { contentResolver ->
                    userPhoto = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap( this.contentResolver, uri)
                    }
                }
            }
            binding.imageViewRegisterUserPhoto.setImageBitmap(userPhoto) //  handle chosen image
        }

    }

    private val resultCamera  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            userPhoto = result.data?.extras?.get("data") as Bitmap
            binding.imageViewRegisterUserPhoto.setImageBitmap(userPhoto)
        }
    }


}