package com.example.myprojek.ui.view.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.myprojek.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    if (viewModel.isLoggedIn) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(R.drawable.bg_wave_top),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
                alpha = 0.8f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and Welcome
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_xxlarge))
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.logo_size_large))
                        .shadow(
                            elevation = dimensionResource(R.dimen.button_elevation),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
                        )
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_app_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(dimensionResource(R.dimen.logo_size_medium))
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                Text(
                    text = stringResource(R.string.welcome_back),
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
                    text = stringResource(R.string.welcome_subtitle),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Login Card
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                    // Password field with visibility toggle
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

                    // PERUBAHAN BESAR DI SINI:
                    // Daftar Akun Baru (KIRI) dan Lupa Password (KANAN) - SEJAJAR
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.spacer_small)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Daftar Akun Baru - DI KIRI
                        TextButton(
                            onClick = {
                                viewModel.clearFields()
                                onNavigateToRegister()
                            },
                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.btn_register_new_account),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Lupa Password - DI KANAN
                        TextButton(
                            onClick = onNavigateToForgot,
                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.text_forgot_password),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

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

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // Login Button
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
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
                            text = stringResource(R.string.btn_login),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Text(
                            text = stringResource(R.string.or_continue_with),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // Social Login Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacer_large))
                    ) {
                        // Google Button dengan warna brand
                        SocialLoginButton(
                            icon = R.drawable.ic_google,
                            text = "Google",
                            iconTint = Color.Unspecified,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Handle Google login
                        }

                        // Facebook Button dengan warna brand
                        SocialLoginButton(
                            icon = R.drawable.ic_facebook,
                            text = "Facebook",
                            iconTint = Color.Unspecified,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Handle Facebook login
                        }
                    }

                    // HAPUS bagian Register link yang lama di bawah
                    // Karena sudah dipindah ke atas
                }
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(dimensionResource(R.dimen.social_button_height))
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.social_button_corner_radius)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}