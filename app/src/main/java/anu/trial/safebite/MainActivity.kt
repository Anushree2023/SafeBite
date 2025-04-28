package anu.trial.safebite // Replace with your actual package name

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.ByteArrayOutputStream
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {


    private lateinit var scanBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanBtn = findViewById(R.id.getstartedbtn)

        val lottieAnimation = findViewById<LottieAnimationView>(R.id.lottieAnimation)

        // Play animation
        lottieAnimation.playAnimation()

        // Stop animation
        // lottieAnimation.cancelAnimation()

        scanBtn.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)


        }

    }
}

