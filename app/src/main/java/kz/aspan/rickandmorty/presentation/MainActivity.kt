package kz.aspan.rickandmorty.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.ActivityMainBinding
import kz.aspan.rickandmorty.presentation.characters.CharactersFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val outMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = this.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = this.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        val width = outMetrics.widthPixels
        val height = outMetrics.heightPixels

        println("TESTW $width           $height")
    }
}