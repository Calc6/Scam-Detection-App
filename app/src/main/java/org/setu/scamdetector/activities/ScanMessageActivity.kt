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
import org.setu.scamdetector.models.ScamDetector
import org.setu.scamdetector.models.ScanResultModel
import timber.log.Timber

class ScanMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanMessageBinding
    private lateinit var app: ScamDetectorApp

    private var scan = ScanResultModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val editScam = intent.getParcelableExtra<ScanResultModel>("scam_edit")
        super.onCreate(savedInstanceState)

        binding = ActivityScanMessageBinding.inflate(layoutInflater)
        if (editScam != null) {
            binding.messageInput.setText(editScam.title)
            binding.messageInput.isEnabled = false
            binding.analyzeButton.isEnabled = false
            binding.analyzeButton.text = "Already Analysed"

            // Show result
            if (editScam.isScam) {
                binding.resultLabel.text = "Likely Scam (${editScam.score}%)"
            } else {
                binding.resultLabel.text = "Likely Safe (${editScam.score}%)"
            }

            binding.reasonsLabel.text = editScam.description ?: ""
            return
        }
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as ScamDetectorApp

        Timber.i("ScanMessageActivity started")

        binding.analyzeButton.setOnClickListener {
            val message = binding.messageInput.text.toString()

            if (message.isNotEmpty()) {
                val result = ScamDetector.analyzeMessage(message)

                if (result.isScam) {
                    binding.resultLabel.text = "Likely Scam (${result.score}%)"
                } else {
                    binding.resultLabel.text = "Likely Safe (${result.score}%)"
                }

                binding.reasonsLabel.text = result.reasons.joinToString(
                    separator = "\n• ",
                    prefix = "• "
                )
                val scanResult = ScanResultModel().apply {
                    title = message
                    score = result.score
                    isScam = result.isScam
                }

                app.scams.create(scanResult)

                setResult(RESULT_OK)
                finish()

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