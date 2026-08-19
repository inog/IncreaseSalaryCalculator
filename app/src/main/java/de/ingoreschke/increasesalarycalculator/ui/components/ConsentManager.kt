package de.ingoreschke.increasesalarycalculator.ui.components

import android.app.Activity
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object ConsentManager {

    /**
     * Initializes Google UMP Consent flow for GDPR/EEA compliance.
     */
    fun requestConsent(activity: Activity, onConsentReady: () -> Unit) {
        val params = ConsentRequestParameters.Builder().build()
        val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(activity)

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    if (consentInformation.canRequestAds()) {
                        onConsentReady()
                    }
                }
            },
            {
                // In case of network failure or error, still proceed
                if (consentInformation.canRequestAds()) {
                    onConsentReady()
                }
            }
        )

        // If consent was previously obtained
        if (consentInformation.canRequestAds()) {
            onConsentReady()
        }
    }
}
