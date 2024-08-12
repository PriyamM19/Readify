package com.example.readify

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentContainerView

class BookDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val authorName = intent.getStringExtra("AUTHOR_NAME")
        val language = intent.getStringExtra("LANGUAGE")
        val status = intent.getStringExtra("STATUS")  // Retrieve the "STATUS" key correctly

        val viewBookTitle = findViewById<TextView>(R.id.viewBookTitle)
        val viewBookAuthor = findViewById<TextView>(R.id.viewBookAuthor)
        val viewBookLang = findViewById<TextView>(R.id.viewBookLang)
        val buttonSetReminder = findViewById<Button>(R.id.buttonSetReminder)
        val fragmentContainer = findViewById<FragmentContainerView>(R.id.fragmentContainer)

        viewBookTitle.text = "Book Title : $bookTitle"
        viewBookAuthor.text = "Book's Author Name : $authorName"
        viewBookLang.text = "Book Language: $language"

        buttonSetReminder.setOnClickListener {
            showNotification(bookTitle, authorName)
            // Show toast message with the Spinner value
            Toast.makeText(this, " $bookTitle added to shelf - Status: $status", Toast.LENGTH_SHORT).show()
        }

        // Load the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NotifyFragment()) // Replace with your Fragment
            .commit()
    }

    private fun showNotification(bookTitle: String?, authorName: String?) {
        val channelId = "book_reminder_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Book Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Reminder: $bookTitle")
            .setContentText("Author: $authorName")
            .setSmallIcon(R.drawable.notify) // Replace with your icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Adds sound and vibration
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("NotificationTest", "Notification is being shown")
            notificationManager.notify(1, notification)
        } else {
            Log.d("NotificationTest", "Requesting notification permission")
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_main_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_book_detail_activity -> {
                // Already on DetailActivity, no action needed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
