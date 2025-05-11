package anu.trial.safebite

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
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
import kotlinx.coroutines.withContext
import java.io.File

class Camera : AppCompatActivity() {
    private lateinit var captureImageButton: ImageButton
    private lateinit var capturedImageView: ImageView
    private lateinit var scanOutButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar


    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_CAPTURE_REQUEST_CODE = 101
    private var capturedImageBitmap: Bitmap? = null

    private val API_KEY = "AIzaSyANN1Vz-j6pls-Qg66RXUGo57BdraWJtUk"

    private var isPersonalized: Boolean = false
    private var userPreferences: String = ""
    lateinit var person:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera3)

        person = findViewById(R.id.PersonalizeBtn)
        backButton = findViewById(R.id.backbtn)
        captureImageButton = findViewById(R.id.captureImageButton)
        capturedImageView = findViewById(R.id.capturedImageView)
        scanOutButton = findViewById(R.id.ScanOutBtn)
        person.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("isPersonalized", false)
            startActivity(intent)
        }

        // Get intent data
        isPersonalized = intent.getBooleanExtra("isPersonalized", false)

        if (isPersonalized) {
            val age = intent.getStringExtra("age") ?: "Not specified"
            val gender = intent.getStringExtra("gender") ?: "Not specified"
            val allergies = intent.getStringExtra("allergies") ?: "None"
            val avoidList = intent.getStringExtra("avoid") ?: "None"

            userPreferences = "User Preferences:\n- Age: $age\n- Gender: $gender\n- Allergies: $allergies\n- Avoid List: $avoidList"
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        scanOutButton.setOnClickListener {
            capturedImageBitmap?.let { bitmap ->
                // Show loading dialog
                val loadingDialog = Dialog(this)
                loadingDialog.setContentView(R.layout.loadingscreen)
                loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                loadingDialog.setCancelable(false) // Prevent user from closing while loading
                loadingDialog.show()

                // Start background task
                CoroutineScope(Dispatchers.IO).launch {
                    val responseText = analyzeImage(bitmap)

                    // Switch to Main thread to update UI
                    withContext(Dispatchers.Main) {
                        loadingDialog.dismiss() // Dismiss dialog when processing is done

                        val intent = Intent(this@Camera, Output::class.java)
                        intent.putExtra("CURRRESPONSE", responseText)
                        startActivity(intent)
                    }
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
        val imageFile = File(getExternalFilesDir(null), "captured_image.jpg")
        val imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
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
                capturedImageView.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Failed to capture image!", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private suspend fun analyzeImage(bitmap: Bitmap): String {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = API_KEY
            )

            val inputContent = content {
                image(bitmap)
                if (isPersonalized) {
                    text("Extract text from this image and list harmful ingredients  Consider these user preferences:\n" +
                            "$userPreferences and1.  Please list all ingredients from the provided list that are considered potentially harmful for consumption \n" +
                            "2.  For each harmful ingredient identified, assign a severity index on a scale of 1 to 5, where:\n" +
                            "     1 = Low severity (potential mild side effects)\n" +
                            "    5 = High severity (potential significant health risks)\n" +
                            "     Provide a brief explanation of why each ingredient is considered harmful.\n" +
                            "3.  Also classify the overall safety of app into SAFE , MODERATE , AVOID\n" +
                            "\n" +
                            "Output the results in the following format:\n" +
                            "\n" +
                            "Harmful Ingredients:\n" +
                            " Ingredient: [Ingredient Name]\n" +
                            "  Severity Index: [Severity Index]" +
                            " (Explanation: Brief Explanation)\n" +
                            "\n" +
                            " Safe Index:[classification into safe , moderate ,avoid\n"+
                            "Present output in ordered list with endline spaces keep good presentation and don't keep an opening and closing statement in output  ")
                } else {
                    text("Extract text from this image and list harmful ingredients  1.  Please list all ingredients from the provided list that are considered potentially harmful for consumption \n" +
                            "2.  For each harmful ingredient identified, assign a severity index on a scale of 1 to 5, where:\n" +
                            "     1 = Low severity (potential mild side effects)\n" +
                            "    5 = High severity (potential significant health risks)\n" +
                            "     Provide a brief explanation of why each ingredient is considered harmful.\n" +
                            "3.  Also classify the overall safety of app into SAFE , MODERATE , AVOID\n" +
                            "\n" +
                            "Output the results in the following format:\n" +
                            "\n" +
                            "Harmful Ingredients:\n" +
                            " Ingredient: [Ingredient Name]\n" +
                            "  Severity Index: [Severity Index]" +
                            " (Explanation: Brief Explanation)\n" +
                            "\n" +
                            " Safe Index:[classification into safe , moderate ,avoid\n" +
                            "Present output in ordered list with endline spaces keep good presentation and don't keep an opening and closing statement in output " )

                }
            }

            val response = generativeModel.generateContent(inputContent)

            val rawText = response.text ?: "No text found"

            // Remove unwanted "**" symbols
            val cleanedText = rawText.replace("**", "")

            return cleanedText

        } catch (e: Exception) {
            Log.e("API_ERROR", "Error in Gemini API call", e)
            runOnUiThread {
                Toast.makeText(this, "Failed to get response from API", Toast.LENGTH_LONG).show()
            }
        }
        return "Unable to fetch result ."
    }
}




