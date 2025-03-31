package anu.trial.safebite

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
//import com.google.firebase.firestore.FirebaseFirestore

class Camera : AppCompatActivity() {
    private lateinit var captureImageButton: Button
    private lateinit var capturedImageView: ImageView
    private lateinit var scanOutButton: Button
    private lateinit var backButton: Button
    private lateinit var resp: String

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_CAPTURE_REQUEST_CODE = 101
    private var capturedImageBitmap: Bitmap? = null

    private val API_KEY = "AIzaSyANN1Vz-j6pls-Qg66RXUGo57BdraWJtUk"

//    private val db = FirebaseFirestore.getInstance()
//    private var userPreferences: Map<String, String> = emptyMap()
    private var isPersonalized: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera3)

        backButton = findViewById(R.id.backbtn)
        captureImageButton = findViewById(R.id.captureImageButton)
        capturedImageView = findViewById(R.id.capturedImageView)
        scanOutButton = findViewById(R.id.ScanOutBtn)
        isPersonalized = intent.getBooleanExtra("isPersonalized", false)

//        if (isPersonalized) {
//            fetchUserPreferences()
//        } else {
//            Log.d("CameraActivity", "Normal Scan - No User Preferences Needed")
//        }


        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        scanOutButton.setOnClickListener {
            capturedImageBitmap?.let { bitmap ->
                CoroutineScope(Dispatchers.Main).launch {
                    val responseText = temp(bitmap) // Wait for AI response
                    val intent = Intent(this@Camera, Output::class.java)
                    intent.putExtra("CURRRESPONSE", responseText)
                    startActivity(intent) // Now we start activity with correct response
                }
            } ?: Toast.makeText(this, "Capture an image first!", Toast.LENGTH_SHORT).show()
        }


        captureImageButton.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }
    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openCameraIntent()
        }
    }


    private fun openCameraIntent() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Save the image to a file instead of using a thumbnail
        val imageFile = File(getExternalFilesDir(null), "captured_image.jpg")
        val imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)

        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri) // Save full-resolution image
        startActivityForResult(captureIntent, IMAGE_CAPTURE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageFile = File(getExternalFilesDir(null), "captured_image.jpg")
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                capturedImageBitmap = bitmap
                capturedImageView.setImageBitmap(bitmap)
                capturedImageView.visibility = View.INVISIBLE
            } else {
                Toast.makeText(this, "Failed to capture image!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun temp(bitmap: Bitmap):String {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = API_KEY
            )

            val inputContent = content {
                image(bitmap)
                text("Extract text from this image and give harmful ingredients in the image also specify sources of the harmful ingredients and there severity.")
            }

            val response = generativeModel.generateContent(inputContent)
//
            val extractedText = response.text ?: "No text found"
            return extractedText;
            // Start ResultActivity and pass the extracted tex

        } catch (e: Exception) {
            Log.e("API_ERROR", "Error in Gemini API call", e)
            runOnUiThread {
                Toast.makeText(this, "Failed to get response from API", Toast.LENGTH_LONG).show()
            }
        }
        return "unable to fetch result";
    }

}




//    private suspend fun temp(bitmap: Bitmap): String {
//        try {
//            val generativeModel = GenerativeModel(
//                modelName = "gemini-1.5-flash",
//                apiKey = API_KEY
//            )
//
//            val inputContent = content {
//                image(bitmap)
//
//                // Check if userPreferences exist (i.e., Personalized Scan is active)
//                val promptText = if (userPreferences.isNotEmpty()) {
//                    """
//                User Preferences:
//                Age: ${userPreferences["age"]}
//                Gender: ${userPreferences["gender"]}
//                Allergies: ${userPreferences["allergies"]}
//                Avoid: ${userPreferences["avoid"]}
//
//                Extract text from this image and classify the ingredients based on the user's preferences.
//                Identify harmful ingredients, specify their sources, and mention their severity.
//                """.trimIndent()
//                } else {
//                    "Extract text from this image and give harmful ingredients in the image. Also, specify sources of the harmful ingredients and their severity."
//                }
//
//                text(promptText)
//            }
//
//            val response = generativeModel.generateContent(inputContent)
//            return response.text ?: "No text found"
//
//        } catch (e: Exception) {
//            Log.e("API_ERROR", "Error in Gemini API call", e)
//            runOnUiThread {
//                Toast.makeText(this, "Failed to get response from API", Toast.LENGTH_LONG).show()
//            }
//        }
//        return "Unable to fetch result"
//    }
