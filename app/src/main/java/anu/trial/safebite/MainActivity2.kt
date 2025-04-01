package anu.trial.safebite

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    private lateinit var ageInput: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var allergyInput: EditText
    private lateinit var avoidInput: EditText
    private lateinit var submitButton: Button
    lateinit var back: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        back = findViewById(R.id.backbtn2)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        ageInput = findViewById(R.id.ageInput)
        genderGroup = findViewById(R.id.genderGroup)
        allergyInput = findViewById(R.id.allergyInput)
        avoidInput = findViewById(R.id.avoidInput)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            saveUserPreferences()
        }
    }

    //
    private fun saveUserPreferences() {
        val age = ageInput.text.toString().trim()
        val allergies = allergyInput.text.toString().trim()
        val avoidList = avoidInput.text.toString().trim()

        // Get selected gender
        val selectedGenderId = genderGroup.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) {
            findViewById<RadioButton>(selectedGenderId).text.toString()
        } else {
            "Not specified"
        }

        if (age.isEmpty()) {
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, Camera::class.java).apply {
            putExtra("isPersonalized", true)
            putExtra("age", age)
            putExtra("gender", gender)
            putExtra("allergies", allergies)
            putExtra("avoid", avoidList)
        }
        startActivity(intent)
    }


}


