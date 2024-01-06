
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.ui.theme.PlaylistTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistTheme {
                Surface {
                    Hello()
                }
            }
        }

    }
}

@Composable
fun Hello(){
    Text("Hello world")
}