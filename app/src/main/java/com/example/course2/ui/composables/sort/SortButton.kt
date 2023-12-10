package com.example.course2.ui.composables.sort

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.course2.ui.composables.filter.SearchOptionButton
import com.example.course2.ui.composables.filter.SearchOptionParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Stable
interface Sortable<T> {
    fun sort(forSorting: T): T

}

class AscendingSort<T, R : Comparable<R>>(
    private val sortBy: (T) -> R?
) : Sortable<Flow<List<T>>> {
    override fun sort(forSorting: Flow<List<T>>): Flow<List<T>> = forSorting.onEach {
        it.sortedBy(sortBy)
    }

}

class DescendingSort<T, R : Comparable<R>>(
    private val sortBy: (T) -> R?
) : Sortable<Flow<List<T>>> {
    override fun sort(forSorting: Flow<List<T>>): Flow<List<T>> = forSorting.onEach {
        it.sortedByDescending(sortBy)
    }
}

sealed class NumericSortParameters(
    val name: String
) {
    data object Ascending : NumericSortParameters("По возрастанию")
    data object Descending : NumericSortParameters("По убыванию")
}


data object NumericSortParameter : SearchOptionParameter<NumericSortParameters> {
    override val parameterName: String
        get() = "Сортировка"
    override val options: List<NumericSortParameters>
        get() = listOf(
            NumericSortParameters.Ascending,
            NumericSortParameters.Descending,
        )

    override fun getDisplayText(option: NumericSortParameters?): String =
        option?.name ?: "По умолчанию"
}


@Preview
@Composable
fun SortButtonPreview() {
    var selectedOptions by remember {
        mutableStateOf(listOf<NumericSortParameters?>())
    }
    Column {
        SearchOptionButton(
            parameters = listOf(
                NumericSortParameter
            ),
            icon = Icons.Default.Build,
            onOptionSelectionChanged = {
                selectedOptions = it
            }
        )

        selectedOptions.forEach {
            Text(text = it.toString())
        }
    }
}