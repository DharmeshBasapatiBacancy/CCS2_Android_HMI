package com.bacancy.ccs2androidhmi.util

object StateAndModesUtils {
    enum class GunChargingState(val stateValue: Int, val description: String) {
        UNPLUGGED(0, "Unplugged"),
        PLUGGED_WAITING(1, "Plugged In & Waiting for Authentication"),
        PLUGGED_AUTHENTICATING(2, "Plugged In & Waiting for Authentication"),
        AUTHENTICATION_SUCCESS(3, "Authentication Success"),
        PREPARING_FOR_CHARGING(4, "Preparing For Charging"),
        CHARGING(5, "Charging"),
        COMPLETE(6, "Complete"),
        COMMUNICATION_ERROR(7, "Communication Error"),
        AUTHENTICATION_TIMEOUT(8, "Authentication Timeout"),
        PLC_FAULT(9, "PLC Fault"),
        RECTIFIER_FAULT(10, "Rectifier Fault"),
        EMERGENCY_STOP(11, "Emergency Stop"),
        AUTHENTICATION_DENIED(12, "Authentication Denied"),
        PRECHARGE_FAIL(13, "Precharge Fail"),
        ISOLATION_FAIL(14, "Isolation Fail"),
        TEMPERATURE_FAULT(15, "Temperature Fault"),
        SPD_FAULT(16, "SPD Fault"),
        SMOKE_FAULT(17, "Smoke Fault"),
        TAMPER_FAULT(18, "Tamper Fault"),
        MAINS_FAIL(19, "Mains Fail"),
        UNAVAILABLE(20, "Unavailable"),
        RESERVED(21, "Reserved"),
        LOADING(-1, "Loading...");

        companion object {
            fun fromStateValue(value: Int): GunChargingState {
                return values().firstOrNull { it.stateValue == value } ?: LOADING
            }
        }
    }

    enum class SessionEndReasons(val stateValue: Int, val description: String) {
        REMOTE_STOP(1, "Remote Stop"),
        LOCAL_STOP(2, "Local Stop"),
        EV_REQUEST_STOP(3, "EV Request Stop"),
        EMERGENCY_STOP(4, "Emergency Stop"),
        FAULT_STOP(5, "Fault Stop"),
        UNKNOWN_STOP(-1, "Unknown Stop");

        companion object {
            // Function to get StopType from a numerical value
            fun fromStateValue(value: Int): SessionEndReasons {
                return values().firstOrNull { it.stateValue == value } ?: UNKNOWN_STOP
            }
        }
    }

    fun checkServerConnectedWith(array: CharArray): String {
        val firstOneIndex = array.indexOf('1')
        return when {
            firstOneIndex == 0 -> "Ethernet"
            firstOneIndex == 1 -> "GSM"
            firstOneIndex == 2 -> "Wifi"
            array.contains('1') -> "Unknown"
            else -> "Not Found"
        }
    }

    fun checkIfEthernetIsConnected(array: CharArray): String {
        return when {
            array.isNotEmpty() && array[0] == '1' -> "Connected"
            array.isEmpty() -> "Not Connected"
            else -> "Not Connected"
        }
    }

    fun checkGSMNetworkStrength(array: CharArray): String {
        return when (val firstOneIndex = array.indexOf('1')) {
            in 0..3 -> (firstOneIndex + 1).toString()
            else -> "0"
        }
    }

    fun checkWifiNetworkStrength(array: CharArray): String {
        return when (val firstOneIndex = array.indexOf('1')) {
            in 0..3 -> (firstOneIndex + 1).toString()
            else -> "0"
        }
    }

}