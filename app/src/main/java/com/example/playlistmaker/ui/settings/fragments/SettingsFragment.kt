package com.example.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {


    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply{
            setContent { settingsView() }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        settingsViewModel.getOnBackLiveData()
//        // обновление темы
//        binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
//        binding.simpleSwitch.setOnClickListener {
//            settingsViewModel.themeSwitch()
//            binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
//        }
//
//        //Поделиться
//        binding.ShareAppText.setOnClickListener {
//            settingsViewModel.shareApp()
//        }
//
//        //Написать в поддержку
//        binding.WriteSupportText.setOnClickListener {
//            settingsViewModel.writeSupport()
//        }
//
//        //share
//        binding.AgreementText.setOnClickListener {
//            settingsViewModel.readAgreement()
//        }
//    }

    @Composable
    @Preview
    fun settingsView() {
        val context = LocalContext.current
        val backgroundColor = ContextCompat.getColor(context, R.color.white)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(backgroundColor))
                .padding(
                    start = dimensionResource(id = R.dimen.left1)
                )
        ) {
            settingsText()
            darkTheme()
            share()
            support()
            agreement()
        }
    }

    @Composable
    fun settingsText() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        val customFont = FontFamily(Font(resId = R.font.ys_display_medium))
        Text(
            text = stringResource(id = R.string.SettingsString),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.settingsTopString14dp),
                    bottom = 0.dp,
                ),
            color = Color(textColor),
            fontFamily = customFont,
            fontSize = 22.sp
        )
    }

    @Composable
    fun darkTheme() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        val customFont = FontFamily(Font(resId = R.font.ys_display_regular))
        Box(Modifier.padding(top = 40.dp)) {
            Text(
                text = stringResource(id = R.string.BlackTheme),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.settingsTopString14dp),
                        bottom = 0.dp,
                    ),
                color = Color(textColor),
                fontFamily = customFont,
                fontSize = 22.sp
            )
            Switch(
                checked = false,
                onCheckedChange = {settingsViewModel.themeSwitch()},
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
            )
        }
    }

    @Composable
    fun share() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        val customFont = FontFamily(Font(resId = R.font.ys_display_regular))
        Box(Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(id = R.string.Share),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.settingsTopString14dp),
                        bottom = 0.dp,
                    ),
                color = Color(textColor),
                fontFamily = customFont,
                fontSize = 22.sp
            )
            Image(
                painter = painterResource(id = R.drawable.share),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp, 45.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }

    @Composable
    fun support() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        val customFont = FontFamily(Font(resId = R.font.ys_display_regular))
        Box(Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(id = R.string.Support),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.settingsTopString14dp),
                        bottom = 0.dp,
                    ),
                color = Color(textColor),
                fontFamily = customFont,
                fontSize = 22.sp
            )
            Image(
                painter = painterResource(id = R.drawable.girl),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp, 45.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }

    @Composable
    fun agreement() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        val customFont = FontFamily(Font(resId = R.font.ys_display_regular))
        Box(Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(id = R.string.Agreement),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.settingsTopString14dp),
                        bottom = 0.dp,
                    ),
                color = Color(textColor),
                fontFamily = customFont,
                fontSize = 22.sp
            )
            Image(
                painter = painterResource(id = R.drawable.go),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp, 27.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }
}