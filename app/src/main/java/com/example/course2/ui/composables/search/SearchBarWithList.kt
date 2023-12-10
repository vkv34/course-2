package com.example.course2.ui.composables.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Stable
class SearchBarWithListState {
    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    fun setActive(isActive: Boolean) {
        _isActive.update { isActive }
    }

}


@Composable
fun rememberSearchBarWithListState() = remember {
    SearchBarWithListState()
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> SearchBarWithList(
    query: String,
    onQueryChange: (String) -> Unit,
    searchList: List<T>,
    onSearchedClick: (T) -> Unit,
    searchListItem: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    itemKey: (T) -> Any? = { null },
    searchBy: (item: T, query: String) -> Boolean,
    state: SearchBarWithListState = rememberSearchBarWithListState()
) {

    val isActive by state.isActive.collectAsState()

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isActive,
        onActiveChange = {
            state.setActive(it)
            onActiveChange(it)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            searchList.filter { searchBy(it, query) }.forEach {
                item(key = itemKey(it)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSearchedClick(it)
                            }
                            .animateItemPlacement()
                    ) {
                        Divider()
                        searchListItem(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> SearchBarWithList(
    query: String,
    onQueryChange: (String) -> Unit,
    searchList: List<T>,
    onSearchedClick: (T) -> Unit,
    searchListItem: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    itemKey: (T) -> Any? = { null },
    state: SearchBarWithListState = rememberSearchBarWithListState()
) {

    val isActive by state.isActive.collectAsState()

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isActive,
        onActiveChange = {
            state.setActive(it)
            onActiveChange(it)
        },
        shape = MaterialTheme.shapes.extraSmall
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (searchList.isEmpty()) {
                if (query.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            text = "Введите запрос для начала поиска"
                        )
                    }
                }else{
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            text = "Ничего не найдено"
                        )
                    }
                }
            }
            searchList.forEach {
                item(key = itemKey(it)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSearchedClick(it)
                            }
                            .animateItemPlacement()
                    ) {
                        Divider()
                        searchListItem(it)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchPrev() {
    val strings = listOf(
        "afdadasd",
        "aslkujrtieu",
        "sdiuerit",
        "edgluiubfd",
        "dltgrjlbk nfkl",
        "askljfhjkwefn",
        "asfjksildufgyuiejhn",
        "asdasdas",
        "asfjksildfdvvufgyuiejhn",
        "x",
        "dsgfsjh",
        "ruweiojh",
    )
    var query by remember {
        mutableStateOf("")
    }
    val state = rememberSearchBarWithListState()
    Box(modifier = Modifier.fillMaxWidth()) {

        SearchBarWithList(
            query = query,
            onQueryChange = { query = it },
            onSearch = {},
            searchList = strings,
            onSearchedClick = {},
            searchListItem = {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            },
            state = state,
            itemKey = { it },
            searchBy = { item, query -> item.contains(query, ignoreCase = true) }
        )
    }
}