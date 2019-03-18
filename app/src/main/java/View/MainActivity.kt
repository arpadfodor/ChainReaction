package View

import Task.AILoaderTask
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import hu.bme.aut.android.chainreaction.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val NewGameButton = findViewById<Button>(R.id.buttonNewGame)
        NewGameButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        val StatsButton = findViewById<Button>(R.id.buttonStats)
        StatsButton.setOnClickListener {
            //todo
        }

        val SettingsButton = findViewById<Button>(R.id.buttonSettings)
        SettingsButton.setOnClickListener {
            //todo
        }

        val AboutButton = findViewById<Button>(R.id.buttonAbout)
        AboutButton.setOnClickListener {
            //todo
        }

        val ExitButton = findViewById<Button>(R.id.buttonExit)
        ExitButton.setOnClickListener {
            finish()
            System.exit(0)
        }

        var task_AI_load = AILoaderTask()
        task_AI_load.LoadNeuralNetwork(this)

    }
}
