package com.example.myprojek.ui.view.entry

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.myprojek.R
import com.example.myprojek.ui.viewmodel.EntryViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    viewModel: EntryViewModel,
    currentUserEmail: String,
    obatId: Int,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }

    // Jika obatId bukan -1 (mode edit), load data
    LaunchedEffect(obatId) {
        viewModel.loadObatIfExist(obatId)
    }

    // --- SETUP TIME PICKER ---
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            viewModel.jam = formattedTime
        },
        hour,
        minute,
        true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (obatId == -1) stringResource(R.string.title_entry)
                        else stringResource(R.string.title_edit)
                    )
                },
                // --- TAMBAHAN: Tombol Kembali (Navigation Icon) ---
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
                // --------------------------------------------------
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_medium))
        ) {
            OutlinedTextField(
                value = viewModel.namaObat,
                onValueChange = { viewModel.namaObat = it },
                label = { Text(stringResource(R.string.label_nama_obat)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.dosis,
                onValueChange = { viewModel.dosis = it },
                label = { Text(stringResource(R.string.label_dosis)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.frekuensi,
                onValueChange = { viewModel.frekuensi = it },
                label = { Text(stringResource(R.string.label_frekuensi)) },
                modifier = Modifier.fillMaxWidth()
            )

            // --- INPUT JAM (Dengan Time Picker) ---
            OutlinedTextField(
                value = viewModel.jam,
                onValueChange = { },
                label = { Text(stringResource(R.string.label_jam_input)) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { timePickerDialog.show() }) {
                        Icon(Icons.Default.AccessTime, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { timePickerDialog.show() },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            if (isError) {
                Text(
                    text = stringResource(R.string.error_fill_all),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (viewModel.namaObat.isNotEmpty() && viewModel.jam.isNotEmpty()) {
                        scope.launch {
                            viewModel.saveObat(currentUserEmail, context)
                            onNavigateBack()
                        }
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (obatId == -1) stringResource(R.string.btn_save)
                    else stringResource(R.string.btn_update)
                )
            }
        }
    }
}
