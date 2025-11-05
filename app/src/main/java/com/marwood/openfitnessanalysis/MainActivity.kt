package com.marwood.openfitnessanalysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.marwood.openfitnessanalysis.ui.theme.OpenFitnessAnalysisTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    private lateinit var healthConnectClient: HealthConnectClient
    
    private val permissions = setOf(
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check if Health Connect is available
        val availability = HealthConnectClient.getSdkStatus(this)
        if (availability == HealthConnectClient.SDK_UNAVAILABLE) {
            // Health Connect not available
            setContent {
                OpenFitnessAnalysisTheme {
                    ErrorScreen("Health Connect is not available on this device")
                }
            }
            return
        }
        
        if (availability == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            setContent {
                OpenFitnessAnalysisTheme {
                    ErrorScreen("Please update Health Connect from the Play Store")
                }
            }
            return
        }
        
        healthConnectClient = HealthConnectClient.getOrCreate(this)
        
        setContent {
            OpenFitnessAnalysisTheme {
                HealthConnectScreen()
            }
        }
    }
    
    @Composable
    fun HealthConnectScreen() {
        var permissionsGranted by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var weeklyStats by remember { mutableStateOf<WeeklyStats?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(Unit) {
            checkPermissions { granted ->
                permissionsGranted = granted
                if (granted) {
                    loadWeeklyStats(
                        onSuccess = { stats ->
                            weeklyStats = stats
                            isLoading = false
                        },
                        onError = { error ->
                            errorMessage = error
                            isLoading = false
                        }
                    )
                }
            }
        }
        
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Open Fitness Analysis") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (!permissionsGranted) {
                    PermissionRequestCard(
                        onRequestPermissions = {
                            requestPermissions { granted ->
                                permissionsGranted = granted
                                if (granted) {
                                    isLoading = true
                                    loadWeeklyStats(
                                        onSuccess = { stats ->
                                            weeklyStats = stats
                                            isLoading = false
                                        },
                                        onError = { error ->
                                            errorMessage = error
                                            isLoading = false
                                        }
                                    )
                                }
                            }
                        }
                    )
                } else if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    ErrorCard(errorMessage!!)
                } else if (weeklyStats != null) {
                    WeeklyStatsDisplay(weeklyStats!!)
                }
            }
        }
    }
    
    @Composable
    fun PermissionRequestCard(onRequestPermissions: () -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Health Connect Permissions Required",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This app needs permission to read your exercise data, steps, distance, and calories from Health Connect.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRequestPermissions) {
                    Text("Grant Permissions")
                }
            }
        }
    }
    
    @Composable
    fun WeeklyStatsDisplay(stats: WeeklyStats) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Your Weekly Stats",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                StatsCard(
                    title = "Total Steps",
                    value = stats.totalSteps.toString(),
                    subtitle = "Last 7 days"
                )
            }
            
            item {
                StatsCard(
                    title = "Total Distance",
                    value = "%.2f km".format(stats.totalDistance / 1000),
                    subtitle = "Last 7 days"
                )
            }
            
            item {
                StatsCard(
                    title = "Total Calories",
                    value = "%.0f kcal".format(stats.totalCalories),
                    subtitle = "Last 7 days"
                )
            }
            
            item {
                StatsCard(
                    title = "Workouts",
                    value = stats.workoutCount.toString(),
                    subtitle = "Last 7 days"
                )
            }
            
            if (stats.exercises.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Activities",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                items(stats.exercises) { exercise ->
                    ExerciseCard(exercise)
                }
            }
        }
    }
    
    @Composable
    fun StatsCard(title: String, value: String, subtitle: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
    
    @Composable
    fun ExerciseCard(exercise: ExerciseInfo) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = exercise.type,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.formattedDate,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${exercise.durationMinutes} minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (exercise.title != null) {
                    Text(
                        text = exercise.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
    
    @Composable
    fun ErrorCard(message: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
    
    private fun checkPermissions(callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                callback(granted.containsAll(permissions))
            } catch (e: Exception) {
                callback(false)
            }
        }
    }
    
    private fun requestPermissions(callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                val permissionsContract = PermissionController.createRequestPermissionResultContract()
                val intent = permissionsContract.createIntent(this@MainActivity, permissions)
                startActivity(intent)
                // Note: In a real app, you would use ActivityResultLauncher
                // For simplicity, we'll just check again after a delay
                kotlinx.coroutines.delay(1000)
                checkPermissions(callback)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }
    
    private fun loadWeeklyStats(onSuccess: (WeeklyStats) -> Unit, onError: (String) -> Unit) {
        lifecycleScope.launch {
            try {
                val endTime = Instant.now()
                val startTime = endTime.minus(7, ChronoUnit.DAYS)
                
                // Read exercise sessions
                val exerciseRequest = ReadRecordsRequest(
                    recordType = ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
                val exerciseResponse = healthConnectClient.readRecords(exerciseRequest)
                
                // Aggregate steps
                val stepsRequest = AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
                val stepsResponse = healthConnectClient.aggregate(stepsRequest)
                val totalSteps = stepsResponse[StepsRecord.COUNT_TOTAL] ?: 0L
                
                // Aggregate distance
                val distanceRequest = AggregateRequest(
                    metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
                val distanceResponse = healthConnectClient.aggregate(distanceRequest)
                val totalDistance = distanceResponse[DistanceRecord.DISTANCE_TOTAL]?.inMeters ?: 0.0
                
                // Aggregate calories
                val caloriesRequest = AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
                val caloriesResponse = healthConnectClient.aggregate(caloriesRequest)
                val totalCalories = caloriesResponse[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
                
                // Convert exercises to display format
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                    .withZone(ZoneId.systemDefault())
                
                val exercises = exerciseResponse.records.map { record ->
                    ExerciseInfo(
                        type = record.exerciseType.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        formattedDate = formatter.format(record.startTime),
                        durationMinutes = java.time.Duration.between(
                            record.startTime,
                            record.endTime
                        ).toMinutes(),
                        title = record.title
                    )
                }
                
                val stats = WeeklyStats(
                    totalSteps = totalSteps,
                    totalDistance = totalDistance,
                    totalCalories = totalCalories,
                    workoutCount = exerciseResponse.records.size,
                    exercises = exercises
                )
                
                onSuccess(stats)
            } catch (e: Exception) {
                onError("Failed to load stats: ${e.message}")
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

data class WeeklyStats(
    val totalSteps: Long,
    val totalDistance: Double,
    val totalCalories: Double,
    val workoutCount: Int,
    val exercises: List<ExerciseInfo>
)

data class ExerciseInfo(
    val type: String,
    val formattedDate: String,
    val durationMinutes: Long,
    val title: String?
)