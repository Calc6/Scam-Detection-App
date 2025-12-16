package org.setu.scamdetector.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.setu.scamdetector.R
import org.setu.scamdetector.databinding.ActivityScanMessageBinding
import org.setu.scamdetector.main.ScamDetectorApp
import org.setu.scamdetector.models.ScanResultModel
import timber.log.Timber

class ScanMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanMessageBinding
    var scam = ScanResultModel()
    lateinit var app: ScamDetectorApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as ScamDetectorApp

        Timber.i("Scam Activity started...")

        if (intent.hasExtra("scam_edit")) {
            scam = intent.getParcelableExtra("scam_edit",
                                                   ScanResultModel::class.java)!!
            binding.scamTitle.setText(scam.title)
            binding.description.setText(scam.description)
        }

        binding.btnAdd.setOnClickListener {
            scam.title = binding.scamTitle.text.toString()
            scam.description = binding.description.text.toString()
            if (!scam.title.isNullOrEmpty()) {
                app.scams.create(scam.copy())
                Timber.i("add Button Pressed: $scam")
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scan_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}