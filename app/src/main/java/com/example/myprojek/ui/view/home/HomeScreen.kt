package com.example.myprojek.ui.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myprojek.R
import com.example.myprojek.data.entity.Obat
import com.example.myprojek.ui.theme.GradientStart
import com.example.myprojek.ui.theme.GradientEnd
import com.example.myprojek.ui.theme.PrimaryColor
import com.example.myprojek.ui.theme.SuccessColor
import com.example.myprojek.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    currentUserEmail: String,
    onNavigateToEntry: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onLogout: () -> Unit
) {
    // --- LOGIK IZIN NOTIFIKASI ---
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // Logika tambahan jika izin diberikan/ditolak bisa diletakkan di sini
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // ----------------------------

    LaunchedEffect(currentUserEmail) {
        viewModel.getObatList(currentUserEmail)
    }

    val obatList by viewModel.homeUiState.collectAsState()
    val scope = rememberCoroutineScope()

    // State untuk dropdown menu
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        // Background pattern (sama seperti auth screens)
        Image(
            painter = painterResource(R.drawable.bg_auth_pattern),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.05f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
        ) {
            // Header dengan user info dan logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_xlarge)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // User info dengan icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_xlarge))
                            .clip(CircleShape)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(PrimaryColor, GradientEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                        )
                    }

                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))

                    Column {
                        Text(
                            text = "Selamat Datang,",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                        Text(
                            text = currentUserEmail.take(20) + if (currentUserEmail.length > 20) "..." else "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }

                // Logout button dengan dropdown
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_xlarge))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.social_button_corner_radius)))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.cd_more_options),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Keluar",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                showMenu = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.ExitToApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            // Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_medium))
            ) {
                // Total Obat Card
                StatsCard(
                    title = "Total Obat",
                    value = obatList.size.toString(),
                    icon = Icons.Default.Medication,
                    color = PrimaryColor,
                    modifier = Modifier.weight(1f)
                )

                // Hari Ini Card
                StatsCard(
                    title = "Hari Ini",
                    value = viewModel.getTodayReminderCount(obatList).toString(),
                    icon = Icons.Default.Notifications,
                    color = SuccessColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            // Main Content Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensionResource(R.dimen.card_elevation),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_large))
                        .fillMaxWidth()
                ) {
                    // Header section dengan judul dan search
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Daftar Obat",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Add Button kecil
                        FloatingActionButton(
                            onClick = onNavigateToEntry,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_xlarge)),
                            containerColor = PrimaryColor,
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = dimensionResource(R.dimen.button_elevation)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.cd_add_obat),
                                tint = Color.White,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                    // Search Bar (didesain selaras dengan auth screens)
                    OutlinedTextField(
                        value = viewModel.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        placeholder = {
                            Text(
                                stringResource(R.string.hint_search),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        shape = RoundedCornerShape(dimensionResource(R.dimen.textfield_corner_radius)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // List Obat
                    if (obatList.isEmpty()) {
                        // Empty state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimensionResource(R.dimen.padding_xlarge)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_medication_empty),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(dimensionResource(R.dimen.logo_size_medium))
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                            Text(
                                text = stringResource(R.string.empty_obat),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                            Button(
                                onClick = onNavigateToEntry,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor,
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = dimensionResource(R.dimen.button_elevation),
                                    pressedElevation = dimensionResource(R.dimen.spacer_small)
                                )
                            ) {
                                Text(
                                    text = "Tambah Obat Pertama",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_small)),
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            items(obatList) { obat ->
                                ObatCard(
                                    obat = obat,
                                    onDelete = { scope.launch { viewModel.deleteObat(obat) } },
                                    onClick = { onNavigateToEdit(obat.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_large))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)))
                        .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun ObatCard(
    obat: Obat,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon obat dengan warna gradient
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_xlarge))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PrimaryColor, GradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_medication),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))

            // Info obat
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = obat.namaObat,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // GANTI CHIP DENGAN ROW TEXT ATAU BADGE
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dosis - Gunakan Surface sebagai pengganti Chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryColor.copy(alpha = 0.1f),
                        contentColor = PrimaryColor,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = obat.dosis,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Frekuensi - Gunakan Surface sebagai pengganti Chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GradientEnd.copy(alpha = 0.1f),
                        contentColor = GradientEnd,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = obat.frekuensi,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Jam minum
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(R.string.label_jam_minum, obat.jamMinum),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Delete button dengan confirm dialog
            IconButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_delete_obat),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = {
                Text(
                    text = "Hapus Obat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text("Yakin ingin menghapus ${obat.namaObat}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("HAPUS")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirm = false }
                ) {
                    Text("BATAL")
                }
            }
        )
    }
}