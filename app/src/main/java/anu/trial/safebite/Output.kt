package anu.trial.safebite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Output : AppCompatActivity() {

    private lateinit var extractedTextView: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output)

        extractedTextView = findViewById(R.id.extractedTextView)
        backButton = findViewById(R.id.backButton)

        // Get the extracted text from Intent
        val extractedText = intent.getStringExtra("CURRRESPONSE") ?: "No text received"

        // Display the extracted text
        extractedTextView.text = extractedText

        // Back button to return to Camera Activity
        backButton.setOnClickListener {
            startActivity(Intent(this, Camera::class.java))
            finish() // Close ResultActivity
        }
    }
}




//package anu.trial.safebite
//
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class Output : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_output)
//
//        val resultTextView = findViewById<TextView>(R.id.resultTextView)
//        val result = intent.getStringExtra("API_RESULT") ?: "No result available"
//        Toast.makeText(this, "${result}", Toast.LENGTH_LONG).show()
//        //resultTextView.text = result
//
//    }
//}
