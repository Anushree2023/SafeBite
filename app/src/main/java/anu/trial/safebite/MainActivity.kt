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

class MainActivity : AppCompatActivity() {


    lateinit var scanBtn: Button

//    val CAMERA_PERMISSION_REQUEST_CODE = 100
//    val IMAGE_CAPTURE_REQUEST_CODE = 101
//    var capturedImageBitmap: Bitmap? = null

    // *** Replace with your ngrok public URL from Colab ***
//    private val API_BASE_URL = "YOUR_NGROK_PUBLIC_URL_HERE" // e.g., "https://xxxx-xx-xx-xx-xx.ngrok-free.app"
//
//
//    interface ApiService {
//        @Headers("Content-Type: application/json")
//        @POST("/predict") // Endpoint defined in your Flask API
//        suspend fun predictImage(@Body requestBody: String): String // Expecting String response
//    }
//
//    private lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanBtn = findViewById(R.id.ScanButtn)
//        captureImageButton = findViewById(R.id.captureImageButton)
//        capturedImageView = findViewById(R.id.capturedImageView)


        scanBtn.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)

        }
    }
}

        // Retrofit setup
//        val retrofit = Retrofit.Builder()
//            .baseUrl(API_BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .build()
//        apiService = retrofit.create(ApiService::class.java)
//
//        captureImageButton.setOnClickListener {
//            checkCameraPermissionAndOpenCamera()
//        }
//    }

//    private fun checkCameraPermissionAndOpenCamera() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_REQUEST_CODE)
//        } else {
//            openCameraIntent()
//        }
//    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCameraIntent()
//            } else {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun openCameraIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
//        } else {
//            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as? Bitmap
//            imageBitmap?.let {
//                capturedImageBitmap = it
//                capturedImageView.setImageBitmap(it)
//                capturedImageView.visibility = View.VISIBLE
//
//                sendImageToColabAPI(it)
//            }
//        }
//    }
//
//    private fun sendImageToColabAPI(bitmap: Bitmap) {
//        predictionTextView.text = "Predicting..."
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val base64Image = convertBitmapToBase64(bitmap)
//                val jsonRequestBody = JSONObject()
//                jsonRequestBody.put("image", base64Image)
//
//                val response = apiService.predictImage(jsonRequestBody.toString())
//
//                launch(Dispatchers.Main) {
//                    handleApiResponse(response)
//                }
//
//            } catch (e: Exception) {
//                launch(Dispatchers.Main) {
//                    predictionTextView.text = "Error: ${e.message}"
//                    Log.e("API_ERROR", "Error calling Colab API: ", e)
//                    Toast.makeText(this@MainActivity, "API Error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    private fun convertBitmapToBase64(bitmap: Bitmap): String {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }
//
//    private fun handleApiResponse(apiResponse: String) {
//        try {
//            val jsonResponse = JSONObject(apiResponse)
//            val prediction = jsonResponse.getString("prediction")
//            val confidence = jsonResponse.getDouble("confidence") // If confidence is in response
//
//            predictionTextView.text = "Prediction: $prediction\nConfidence: ${String.format("%.2f", confidence)}"
//
//        } catch (e: Exception) {
//            predictionTextView.text = "Error parsing API response: ${e.message}\nRaw Response: $apiResponse"
//            Log.e("JSON_ERROR", "Error parsing JSON response: ", e)
//            Log.d("RAW_RESPONSE", apiResponse)
//            Toast.makeText(this@MainActivity, "Error parsing API response", Toast.LENGTH_SHORT).show()
//        }
//    }



//package anu.trial.safebite
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import anu.trial.safebite.R.*
//
//class MainActivity : AppCompatActivity() {
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(layout.activity_main)
//
//        val scanButton: Button = findViewById(id.scanButton)
//        scanButton.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}