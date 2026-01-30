package org.setu.scamdetector.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.setu.scamdetector.R
import org.setu.scamdetector.databinding.ActivityScanMessageBinding
import org.setu.scamdetector.main.ScamDetectorApp
import org.setu.scamdetector.models.ScamDetector
import org.setu.scamdetector.models.ScanResultModel
import timber.log.Timber
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ScanMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanMessageBinding
    private lateinit var app: ScamDetectorApp

    private var isViewMode = false
    private var scan = ScanResultModel()

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                binding.screenshotPreview.setImageURI(uri)
                binding.screenshotPreview.visibility = android.view.View.VISIBLE

                Snackbar.make(binding.root, "Screenshot selected", Snackbar.LENGTH_SHORT).show()
                runOcrFromUri(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadScreenshotButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as ScamDetectorApp

        Timber.i("ScanMessageActivity started")

        val editScam = intent.getParcelableExtra<ScanResultModel>("scam_edit")

        if (editScam != null) {
            isViewMode = true
            scan = editScam

            binding.messageInput.setText(scan.title)
            binding.messageInput.isEnabled = false
            binding.analyzeButton.isEnabled = false
            binding.analyzeButton.text = "Already Analysed"

            if (scan.isScam) {
                binding.resultLabel.text = "Likely Scam (${scan.score}%)"
            } else {
                binding.resultLabel.text = "Likely Safe (${scan.score}%)"
            }

            binding.reasonsLabel.text = scan.description ?: ""
        }

        binding.analyzeButton.setOnClickListener {
            if (isViewMode) return@setOnClickListener
            val message = binding.messageInput.text.toString()

            if (message.isNotEmpty()) {
                val result = ScamDetector.analyzeMessage(message)

                binding.resultLabel.text =
                    if (result.isScam)
                        "Likely Scam (${result.score}%)"
                    else
                        "Likely Safe (${result.score}%)"

                binding.reasonsLabel.text = result.reasons.joinToString(
                    separator = "\n• ",
                    prefix = ""
                )

                scan = ScanResultModel(
                    title = message,
                    score = result.score,
                    isScam = result.isScam,
                    description = binding.reasonsLabel.text.toString()
                )

                app.scams.create(scan)
                setResult(RESULT_OK)
                finish()

            } else {
                Snackbar.make(it, "Please enter a message to scan", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun runOcrFromUri(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val extractedText = cleanOcrText(visionText.text)

                    if (extractedText.isNotEmpty()) {
                        binding.messageInput.setText(extractedText)
                        Snackbar.make(binding.root, "Text extracted from screenshot", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(binding.root, "No text found in image", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Snackbar.make(binding.root, "OCR failed: ${e.message}", Snackbar.LENGTH_LONG).show()
                }

        } catch (e: Exception) {
            Snackbar.make(binding.root, "Could not read image: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun cleanOcrText(raw: String): String {
        return raw
            .replace("\r", "\n")                 // normalise newlines
            .replace(Regex("\n+"), "\n")         // collapse multiple newlines
            .replace(Regex("[ \\t]+"), " ")      // collapse spaces/tabs
            .replace(Regex(" *\n *"), "\n")      // trim spaces around newlines
            .trim()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_scam, menu)
        return true
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