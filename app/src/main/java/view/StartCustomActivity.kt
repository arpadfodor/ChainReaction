package view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import kotlinx.android.synthetic.main.activity_start_custom.*
import presenter.*

/**
 * Activity of creating a custom game play
 */
class StartCustomActivity : AppCompatActivity(), IStartCustomView {

    companion object {
        private const val CUSTOM_GAME = 1

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    /**
     * presenter of the view
     */
    private lateinit var presenter: StartCustomPresenter

    private var gameType = CUSTOM_GAME
    private var gameMode = NORMAL_MODE

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_custom)

        presenter = StartCustomPresenter(this, this.applicationContext)

        val gameTypeStartView = findViewById<ImageView>(R.id.gameTypeStartCustomView)
        gameTypeStartView.setImageDrawable(resources.getDrawable(R.drawable.game_mode_custom))

        val addHumanPlayerButton = findViewById<Button>(R.id.buttonAddHumanPlayer)
        addHumanPlayerButton.setOnClickListener {
            presenter.addHumanPlayer()
        }

        val addAIPlayerButton = findViewById<Button>(R.id.buttonAddAIPlayer)
        addAIPlayerButton.setOnClickListener {
            presenter.addAIPlayer()
        }

        val clearPlayersButton = findViewById<Button>(R.id.buttonClearPlayers)
        clearPlayersButton.setOnClickListener {
            presenter.clearPlayers()
        }

        val startGameButton = findViewById<Button>(R.id.buttonStartGame)
        startGameButton.setOnClickListener {

            if(presenter.canGameBeStarted()){

                val myIntent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("number_of_players", presenter.getPlayerCount())
                myIntent.putExtra("PlayGroundHeight", presenter.getPlayGroundHeight())
                myIntent.putExtra("PlayGroundWidth", presenter.getPlayGroundWidth())
                myIntent.putExtra("GameType", gameType)
                myIntent.putExtra("GameMode", gameMode)
                myIntent.putExtra("ChallengeLevel", 0)

                for(i in 0 until presenter.getPlayerCount()){
                    myIntent.putExtra((i+1).toString(), presenter.getPlayerStringElementAt(i))
                }

                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            }

        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = presenter.getAdapter()

        updateHeightText(presenter.getPlayGroundHeight())
        updateWidthText(presenter.getPlayGroundWidth())

        val heightPlusButton = findViewById<Button>(R.id.buttonHeightPlus)
        heightPlusButton.setOnClickListener {
            presenter.heightPlus()
        }

        val heightMinusButton = findViewById<Button>(R.id.buttonHeightMinus)
        heightMinusButton.setOnClickListener {
            presenter.heightMinus()
        }

        val widthPlusButton = findViewById<Button>(R.id.buttonWidthPlus)
        widthPlusButton.setOnClickListener {
            presenter.widthPlus()
        }

        val widthMinusButton = findViewById<Button>(R.id.buttonWidthMinus)
        widthMinusButton.setOnClickListener {
            presenter.widthMinus()
        }

        val randomButton = findViewById<Button>(R.id.buttonRandom)
        randomButton.setOnClickListener {
            presenter.randomConfig()
        }

        mAdView = findViewById(R.id.startCustomAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Shows the user that a Player has been added
     *
     * @param   playerNumber    Id of the Player
     */
    override fun playerAdded(playerNumber: Int){
        Snackbar.make(recyclerViewPlayers, getString(R.string.player_added, (playerNumber)), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that Players list is full
     */
    override fun playersFull(){
        Snackbar.make(recyclerViewPlayers, R.string.maximum_reached, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that Players list has been cleared
     */
    override fun playersCleared(){
        Snackbar.make(recyclerViewPlayers, R.string.list_clear, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that there are not enough Players to start
     */
    override fun notEnoughPlayer(){
        Snackbar.make(recyclerViewPlayers, R.string.not_enough_player, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that maximum size has been reached
     */
    override fun maximumSizeReached(){
        Snackbar.make(recyclerViewPlayers, R.string.maximum_size, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that minimum size has been reached
     */
    override fun minimumSizeReached(){
        Snackbar.make(recyclerViewPlayers, R.string.minimum_size, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that random generating happened
     */
    override fun randomGenerated(){
        Snackbar.make(recyclerViewPlayers, R.string.random_generated, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user the current height
     *
     * @param    value          Height value to show
     */
    override fun updateHeightText(value: Int){
        val heightTextView = findViewById<TextView>(R.id.tvHeight)
        heightTextView.text = getString(R.string.height_show, value)
    }

    /**
     * Shows the user the current width
     *
     * @param    value          Width value to show
     */
    override fun updateWidthText(value: Int){
        val widthTextView = findViewById<TextView>(R.id.tvWidth)
        widthTextView.text = getString(R.string.width_show, value)
    }

    /**
     * Called when leaving the activity - stops the presenter calculations too
     */
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    /**
     * Called when returning to the activity
     */
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    /**
     * Called before the activity is destroyed
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}