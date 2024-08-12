package com.example.readify

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bookTitle = findViewById<EditText>(R.id.title)
        val authorName = findViewById<EditText>(R.id.author)
        val language = findViewById<EditText>(R.id.language)
        val statusSpinner = findViewById<Spinner>(R.id.status)
        val convertButton = findViewById<Button>(R.id.convertButton)

        // Setup Spinner with sample statuses
        val status = arrayOf("Yet to Start", "In Progress", "Completed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, status)
        statusSpinner.adapter = adapter

        convertButton.setOnClickListener {
            val title = bookTitle.text.toString()
            val author = authorName.text.toString()
            val lang = language.text.toString()
            val selectedStatus = statusSpinner.selectedItem.toString()  // Correct key: "STATUS"

            val intent = Intent(this, BookDetails::class.java)
            intent.putExtra("BOOK_TITLE", title)
            intent.putExtra("AUTHOR_NAME", author)
            intent.putExtra("LANGUAGE", lang)
            intent.putExtra("STATUS", selectedStatus)  // Correct key: "STATUS"
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_main_activity -> {
                // Already on MainActivity, no action needed
                true
            }
            R.id.action_book_detail_activity -> {
                val intent = Intent(this, BookDetails::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
