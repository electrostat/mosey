package agbytech.com.mosey

import agbytech.com.mosey2.Mosey
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var mosey: Mosey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mosey = Mosey(this)
    }
}
