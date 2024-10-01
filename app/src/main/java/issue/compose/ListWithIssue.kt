package issue.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

sealed interface ListState<out T> {

    val data: List<T>

    data object Initial : ListState<Nothing> {
        override val data: List<Nothing> = emptyList()
    }

    data class Loading<T>(
        override val data: List<T>
    ) : ListState<T>

    data class Completed<T>(
        override val data: List<T>
    ) : ListState<T>
}

private const val LOADING_ITEM_KEY = "loading_item"

@Composable
fun <T : UniqueItem> LazyColumnWithIssue(
    items: Flow<ListState<T>>,
    modifier: Modifier = Modifier,
    contentType: (T) -> String? = { null },
    item: @Composable LazyItemScope.(Int, T) -> Unit,
) {
    val listState by items.collectAsStateWithLifecycle(initialValue = ListState.Initial)

    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = listState.data,
            key = { _, item -> item.id },
            contentType = { _, item -> contentType(item) }
        ) { index, item ->
            item(index, item)
        }

        if (listState is ListState.Loading) {
            item(key = LOADING_ITEM_KEY) {
                Spacer(modifier = Modifier.size(16.dp))
                DefaultLoadingCase()
            }
        }
    }
}


@Composable
private fun DefaultLoadingCase(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 24.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center)
        )
    }
}
