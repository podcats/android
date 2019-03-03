package com.podcats.player_ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.merge_player_controls.view.*
import java.util.concurrent.TimeUnit

private const val SECONDS_IN_MINUTE = 60
private const val TWO_DIGIT_PADDING = "%02d"

class PlayerControlsView constructor(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    var model: Model = Model(0, 0, isPlaying = false)
        set(model) {
            field = model
            bind(model)
        }

    init {
        inflate(context, R.layout.merge_player_controls, this)
    }

    fun setOnClickRewindListener(listener: () -> Unit) = rewindButton.setOnClickListener { listener() }

    fun setOnClickForwardListener(listener: () -> Unit) = forwardButton.setOnClickListener { listener() }

    fun setOnClickPlayPauseListener(listener: () -> Unit) = playPauseButton.setOnClickListener { listener() }

    private fun bind(model: Model) {
        seekBar.max = model.totalMillis
        seekBar.setProgress(model.positionMillis, true)

        timeStartTextView.text = model.positionMillis.millisToTimestamp()
        timeEndTextView.text = model.totalMillis.millisToTimestamp()

        if (model.isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_pause_circle)
        } else {
            playPauseButton.setImageResource(R.drawable.ic_play_circle)
        }
    }

    /**
     * e.g. "1:05" for millis representing 1 minute 5 seconds.
     */
    private fun Int.millisToTimestamp(): CharSequence {
        val asSeconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong()).toInt()
        val minutes = asSeconds / SECONDS_IN_MINUTE
        val seconds = asSeconds % SECONDS_IN_MINUTE
        return "$minutes:${seconds.apply { TWO_DIGIT_PADDING.format(this) }}"
    }

    data class Model(
            val positionMillis: Int,
            val totalMillis: Int,
            val isPlaying: Boolean
    )
}
