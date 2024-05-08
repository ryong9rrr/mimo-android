package com.mimo.android.screens.firstsettingfunnels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mimo.android.FirstSettingFunnelsViewModel
import com.mimo.android.components.Button
import com.mimo.android.R

@Preview
@Composable
fun TestFunnel(
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel? = null
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Button(text = "first setting start", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_first_setting_start)
        })
        Button(text = "qr code scan", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_qr_code_scan)
        })
        Button(text = "hub find waiting", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_hub_find_waiting)
        })
        Button(text = "redirect_main_after_find_existing_hub", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_redirect_main_after_find_existing_hub)
        })
        Button(text = "redirect_location_register_after_find_new_hub", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_redirect_location_register_after_find_new_hub)
        })
        Button(text = "auto register location", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_auto_register_location)
        })
        Button(text = "make location alias", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_make_location_alias)
        })
        Button(text = "check entered hub info", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_check_entered_hub_info)
        })
        Button(text = "redirect_main_after_register_hub_and_location", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_redirect_main_after_register_hub_and_location)
        })
        Button(text = "first_setting_funnel_enter_location_to_register_hub", onClick = {
            firstSettingFunnelsViewModel?.updateCurrentStep(R.string.first_setting_funnel_enter_location_to_register_hub)
        })
    }
}