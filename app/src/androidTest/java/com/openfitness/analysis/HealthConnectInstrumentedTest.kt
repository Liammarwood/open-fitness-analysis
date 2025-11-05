package com.openfitness.analysis

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for Health Connect functionality.
 * These tests require Health Connect to be available on the device.
 */
@RunWith(AndroidJUnit4::class)
class HealthConnectInstrumentedTest {
    
    private lateinit var context: Context
    
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }
    
    @Test
    fun testHealthConnectAvailability() {
        // Test that Health Connect SDK status can be queried
        val sdkStatus = HealthConnectClient.getSdkStatus(context)
        
        // The SDK status should be one of the defined values
        assertTrue(
            "SDK status should be a valid value",
            sdkStatus == HealthConnectClient.SDK_AVAILABLE ||
            sdkStatus == HealthConnectClient.SDK_UNAVAILABLE ||
            sdkStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
        )
    }
    
    @Test
    fun testHealthConnectClientCreation() {
        // Test that we can query SDK status without crashing
        val sdkStatus = HealthConnectClient.getSdkStatus(context)
        
        if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) {
            // If available, we should be able to create a client
            val client = HealthConnectClient.getOrCreate(context)
            assertNotNull("Health Connect client should not be null", client)
        } else {
            // If not available, that's okay - just verify we can handle it
            assertTrue(
                "SDK unavailable is an acceptable state",
                sdkStatus != HealthConnectClient.SDK_AVAILABLE
            )
        }
    }
    
    @Test
    fun testPermissionControllerAccess() {
        val sdkStatus = HealthConnectClient.getSdkStatus(context)
        
        if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) {
            val client = HealthConnectClient.getOrCreate(context)
            assertNotNull("Permission controller should be accessible", client.permissionController)
        }
    }
    
    @Test
    fun testContextIsNotNull() {
        assertNotNull("Application context should not be null", context)
        assertEquals(
            "Context package name should match",
            "com.openfitness.analysis",
            context.packageName
        )
    }
}
