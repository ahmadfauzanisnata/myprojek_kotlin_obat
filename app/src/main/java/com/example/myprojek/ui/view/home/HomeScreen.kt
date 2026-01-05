package com.example.myprojek.ui.view.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myprojek.R
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    currentUserEmail: String,
    onNavigateToEntry: () -> Unit,
    onNavigateToEdit: (Int) -> Unit, // Callback baru untuk edit
    onLogout: () -> Unit
) {
    LaunchedEffect(currentUserEmail) {
        viewModel.getObatList(currentUserEmail)
    }
    val obatList by viewModel.homeUiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_home)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.cd_logout)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToEntry) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_obat)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            // --- FITUR SEARCH ---
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text(stringResource(R.string.hint_search)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_small)),
                singleLine = true
            )
            // --------------------

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_small)),
                modifier = Modifier.fillMaxSize()
            ) {
                items(obatList) { obat ->
                    ObatCard(
                        obat = obat,
                        onDelete = { scope.launch { viewModel.deleteObat(obat) } },
                        onClick = { onNavigateToEdit(obat.id) } // Klik card untuk edit
                    )
                }
                if (obatList.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.empty_obat),
                            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_large))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ObatCard(obat: Obat, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Buat card bisa diklik
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = obat.namaObat,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${obat.dosis} • ${obat.frekuensi}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.label_jam_minum, obat.jamMinum),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_delete_obat),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
