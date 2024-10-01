package issue.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = MutableStateFlow<ListState<User>>(ListState.Initial)
        val completedData = listOf(
            User("User1", "Tommy", 20),
            User("User2", "Jerry", 30),
            User("User3", "Spike", 40),
            User("User4", "Tyke", 10),
        )

        setContent {
            LaunchedEffect(Unit) {
                items.update { ListState.Loading(data = emptyList()) }
                delay(1.seconds)
                items.update { ListState.Completed(data = completedData) }

                // ⚠️ This is the state where the crash happens
                //
                // Delay is for simulation only
                // In the real app, we're updating outside the composable

                delay(1.seconds)
                items.update { ListState.Initial }
                delay(1.seconds)
                items.update { ListState.Loading(data = emptyList()) }
            }

            LazyColumnWithIssue(
                items = items,
                modifier = Modifier.fillMaxSize(),
            ) { _, item ->
                Text(
                    text = "${item.name} - ${item.age}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }

}
