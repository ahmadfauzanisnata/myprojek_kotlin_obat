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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprojek.R
import com.example.myprojek.ui.theme.GradientStart
import com.example.myprojek.ui.theme.GradientEnd
import com.example.myprojek.ui.theme.PrimaryColor
import com.example.myprojek.ui.theme.SuccessColor
import com.example.myprojek.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // JIKA SUDAH SUKSES, TAMPILKAN HALAMAN SUKSES TERPISAH
    if (viewModel.registerSuccessMessage != null) {
        RegisterSuccessScreen(
            successMessage = viewModel.registerSuccessMessage!!,
            onNavigateBack = {
                viewModel.afterRegisterSuccess()
                onNavigateBack()
            }
        )
        return
    }

    var acceptedTerms by remember { mutableStateOf(false) }

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

            // Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
            ) {
                Text(
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))

                Text(
                    text = stringResource(R.string.register_subtitle),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Registration Card
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
                    // Email field
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
                        isError = viewModel.errorMessage?.contains("email", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                    // Password field
                    OutlinedTextField(
                        value = viewModel.passwordInput,
                        onValueChange = { viewModel.passwordInput = it },
                        label = {
                            Text(
                                stringResource(R.string.label_password),
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
                        visualTransformation = PasswordVisualTransformation(),
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
                        ),
                        singleLine = true,
                        isError = viewModel.errorMessage?.contains("password", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                    // Confirm Password field
                    OutlinedTextField(
                        value = viewModel.confirmPasswordInput,
                        onValueChange = { viewModel.confirmPasswordInput = it },
                        label = {
                            Text(
                                stringResource(R.string.label_confirm_password),
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
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.textfield_corner_radius)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        ),
                        singleLine = true,
                        isError = viewModel.errorMessage?.contains("konfirmasi", ignoreCase = true) == true ||
                                viewModel.errorMessage?.contains("cocok", ignoreCase = true) == true,
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

                    // Terms and Conditions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.spacer_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = acceptedTerms,
                            onCheckedChange = { acceptedTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
                        Text(
                            text = stringResource(R.string.agree_to_terms),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { /* Navigate to terms */ },
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacer_small))
                        ) {
                            Text(
                                text = stringResource(R.string.terms_of_service),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xlarge)))

                    // Register Button
                    Button(
                        onClick = {
                            if (acceptedTerms) {
                                viewModel.register(onSuccess = onRegisterSuccess)
                            } else {
                                viewModel.errorMessage = "Anda harus menyetujui Syarat & Ketentuan"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (acceptedTerms) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = dimensionResource(R.dimen.button_elevation),
                            pressedElevation = dimensionResource(R.dimen.spacer_small),
                            hoveredElevation = dimensionResource(R.dimen.button_elevation),
                            focusedElevation = dimensionResource(R.dimen.button_elevation),
                            disabledElevation = 0.dp
                        ),
                        enabled = acceptedTerms
                    ) {
                        Text(
                            text = stringResource(R.string.btn_register_submit),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterSuccessScreen(
    successMessage: String,
    onNavigateBack: () -> Unit
) {
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensionResource(R.dimen.card_elevation),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                        spotColor = SuccessColor.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xxlarge))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Success Icon dengan background gradient
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.logo_size_large))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(SuccessColor, SuccessColor.copy(alpha = 0.7f)),
                                    radius = 100f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_success),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_xxlarge))
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xlarge)))

                    // Title Success
                    Text(
                        text = "Registrasi Berhasil!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = SuccessColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // Success Message
                    Text(
                        text = successMessage,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xxlarge)))

                    // Tombol untuk kembali ke Login
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height_large)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = dimensionResource(R.dimen.button_elevation),
                            pressedElevation = dimensionResource(R.dimen.spacer_small)
                        )
                    ) {
                        Text(
                            text = "Kembali ke Login",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}