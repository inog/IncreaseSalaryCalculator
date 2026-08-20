# IncreaseSalaryCalculator

**IncreaseSalaryCalculator** is a modern, lightweight Android application built with **Jetpack Compose** and **Material 3** to calculate salary increases accurately based on current salary and percentage increase.

---

## Features

- 💶 **Precise Salary Calculation**: Uses `BigDecimal` with half-up rounding to avoid floating-point inaccuracies.
- 🎚️ **Interactive Slider & Direct Input**: Adjust percentage increase easily via slider (0–20%) or precise text input.
- 💾 **Persistent Inputs**: Automatically saves the last entered salary and percentage for subsequent sessions.
- 🌍 **Locale & Currency Aware**: Formats results, decimal separators, thousand separators, and currency symbols based on the user's locale.
- 🎨 **Material 3 & Edge-to-Edge**: Full support for Android 15 & 16 Edge-to-Edge display, dynamic theming (Material You), and dark/light modes.
- 🔒 **Privacy-First**: No personal salary data is collected or transmitted; all calculations and inputs remain strictly on-device.

---

## Technical Details

### Build Configuration
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 16 (API 36) — *Compliant with Google Play Android 15+ requirements*
- **Compile SDK**: Android 16 (API 36)
- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose (Compose BOM `2024.10.01` with Kotlin Compose Compiler Plugin)
- **Design System**: Material 3 (`androidx.compose.material3`)
- **Java Version**: JDK 17
- **Version**: 1.3.0 (`versionCode: 2400130`)

### Key Dependencies
- AndroidX Core KTX (`1.15.0`)
- AndroidX Activity Compose (`1.9.3`)
- AndroidX Lifecycle ViewModel Compose (`2.8.7`)
- AndroidX DataStore Preferences (`1.1.2`)
- Jetpack Compose BOM (`2024.10.01`)
- Google Play Services Ads (`23.6.0`)
- Google User Messaging Platform UMP (`3.1.0`)
- JUnit 4, Kotlinx Coroutines Test & Espresso

---

## Versioning Pattern

This project follows the **SSWR** versioning scheme:
```text
versionCode = {minSdk}{00}{3 digits Version epic.major.minor}
```
* Example: `minSdk = 24`, `versionName = "1.3.0"` $\rightarrow$ `versionCode = 2400130`

---

## Building the Project

```bash
# Build debug APK
./gradlew assembleDebug

# Build release bundle (AAB)
./gradlew bundleRelease

# Run unit tests
./gradlew test

# Install debug build on connected device/emulator
./gradlew installDebug
```

---

## Release Notes

### Version 1.3.0 (versionCode: 2400130)
- **Currency Switcher**: Choose your preferred currency (EUR, USD, GBP, CHF, JPY, CAD, AUD, PLN, SEK, or Auto).
- **Target Salary Calculator (Reverse Mode)**: Calculate required percentage increase from base salary to a desired target.
- **Monthly / Annual Toggle**: Instant period breakdown (monthly difference vs. annual difference).
- **Quick Preset Chips**: Fast percentage selection (+2%, +3%, +5%, +7.5%, +10%, +15%, +20%) with haptic feedback.
- **Material 3 Redesign**: Enhanced result card, smooth animated numbers, and expanded slider (0–50%).
- **Share & Copy Actions**: Easily copy results to clipboard or share via Android ShareSheet.
- **Negotiation Tips Dialog**: Built-in actionable tips for salary negotiation preparation.
- **Modern Architecture**: Full MVVM with ViewModel, StateFlow, and Jetpack DataStore persistence.
- **Full German & English Localization**: German translations and locale-aware comma/dot decimal input support.
- **GDPR & Performance**: Integrated Google UMP consent management and R8 release minification.

---

## License & Contact

- **Developer**: Ingo Reschke
- **Privacy Policy**: [privacypolicy.html](website/privacypolicy.html) (Live: [apps.ingoreschke.de/increasesalarycalculator/privacy.html](https://apps.ingoreschke.de/increasesalarycalculator/privacy.html))
- **Contact**: `isc [ at ] ingoreschke.de`

