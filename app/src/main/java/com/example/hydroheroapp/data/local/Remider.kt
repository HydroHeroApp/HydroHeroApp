package com.example.hydroheroapp.data.local
data class Reminder(
    var time: String,
    var description: String,
    var interval: Int,
    var soundEnabled: Boolean,
    var vibrate: Boolean,
    var ringtone: String
)