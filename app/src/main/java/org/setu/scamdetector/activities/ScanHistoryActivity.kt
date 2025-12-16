package org.setu.scamdetector.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.setu.scamdetector.R
import org.setu.scamdetector.adapters.ScanHistoryAdapter
import org.setu.scamdetector.adapters.ScamListener
import org.setu.scamdetector.databinding.ActivityScanHistoryBinding
import org.setu.scamdetector.main.ScamDetectorApp
import org.setu.scamdetector.models.ScanResultModel

class ScanHistoryActivity : AppCompatActivity(), ScamListener {

    lateinit var app: ScamDetectorApp
    private lateinit var binding: ActivityScanHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as ScamDetectorApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ScanHistoryAdapter(app.scams.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scan_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ScanMessageActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.scams.findAll().size)
            }
        }

    override fun onScamClick(scam: ScanResultModel) {
        val launcherIntent = Intent(this, ScanMessageActivity::class.java)
        launcherIntent.putExtra("scam_edit", scam)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.scams.findAll().size)
            }
        }
}