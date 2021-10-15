package kz.aspan.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.aspan.rickandmorty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CharactersFragment())
            .commit()
    }
}