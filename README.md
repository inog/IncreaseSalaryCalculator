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
- **Version**: 1.2.1 (`versionCode: 2400121`)

### Key Dependencies
- AndroidX Core KTX (`1.15.0`)
- AndroidX Activity Compose (`1.9.3`)
- Jetpack Compose BOM (`2024.10.01`)
- Google Play Services Ads (`23.6.0`)
- JUnit 4 & AndroidX Test Runner / Espresso

---

## Versioning Pattern

This project follows the **SSWR** versioning scheme:
```text
versionCode = {minSdk}{00}{3 digits Version epic.major.minor}
```
* Example: `minSdk = 24`, `versionName = "1.2.1"` $\rightarrow$ `versionCode = 2400121`

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

### Version 1.2.1 (versionCode: 2400121)
- **Target SDK 36 (Android 16)**: Full support and optimization for Android 15 & 16.
- **Edge-to-Edge Display**: Modernized UI layout with edge-to-edge rendering and safe window insets padding.
- **Stack Modernization**: Upgraded to Kotlin 2.0.21, official Kotlin Compose Compiler plugin, and Compose BOM 2024.10.01.
- **Dependency & Performance Updates**: Updated Google Mobile Ads SDK and AndroidX libraries.
- **Standardized Versioning**: Adopted structured `{minSdk}{00}{version}` version code pattern.

---

## License & Contact

- **Developer**: Ingo Reschke
- **Privacy Policy**: [privacypolicy.html](website/privacypolicy.html)
- **Contact**: `kontakt [ at ] isc.ingo-reschke.de`

