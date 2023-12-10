package com.example.course2.ui.composables.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.course2.ui.composables.dropdown.DropDownMenu

@Stable
interface SearchOptionParameter<T> {
    val parameterName: String
    val options: List<T>
    val defaultValue: T?
        get() = null

    fun getDisplayText(option: T?): String = option.toString()

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchOptionButton(
    parameters: List<SearchOptionParameter<T>>,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onOptionSelectionChanged: (List<T?>) -> Unit,
) {
    var dialogOpened by remember {
        mutableStateOf(false)
    }

    IconButton(
        modifier = modifier,
        onClick = { dialogOpened = !dialogOpened }) {
        Icon(imageVector = icon, contentDescription = null)
    }

    if (dialogOpened) {
        ModalBottomSheet(
            modifier = Modifier.heightIn(min = 500.dp),
            windowInsets = BottomSheetDefaults.windowInsets
                .exclude(WindowInsets.displayCutout)
                .exclude(WindowInsets.navigationBars)
            ,
            onDismissRequest = { dialogOpened = false }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val selectedOptions = remember {
                    mutableStateListOf<T?>().apply {
                        addAll(parameters.map { null })
                    }
                }
                parameters.forEachIndexed { index, parameter ->
                    Spacer(modifier = Modifier.height(16.dp))
                    var selectedOption: T? by remember {
                        mutableStateOf(parameter.defaultValue)
                    }
                    DropDownMenu(
                        options = parameter.options,
                        selectedOption = selectedOption,
                        onSelectionChanged = {
                            selectedOption = it
                            selectedOptions[index] = it
                            onOptionSelectionChanged(selectedOptions)
                        },
                        displayValue = { parameter.getDisplayText(it) },
                        title = "Сортировать по"
                    )
                }
            }
        }
    }
}