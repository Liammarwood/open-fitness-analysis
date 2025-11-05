package com.openfitness.analysis

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.openfitness.analysis.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var healthConnectClient: HealthConnectClient
    
    // Define the permissions we need
    private val permissions = setOf(
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(androidx.health.connect.client.records.StepsRecord::class),
        HealthPermission.getReadPermission(androidx.health.connect.client.records.DistanceRecord::class),
        HealthPermission.getReadPermission(androidx.health.connect.client.records.ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(androidx.health.connect.client.records.TotalCaloriesBurnedRecord::class)
    )
    
    // Permission request launcher using Health Connect specific contract
    private val requestPermissions = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(permissions)) {
            showStatus(getString(R.string.permissions_granted))
        } else {
            showStatus(getString(R.string.permissions_denied))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize Health Connect client
        if (HealthConnectClient.getSdkStatus(this) == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(this)
        } else {
            showStatus("Health Connect is not available on this device")
            return
        }
        
        setupButtons()
    }
    
    private fun setupButtons() {
        binding.checkPermissionsButton.setOnClickListener {
            checkPermissions()
        }
        
        binding.requestPermissionsButton.setOnClickListener {
            requestPermissionsFromUser()
        }
        
        binding.readActivitiesButton.setOnClickListener {
            readActivities()
        }
    }
    
    private fun checkPermissions() {
        lifecycleScope.launch {
            try {
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                val message = if (granted.containsAll(permissions)) {
                    "All required permissions are granted"
                } else {
                    "Missing permissions: ${permissions.minus(granted)}"
                }
                showStatus(message)
            } catch (e: Exception) {
                showError(e)
            }
        }
    }
    
    private fun requestPermissionsFromUser() {
        lifecycleScope.launch {
            try {
                requestPermissions.launch(permissions)
            } catch (e: Exception) {
                showError(e)
            }
        }
    }
    
    private fun readActivities() {
        lifecycleScope.launch {
            try {
                showStatus(getString(R.string.loading))
                
                // Read exercise sessions from the last 30 days
                val endTime = Instant.now()
                val startTime = endTime.minusSeconds(30 * 24 * 60 * 60) // 30 days ago
                
                val request = ReadRecordsRequest(
                    recordType = ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
                
                val response = healthConnectClient.readRecords(request)
                
                if (response.records.isEmpty()) {
                    binding.activitiesText.text = getString(R.string.no_activities)
                    showStatus("No activities found in the last 30 days")
                } else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault())
                    
                    val activitiesText = buildString {
                        appendLine("Found ${response.records.size} activities:\n")
                        response.records.forEachIndexed { index, record ->
                            val durationMinutes = java.time.Duration.between(
                                record.startTime,
                                record.endTime
                            ).toMinutes()
                            
                            appendLine("${index + 1}. ${record.exerciseType.name}")
                            appendLine("   Start: ${formatter.format(record.startTime)}")
                            appendLine("   End: ${formatter.format(record.endTime)}")
                            appendLine("   Duration: $durationMinutes minutes")
                            if (record.title != null) {
                                appendLine("   Title: ${record.title}")
                            }
                            appendLine()
                        }
                    }
                    binding.activitiesText.text = activitiesText
                    showStatus("Successfully loaded ${response.records.size} activities")
                }
            } catch (e: Exception) {
                showError(e)
            }
        }
    }
    
    private fun showStatus(message: String) {
        binding.statusText.text = message
    }
    
    private fun showError(e: Exception) {
        val message = getString(R.string.error_health_connect, e.message)
        showStatus(message)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
