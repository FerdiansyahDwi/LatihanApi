package com.example.latihanapi

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.latihanapi.databinding.ActivityCreateCatatanBinding
import com.example.latihanapi.entities.Catatan
import kotlinx.coroutines.launch

class CreateCatatanActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateCatatanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEvents()
    }

    fun setupEvents() {
        binding.tombolSimpan.setOnClickListener {
            val judul = binding.inputJudul.text.toString()
            val isi = binding.inputIsi.text.toString()

            if (judul.isEmpty() || isi.isEmpty()) {
                displayMessage("Judul dan isi Catatan harus diisi")
                return@setOnClickListener
            }

            val payload = Catatan(
                judul = judul,
                isi = isi,
                id = null
            )

            lifecycleScope.launch {
                val response = RetrofitClient.catatanRepository.createCatatan(payload)
                if (response.isSuccessful) {
                    displayMessage("Catatan berhasil dibuat")

                    val intent = Intent(this@CreateCatatanActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    displayMessage("Gagal : ${response.message()}")
                }
            }
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}