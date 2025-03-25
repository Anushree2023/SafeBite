package anu.trial.safebite

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Camera : AppCompatActivity() {
    private lateinit var captureImageButton: Button
    private lateinit var capturedImageView: ImageView
    private lateinit var scanOutButton: Button
    private lateinit var backButton: Button
    private lateinit var resp: String

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_CAPTURE_REQUEST_CODE = 101
    private var capturedImageBitmap: Bitmap? = null

    private val API_KEY = "AIzaSyANN1Vz-j6pls-Qg66RXUGo57BdraWJtUk" // REPLACE WITH YOUR API KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera3)

        backButton = findViewById(R.id.backbtn)
        captureImageButton = findViewById(R.id.captureImageButton)
        capturedImageView = findViewById(R.id.capturedImageView)
        scanOutButton = findViewById(R.id.ScanOutBtn)
//        resultTextView = findViewById(R.id.resultTextView) // Display extracted text

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        scanOutButton.setOnClickListener {
            resp="alice"
            capturedImageBitmap?.let { bitmap ->
                CoroutineScope(Dispatchers.Main).launch {
                    resp=temp(bitmap)
                }
            } ?: Toast.makeText(this, "Capture an image first!", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,Output::class.java)
            intent.putExtra("CURRRESPONSE",resp)
            startActivity(intent)
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
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                capturedImageBitmap = it
                capturedImageView.setImageBitmap(it)
                capturedImageView.visibility = View.VISIBLE
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
            runOnUiThread {
                Toast.makeText(this, response.text ?: "No text found", Toast.LENGTH_LONG).show()
            }
//        } catch (e: Exception) {
//            Log.e("API_ERROR", "Error in Gemini API call", e)
//            runOnUiThread {
//                Toast.makeText(this, "Failed to get response from API", Toast.LENGTH_SHORT).show()
//            }
//        }
            val extractedText = response.text ?: "No text found"
            return extractedText;
            // Start ResultActivity and pass the extracted tex

        } catch (e: Exception) {
            Log.e("API_ERROR", "Error in Gemini API call", e)
            runOnUiThread {
                Toast.makeText(this, "Failed to get response from API", Toast.LENGTH_SHORT).show()
            }
        }
        return "unable to fetch result";
    }

}
