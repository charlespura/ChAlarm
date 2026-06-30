
# ChAlarm - Photo-Matching Alarm Clock

An Android alarm clock application that requires you to take a photo of a specific household object to turn off the alarm. Built with Java and Android Studio.

## 📱 Features

- **Custom Object Setup**: Register household objects with photos and labels
- **Smart Alarm Triggering**: Alarm fires at exact scheduled time using Android's AlarmManager
- **Random Morning Challenge**: Each alarm randomly selects one registered object for verification
- **Photo Matching Verification**: Captures and compares photos using an image-matching algorithm
- **Full-Screen Alarm Mode**: Blocks navigation buttons until the alarm is dismissed
- **Emergency PIN**: Backup dismissal method for emergencies
- **Future Snooze Support**: Planned snooze functionality for later releases

## 🛠️ Technical Architecture

- **Language**: Java
- **Minimum SDK**: Android 11 (API 30)
- **Target SDK**: Android 14 (API 34)
- **Architecture**: MVC with Services and Broadcast Receivers

### Key Components

- **AlarmManager**: Handles exact alarm scheduling
- **Foreground Service**: Plays alarm sound and vibration
- **ImageMatcher**: Compares captured photos with reference images
- **ObjectManager**: Manages object storage and retrieval
- **SharedPreferences**: Stores object labels and metadata
- **Internal Storage**: Stores reference photos as JPEGs

## 📸 How It Works

1. **Setup Phase**:
   - Take photos of household objects
   - Add custom labels for each object
   - Register objects for use in alarms

2. **Alarm Phase**:
   - Set alarm time
   - When the alarm triggers, the app randomly selects one registered object
   - Full-screen alarm shows the object label
   - User must take a matching photo to dismiss the alarm

3. **Verification**:
   - App compares the captured photo with the reference image
   - A similarity threshold is required for dismissal
   - Failed attempts prompt the user to retry

## 🔐 Permissions

- `RECEIVE_BOOT_COMPLETED` - Reschedule alarms after reboot
- `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` - Precision timing
- `CAMERA` - Capture setup and morning photos
- `FOREGROUND_SERVICE` / `FOREGROUND_SERVICE_MEDIA_PLAYBACK` - Background alarm playback
- `WAKE_LOCK` and `DISABLE_KEYGUARD` - Turn on screen and bypass the lock screen
- `VIBRATE` - Alarm vibration

## 🚀 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/charlespura/ChAlarm.git
   ```
2. Open the project in Android Studio
3. Build and run on an Android 11+ device or emulator
4. Grant permissions when prompted

### Requirements

- Android Studio 2024.2.1 or later
- Android SDK 34
- Gradle 8.7
- Java 11+

## 🏗️ Project Structure
<img width="300" height="555" alt="Screenshot 2026-06-30 at 7 05 07 PM" src="https://github.com/user-attachments/assets/b57b6d77-00ac-410e-ad9e-8776d5e5cb87" />


## 🎯 Future Improvements

- Support for multiple alarm schedules
- Snooze functionality
- Cloud backup for object images
- Voice assistant integration
- Analytics and statistics

## 📄 License
This project is open source and available under the MIT License.

## 👤 Author
Charles Pura

GitHub: @charlespura
