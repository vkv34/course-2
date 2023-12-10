package com.example.course2.ui.composables.dropdown

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownMenu(
    options: List<T>,
    selectedOption: T?,
    onSelectionChanged: (T) -> Unit,
    displayValue: (T?) -> String,
    title: String,
    editable: Boolean = false,
    onTextChange: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember(selectedOption) {
        mutableStateOf(displayValue(selectedOption))
    }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = !editable,
            value = text,
            onValueChange = {
                if (editable) {
                    text = it
                    onTextChange(it)
                }
            },
            label = { Text(title) },
            trailingIcon = { TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { if (!editable) expanded = false },
        ) {
            options
                .filter {
                    if (editable) {
                        displayValue(it).contains(text, ignoreCase = true)
                    } else {
                        true
                    }
                }
                .forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(displayValue(selectionOption)) },
                        onClick = {
                            onSelectionChanged(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
        }
    }
}