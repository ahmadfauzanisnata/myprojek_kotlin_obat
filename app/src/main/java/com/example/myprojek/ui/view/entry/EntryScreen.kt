package com.example.myprojek.ui.view.entry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.myprojek.R
import com.example.myprojek.ui.viewmodel.EntryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    viewModel: EntryViewModel,
    currentUserEmail: String,
    obatId: Int, // Parameter baru
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }

    // Jika obatId bukan -1 (mode edit), load data
    LaunchedEffect(obatId) {
        viewModel.loadObatIfExist(obatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // Ganti judul dinamis
                title = {
                    Text(if (obatId == -1) stringResource(R.string.title_entry)
                    else stringResource(R.string.title_edit))
                }
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
            OutlinedTextField(
                value = viewModel.jam,
                onValueChange = { viewModel.jam = it },
                label = { Text(stringResource(R.string.label_jam_input)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
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
                // Ganti teks tombol dinamis
                Text(
                    if (obatId == -1) stringResource(R.string.btn_save)
                    else stringResource(R.string.btn_update)
                )
            }
        }
    }
}
