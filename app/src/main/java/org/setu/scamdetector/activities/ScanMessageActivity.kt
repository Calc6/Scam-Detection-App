package org.setu.scamdetector.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.setu.scamdetector.R
import org.setu.scamdetector.databinding.ActivityScanMessageBinding
import org.setu.scamdetector.main.ScamDetectorApp
import org.setu.scamdetector.models.ScanResultModel
import timber.log.Timber

class ScanMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanMessageBinding
    private lateinit var app: ScamDetectorApp

    private var scan = ScanResultModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as ScamDetectorApp

        Timber.i("ScanMessageActivity started")

        binding.analyzeButton.setOnClickListener {
            val message = binding.messageInput.text.toString()

            if (message.isNotEmpty()) {
                val result = org.setu.scamdetector.models.ScamDetector.analyzeMessage(message)

                if (result.isScam) {
                    binding.resultLabel.text = "Likely Scam (${result.score}%)"
                } else {
                    binding.resultLabel.text = "Likely Safe (${result.score}%)"
                }
            } else {
                Snackbar
                    .make(it, "Please enter a message to scan", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_scam, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_cancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}