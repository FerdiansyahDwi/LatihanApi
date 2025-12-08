package com.example.latihanapi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanapi.adapter.CatatanAdapter
import com.example.latihanapi.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CatatanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupEvents()
    }

    fun setupEvents() {
        adapter = CatatanAdapter(mutableListOf())
        binding.container.adapter = adapter
        binding.container.layoutManager = LinearLayoutManager(this)

//        binding.btnTambah.setOnClickListener {
//            val intent = Intent(this,CreateCatatanActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        lifecycleScope.launch {
            val response = RetrofitClient.catatanRepository.getCatatan()
            if (!response.isSuccessful) {
                displayMessage("Gagal : ${response.message()}")
                return@launch
            }

            val data = response.body()
            if (data == null) {
                displayMessage("Tidak ada data")
                return@launch
            }

            adapter.updateDataset(data)
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}