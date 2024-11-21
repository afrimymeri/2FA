# Android 2FA Application

This project is an Android application that implements a Two-Factor Authentication (2FA) system using Firebase Authentication. The app allows users to sign up, log in, and verify their email addresses while securely managing their data in Firebase Realtime Database.

## Features

- **User Registration**: Users can register with their name, surname, phone number, email, and password.
- **Email Verification**: After registration, a verification email is sent to the user to confirm their email address.
- **Login**: Users can log in after verifying their email address.
- **Firebase Integration**:
  - Firebase Authentication for user authentication.
  - Firebase Realtime Database for storing user data.

 ## Installation
Clone the repository to your local machine:
Open the project in Android Studio.

Connect the app to your Firebase project:

Go to the Firebase Console.
Create a new project or use an existing one.
Add your app's package name (com.example.a2fa) to the Firebase project.
Download the google-services.json file and place it in the app/ directory of the project.
Sync the project with Gradle files to apply Firebase configuration.



