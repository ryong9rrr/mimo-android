package com.mimo.android.screens.firstsettingfunnels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mimo.android.FirstSettingFunnelsViewModel
import com.mimo.android.MainActivity
import com.mimo.android.QrCodeViewModel
import com.mimo.android.R
import com.mimo.android.UserLocation
import com.mimo.android.services.gogglelocation.RequestPermissionsUtil

private const val TAG = "FUNNEL_ROOT"

@Composable
fun FirstSettingFunnelsRoot(
    qrCodeViewModel: QrCodeViewModel,
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel,
    checkCameraPermission: () -> Unit,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
    context: Context
){
    FunnelMatcher(
            qrCodeViewModel = qrCodeViewModel,
            firstSettingFunnelsViewModel = firstSettingFunnelsViewModel,
            checkCameraPermission = checkCameraPermission,
            launchGoogleLocationAndAddress = launchGoogleLocationAndAddress,
            context = context
    )
}

@Composable
fun FunnelMatcher(
    qrCodeViewModel: QrCodeViewModel,
    firstSettingFunnelsViewModel: FirstSettingFunnelsViewModel,
    checkCameraPermission: () -> Unit,
    launchGoogleLocationAndAddress: (cb: (userLocation: UserLocation?) -> Unit) -> Unit,
    context: Context
){
    val firstSettingFunnelsUiState by firstSettingFunnelsViewModel.uiState.collectAsState()
    val qrCodeUiState by qrCodeViewModel.uiState.collectAsState()

    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_first_setting_start) {
        FunnelFirstSettingStart(
            checkCameraPermission = checkCameraPermission
        )
        return
    }

//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_qr_code_scan) {
//        FunnelQrCodeScan(
//            goPrev = {
//                firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_first_setting_start)
//            },
//            checkCameraPermission = checkCameraPermission
//        )
//        return
//    }

    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_hub_find_waiting) {
        FunnelHubFindWaiting(
            goNext = {
                val qrCode = qrCodeUiState.qrCode
                if (qrCode == null) {
                    Log.e(TAG, "QR CODE 없음...")
                    Toast.makeText(
                        context,
                        "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                    firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_first_setting_start)
                    return@FunnelHubFindWaiting
                }

                firstSettingFunnelsViewModel.setHubAndRedirect(
                    qrCode = qrCode,
                    launchGoogleLocationAndAddress = launchGoogleLocationAndAddress
                )
            }
        )
        return
    }

    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_redirect_main_after_find_existing_hub) {
        RedirectMainAfterFindExistingHub(
            hub = firstSettingFunnelsUiState.hub,
            goNext = {
                firstSettingFunnelsViewModel.redirectMain()
            },
            redirectAfterCatchError = {
                Toast.makeText(
                    context,
                    "다시 시도해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_first_setting_start)
            }
        )
        return
    }

//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_redirect_location_register_after_find_new_hub) {
//        RedirectLocationRegisterAfterFindNewHub {
//            launchGoogleLocationAndAddress { userLocation ->
//                firstSettingFunnelsViewModel.redirectAutoRegisterLocationFunnel(userLocation)
//            }
//        }
//        return
//    }

    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_auto_register_location) {
        val userLocation = firstSettingFunnelsUiState.userLocation
        val userAddress = userLocation?.address

        // 위치권한이 없거나 모종의 이유로 위치를 받아올 수 없었다면...
        if (userAddress == null) {
            Toast.makeText(
                context,
                "앱의 위치권한을 켜주세요",
                Toast.LENGTH_SHORT
            ).show()
            // 처음 화면으로 그냥 이동시키고
            firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_first_setting_start)
            // 다시 권한 묻기
            RequestPermissionsUtil(context).requestLocation()
            return
        }

        FunnelAutoRegisterLocation(
            location = userAddress,
            onConfirm = {
                // TODO: 그냥 여기서 바로 처리해버리기... 집 등록 API 호출하고 메인으로 이동
                val hubQrCode = qrCodeUiState.qrCode
                val address = firstSettingFunnelsUiState.userLocation?.address
                Log.i(TAG, "허브 ${hubQrCode}를 ${address} 에 등록완료!!")
                Toast.makeText(
                    MainActivity.getMainActivityContext(),
                    "허브와 거주지를 등록했어요. 메인화면으로 이동할게요.",
                    Toast.LENGTH_SHORT
                ).show()
                firstSettingFunnelsViewModel.redirectMain()
            }
        )
        return
    }

    // TODO: 처음부터 별칭짓기, 주소입력도 귀찮다 그냥 빨리빨리 등록시키자
//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_make_location_alias){
//        // TODO: manage state...
//        FunnelMakeLocationAlias(
//            location = "서울특별시 강남구 테헤란로 212",
//            goPrev = {},
//            onComplete = {}
//        )
//        return
//    }
//
//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_check_entered_hub_info) {
//        // TODO: manage state...
//        FunnelCheckEnteredHubInfo(
//            location = "서울특별시 강남구 테헤란로 212",
//            locationAlias = "서울특별시 집",
//            goPrev = {},
//            goStartScreen = {},
//            onConfirm = {}
//        )
//        return
//    }
//
//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_redirect_main_after_register_hub_and_location) {
//        // TODO: manage state...
//        RedirectMainAfterRegisterHubAndLocation(
//            locationAlias = "서울특별시 집",
//            location = "서울특별시 강남구 테헤란로 212",
//            goNext = {}
//        )
//        return
//    }
//
//    if (firstSettingFunnelsUiState.currentStepId == R.string.first_setting_funnel_enter_location_to_register_hub) {
//        // TODO: manage state...
//        // TODO: 근데 굳이 위치를 따로 입력받아야하나...
//        FunnelEnterLocationToRegisterHub(
//            goPrev = {
//                firstSettingFunnelsViewModel.updateCurrentStep(stepId = R.string.first_setting_funnel_auto_register_location)
//            },
//            onSelectLocation = {
//                // TODO
//            }
//        )
//    }
}