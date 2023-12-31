package com.bacancy.ccs2androidhmi.util

import android.util.Log
import com.bacancy.ccs2androidhmi.util.ModbusTypeConverter.toHex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream

object ReadWriteUtil {

    suspend fun writeToSingleHoldingRegisterNew(
        mOutputStream: OutputStream?,
        mInputStream: InputStream?,
        startAddress: Int,
        regValue: Int,
        onAuthDataReceived: (ByteArray) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val writeRequestFrame: ByteArray =
                ModBusUtils.createWriteSingleRegisterRequest(startAddress, regValue)
            mOutputStream?.write(writeRequestFrame)

            val writeResponseFrame = ByteArray(16)
            val size: Int = mInputStream?.read(writeResponseFrame) ?: 0

            if (size > 0) {
                val readRequestFrame: ByteArray =
                    ModBusUtils.createReadHoldingRegistersRequest(startAddress, 1)
                mOutputStream?.write(readRequestFrame)

                val readResponseFrame = ByteArray(64)
                val size1: Int = mInputStream?.read(readResponseFrame) ?: 0

                if (size1 > 0) {
                    onAuthDataReceived(readResponseFrame)
                }
            }
        }
    }

    suspend fun writeRequestAndReadResponse(
        mOutputStream: OutputStream?, mInputStream: InputStream?, responseSize: Int,
        requestFrame: ByteArray, onDataReceived: (ByteArray) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val bufferedInputStream = BufferedInputStream(mInputStream)
                val bufferedOutputStream = BufferedOutputStream(mOutputStream)
                withTimeoutOrNull(1000) {
                    try {
                        withContext(Dispatchers.IO) {
                            Log.w(
                                "MONTAG",
                                "writeRequestAndReadResponse: Calling write from Normal State"
                            )
                            bufferedOutputStream.write(requestFrame)
                            bufferedOutputStream.flush()
                        }
                    } catch (e: TimeoutCancellationException) {
                        e.printStackTrace()
                        withContext(Dispatchers.IO) {
                            Log.w(
                                "MONTAG",
                                "writeRequestAndReadResponse: Calling write from Timeout State"
                            )
                            bufferedOutputStream.write(requestFrame)
                            bufferedOutputStream.flush()
                        }
                    }
                }

                val responseFrame = ByteArray(responseSize)

                delay(500) //waiting for 500ms between write and read

                bufferedInputStream.mark(0)
                val size = bufferedInputStream.read(responseFrame)
                Log.i(
                    "MONTAG",
                    "writeRequestAndReadResponse: RESPONSE FRAME = ${responseFrame.toHex()}"
                )
                if (size > 0 && isValidResponse(responseFrame)) {
                    onDataReceived(responseFrame)
                } else {
                    bufferedInputStream.reset()
                    Log.e(
                        "TAG",
                        "writeRequestAndReadResponse: Error Frame - ${responseFrame.toHex()}"
                    )
                }

            } catch (e: Exception) {
                Log.e("TAG", "writeRequestAndReadResponse: In Catch - ${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    }

    suspend fun readAndWriteRegisters(
        mOutputStream: OutputStream?, mInputStream: InputStream?, responseSize: Int,
        requestFrame: ByteArray, onDataReceived: (ByteArray) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val bufferedInputStream = BufferedInputStream(mInputStream)
                val bufferedOutputStream = BufferedOutputStream(mOutputStream)

                bufferedOutputStream.write(requestFrame)
                bufferedOutputStream.flush()

                val responseFrame = ByteArray(responseSize)

                delay(500) //waiting for 500ms between write and read

                bufferedInputStream.mark(0)
                val size = bufferedInputStream.read(responseFrame)

                if (size > 0 && isValidResponse(responseFrame)) {
                    onDataReceived(responseFrame)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isValidResponse(responseFrame: ByteArray): Boolean {
        val hexResponse = responseFrame.toHex()
        return hexResponse.startsWith(ModBusUtils.HOLDING_REGISTERS_CORRECT_RESPONSE_BITS) ||
                hexResponse.startsWith(ModBusUtils.INPUT_REGISTERS_CORRECT_RESPONSE_BITS)
    }

}