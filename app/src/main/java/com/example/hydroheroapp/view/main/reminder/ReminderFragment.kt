package com.example.hydroheroapp.view.main.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hydroheroapp.R
import com.example.hydroheroapp.data.local.Reminder
import com.example.hydroheroapp.databinding.FragmentReminderBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class ReminderFragment : Fragment() {
    private lateinit var binding: FragmentReminderBinding
    private lateinit var reminders: MutableList<Reminder>
    private lateinit var adapter: ReminderAdapter
    private val sharedPrefs by lazy { requireContext().getSharedPreferences("reminders", Context.MODE_PRIVATE) }

    private var selectedRingtoneUri: Uri? = null

    private val selectRingtoneLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            @Suppress("DEPRECATION")
            selectedRingtoneUri = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            updateRingtoneButtonText(selectedRingtoneUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load reminders from SharedPreferences
        loadReminders()

        // Load switch states from SharedPreferences
        loadSwitchStates()

        // Initialize RecyclerView
        adapter = ReminderAdapter(reminders, this::showReminderOptionsDialog)
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = adapter

        // Set up FAB click listener
        binding.fabAddNotification.setOnClickListener {
            showAddReminderDialog()
        }

        // Set up switch listeners to save their states
        binding.cvSounds.findViewById<Switch>(R.id.switch_sounds).setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState("switch_sounds", isChecked)
        }
        binding.cvSounds.findViewById<Switch>(R.id.switch_vibrate).setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState("switch_vibrate", isChecked)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "DefaultLocale")
    private fun showAddReminderDialog(reminder: Reminder? = null) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_reminder, null)
        val timePicker: TimePicker = dialogView.findViewById(R.id.time_picker)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_description)
        val intervalSpinner: Spinner = dialogView.findViewById(R.id.spinner_interval)
        val selectRingtoneButton: Button = dialogView.findViewById(R.id.btn_select_ringtone)

        val intervals = arrayOf("5 minutes", "10 minutes", "15 minutes", "20 minutes", "25 minutes", "30 minutes")
        intervalSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, intervals)

        if (reminder != null) {
            val (hour, minute) = reminder.time.split(":").map { it.toInt() }
            timePicker.hour = hour
            timePicker.minute = minute
            descriptionEditText.setText(reminder.description)
            intervalSpinner.setSelection(intervals.indexOf("${reminder.interval} minutes"))
            selectedRingtoneUri = Uri.parse(reminder.ringtone)
            updateRingtoneButtonText(selectedRingtoneUri)
        }

        selectRingtoneButton.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone")
                putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedRingtoneUri)
            }
            selectRingtoneLauncher.launch(intent)
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle(if (reminder == null) "Add Reminder" else "Edit Reminder")
            .setPositiveButton("Save") { dialog, _ ->
                val hour = timePicker.hour
                val minute = timePicker.minute
                val time = String.format("%02d:%02d", hour, minute)
                val description = descriptionEditText.text.toString()
                val interval = intervals[intervalSpinner.selectedItemPosition].split(" ")[0].toInt()

                val defaultRingtoneUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.default_ringtone)
                val ringtoneUri = selectedRingtoneUri ?: defaultRingtoneUri

                val soundEnabled = getSwitchState(R.id.switch_sounds)
                val vibrate = getSwitchState(R.id.switch_vibrate)

                if (reminder == null) {
                    val newReminder = Reminder(
                        time, description, interval, soundEnabled, vibrate,
                        ringtone = ringtoneUri.toString()
                    )
                    reminders.add(newReminder)
                    adapter.notifyDataSetChanged()
                    scheduleNotification(newReminder)
                } else {
                    reminder.time = time
                    reminder.description = description
                    reminder.interval = interval
                    reminder.soundEnabled = soundEnabled
                    reminder.vibrate = vibrate
                    reminder.ringtone = ringtoneUri.toString()
                    adapter.notifyDataSetChanged()
                    scheduleNotification(reminder)
                }
                saveReminders()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateRingtoneButtonText(uri: Uri?) {
        val ringtoneName = uri?.let {
            RingtoneManager.getRingtone(requireContext(), it).getTitle(requireContext())
        } ?: "Default"
        view?.findViewById<Button>(R.id.btn_select_ringtone)?.text = ringtoneName
    }

    private fun getSwitchState(switchId: Int): Boolean {
        val switch = view?.findViewById<Switch>(switchId)
        return switch?.isChecked ?: true
    }

    private fun showReminderOptionsDialog(reminder: Reminder) {
        AlertDialog.Builder(requireContext())
            .setTitle("Reminder Options")
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> showAddReminderDialog(reminder)
                    1 -> deleteReminder(reminder)
                }
            }
            .create()
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteReminder(reminder: Reminder) {
        reminders.remove(reminder)
        adapter.notifyDataSetChanged()
        cancelNotification(reminder)
        saveReminders()
    }

    private fun cancelNotification(reminder: Reminder) {
        val notificationIntent = Intent(requireContext(), ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), reminder.time.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(reminder: Reminder) {
        val notificationIntent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("time", reminder.time)
            putExtra("description", reminder.description)
            putExtra("interval", reminder.interval)
            putExtra("ringtone", reminder.ringtone)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), reminder.time.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            val (hour, minute) = reminder.time.split(":").map { it.toInt() }
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        // Ensure the alarm is set for the future, not past
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun saveReminders() {
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(reminders)
        editor.putString("reminder_list", json)
        editor.apply()
    }

    private fun loadReminders() {
        val gson = Gson()
        val json = sharedPrefs.getString("reminder_list", null)
        val type = object : TypeToken<MutableList<Reminder>>() {}.type
        reminders = if (json != null) gson.fromJson(json, type) else mutableListOf()
    }

    private fun saveSwitchState(key: String, state: Boolean) {
        sharedPrefs.edit().putBoolean(key, state).apply()
    }

    private fun loadSwitchStates() {
        binding.cvSounds.findViewById<Switch>(R.id.switch_sounds).isChecked = sharedPrefs.getBoolean("switch_sounds", true)
        binding.cvSounds.findViewById<Switch>(R.id.switch_vibrate).isChecked = sharedPrefs.getBoolean("switch_vibrate", true)
    }
}