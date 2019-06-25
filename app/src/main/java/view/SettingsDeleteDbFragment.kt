package view

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import hu.bme.aut.android.chainreaction.R
import model.db.DbDefaults
import model.db.challenge.ChallengeDatabase
import model.db.stats.PlayerTypeStatsDatabase

class SettingsDeleteDbFragment : PreferenceFragmentCompat() {

    private var deleteDialog: AlertDialog? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, key: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onResume() {

        super.onResume()

        val myPref = findPreference("delete_database") as Preference
        myPref.setOnPreferenceClickListener {
            deleteDialog()
            true
        }

    }

    private fun deleteDialog(){

        if(deleteDialog == null) {

            deleteDialog = AlertDialog.Builder(this.requireContext())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_delete_database))
                .setPositiveButton(android.R.string.yes) { dialog, which ->

                    val dbStats = Room.databaseBuilder(this.requireContext(), PlayerTypeStatsDatabase::class.java, "db_stats").build()
                    val dbChallenge = Room.databaseBuilder(this.requireContext(), ChallengeDatabase::class.java, "db_challenge").build()

                    Thread {

                        dbStats.playerTypeStatDAO().deleteAll()
                        val defaultStats = DbDefaults.statsDatabaseDefaults()
                        for(stat in defaultStats){
                            dbStats.playerTypeStatDAO().insert(stat)
                        }
                        dbStats.close()

                        dbChallenge.challengeLevelsDAO().deleteAll()
                        val defaultChallengeStates = DbDefaults.challengeDatabaseDefaults()
                        for(level in defaultChallengeStates){
                            dbChallenge.challengeLevelsDAO().insert(level)
                        }
                        dbChallenge.close()

                    }.start()
                    deleteDialog = null
                    Snackbar.make(this.activity?.findViewById(android.R.id.content)!!, "Statistics are cleared", Snackbar.LENGTH_SHORT).show()

                }
                .setNegativeButton(android.R.string.no) {dialog, which ->
                    deleteDialog = null
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

        }

    }

}