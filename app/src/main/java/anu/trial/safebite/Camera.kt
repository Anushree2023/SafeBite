package anu.trial.safebite

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

class Camera : AppCompatActivity() {
    private lateinit var captureImageButton: Button
    private lateinit var capturedImageView: ImageView
//    private lateinit var predictionTextView: TextView
    lateinit var back:Button
     lateinit var out:Button

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_CAPTURE_REQUEST_CODE = 101
    private var capturedImageBitmap: Bitmap? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera3)

         back=findViewById(R.id.backbtn)
        captureImageButton = findViewById(R.id.captureImageButton)
        capturedImageView = findViewById(R.id.capturedImageView)
        out=findViewById(R.id.ScanOutBtn)
//        predictionTextView = findViewById(R.id.predictionTextView)

        back.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        out.setOnClickListener{
            val intent2=Intent(this,Output::class.java)
            startActivity(intent2)
        }

        captureImageButton.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }


    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
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

    @Deprecated("Deprecated in Java") // Avoid lint warning for deprecated method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                capturedImageBitmap = it
                capturedImageView.setImageBitmap(it)
                capturedImageView.visibility = View.VISIBLE

//                sendImageToColabAPI(it)
            }
        }
    }

//    private fun sendImageToColabAPI(bitmap: Bitmap) {
//        predictionTextView.text = "Predicting..."
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val base64Image = convertBitmapToBase64(bitmap)
//                val jsonRequestBody = JSONObject()
//                jsonRequestBody.put("image", base64Image)
//
//                val apiService = createApiService() // Create API service instance
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
//                    Toast.makeText(this@Camera, "API Error: ${e.message}", Toast.LENGTH_LONG).show()
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
//            val confidence = jsonResponse.optDouble("confidence", -1.0) // Handle missing confidence
//
//            val confidenceText = if (confidence >= 0) {
//                "\nConfidence: ${String.format("%.2f", confidence)}"
//            } else {
//                ""
//            }
//
//            predictionTextView.text = "Prediction: $prediction$confidenceText"
//
//        } catch (e: Exception) {
//            predictionTextView.text = "Error parsing API response: ${e.message}\nRaw Response: $apiResponse"
//            Log.e("JSON_ERROR", "Error parsing JSON response: ", e)
//            Toast.makeText(this@Camera, "Error parsing API response", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Retrofit API Service (Placeholder)
//    private fun createApiService(): ApiService {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://your-api-url.com/") // Replace with actual API URL
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .build()
//
//        return retrofit.create(ApiService::class.java)
//    }
}

// API Service Interface
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("predict")
    suspend fun predictImage(@Body requestBody: String): String
}


//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//class Camera : AppCompatActivity() {
//    lateinit var captureImageButton: Button
//    lateinit var capturedImageView: ImageView
//    lateinit var predictionTextView: TextView
//    lateinit var scanBtn: Button
//
//    val CAMERA_PERMISSION_REQUEST_CODE = 100
//    val IMAGE_CAPTURE_REQUEST_CODE = 101
//    var capturedImageBitmap: Bitmap? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_camera3)
//
//        captureImageButton = findViewById(R.id.captureImageButton)
//        capturedImageView = findViewById(R.id.capturedImageView)
////        predictionTextView = findViewById(R.id.predictionTextView)
//
//    }
//     fun checkCameraPermissionAndOpenCamera() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_REQUEST_CODE)
//        } else {
//            openCameraIntent()
//        }
//    }
//}