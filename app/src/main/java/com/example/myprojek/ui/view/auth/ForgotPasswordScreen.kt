package com.example.myprojek.ui.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojek.R
import com.example.myprojek.ui.theme.GradientStart
import com.example.myprojek.ui.theme.GradientEnd
import com.example.myprojek.ui.theme.PrimaryColor
import com.example.myprojek.ui.theme.SuccessColor
import com.example.myprojek.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    // State untuk auto-navigate setelah sukses
    var showSuccess by remember { mutableStateOf(false) }
    // State untuk visibility password
    var passwordVisible by remember { mutableStateOf(false) }

    // Effect untuk auto-navigate setelah sukses
    LaunchedEffect(viewModel.resetPasswordSuccessMessage) {
        if (viewModel.resetPasswordSuccessMessage != null) {
            showSuccess = true
            // Delay 2 detik sebelum kembali otomatis
            delay(2000)
            viewModel.afterResetPasswordSuccess()
            onNavigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            )
    ) {
        // Background decorative elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(R.dimen.padding_xxlarge))
        ) {
            Image(
                painter = painterResource(R.drawable.bg_auth_pattern),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
                alpha = 0.05f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_xlarge)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearFields()
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_xxlarge))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.social_button_corner_radius)))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.btn_back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // LOGO & JUDUL DI LUAR KOTAK PUTIH
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
            ) {
                // Icon dengan gradient background - DI LUAR KARD
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.logo_size_small))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryColor, GradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_password_reset),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large))
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                // Title - DI LUAR KARD
                Text(
                    text = stringResource(R.string.title_forgot_password),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))

                // Subtitle - DI LUAR KARD
                Text(
                    text = stringResource(R.string.subtitle_forgot_password),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Main Content Card (HANYA FORM SAJA)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensionResource(R.dimen.card_elevation),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                        spotColor = Color.White.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xlarge))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // JIKA SUDAH SUKSES, TAMPILKAN PESAN SUKSES SAJA
                    if (showSuccess && viewModel.resetPasswordSuccessMessage != null) {
                        // Success Message Card - TETAP DI CARD YANG SAMA
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Success Icon dengan background
                            Box(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.logo_size_small))
                                    .clip(RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)))
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(SuccessColor, SuccessColor.copy(alpha = 0.7f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_success),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large))
                                )
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                            // Title
                            Text(
                                text = "Password Berhasil Diubah!",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                ),
                                color = SuccessColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))



                            // Jangan tampilkan form lagi setelah sukses
                            return@Column
                        }
                    }

                    // JIKA BELUM SUKSES, TAMPILKAN FORM RESET PASSWORD

                    // Email field with icon
                    OutlinedTextField(
                        value = viewModel.emailInput,
                        onValueChange = { viewModel.emailInput = it },
                        label = {
                            Text(
                                stringResource(R.string.label_email),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.textfield_corner_radius)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                    // New Password field with icon dan visibility toggle
                    OutlinedTextField(
                        value = viewModel.passwordInput,
                        onValueChange = { viewModel.passwordInput = it },
                        label = {
                            Text(
                                stringResource(R.string.label_new_password),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        stringResource(R.string.hide_password)
                                    else
                                        stringResource(R.string.show_password),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        supportingText = {
                            Text(
                                stringResource(R.string.text_min_chars),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        shape = RoundedCornerShape(dimensionResource(R.dimen.textfield_corner_radius)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error Message
                    viewModel.errorMessage?.let { errorMsg ->
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.social_button_corner_radius)),
                            color = MaterialTheme.colorScheme.errorContainer,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = dimensionResource(R.dimen.padding_medium),
                                    vertical = dimensionResource(R.dimen.padding_small)
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_error),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
                                Text(
                                    text = errorMsg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xlarge)))

                    // Reset Password Button
                    Button(
                        onClick = {
                            viewModel.resetPassword(onSuccess = {})
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = dimensionResource(R.dimen.button_elevation),
                            pressedElevation = dimensionResource(R.dimen.spacer_small),
                            hoveredElevation = dimensionResource(R.dimen.button_elevation),
                            focusedElevation = dimensionResource(R.dimen.button_elevation),
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.btn_reset_password),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                }
            }
        }
    }
}