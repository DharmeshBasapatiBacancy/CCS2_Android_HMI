package com.bacancy.ccs2androidhmi.repository

import androidx.lifecycle.LiveData
import com.bacancy.ccs2androidhmi.db.AppDatabase
import com.bacancy.ccs2androidhmi.db.entity.TbChargingHistory
import com.bacancy.ccs2androidhmi.db.entity.TbAcMeterInfo
import com.bacancy.ccs2androidhmi.db.entity.TbGunsChargingInfo
import com.bacancy.ccs2androidhmi.db.entity.TbGunsDcMeterInfo
import com.bacancy.ccs2androidhmi.db.entity.TbGunsLastChargingSummary
import com.bacancy.ccs2androidhmi.db.entity.TbMiscInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun insertChargingSummary(chargingSummary: TbChargingHistory){
        appDatabase.appDao().insertSummary(chargingSummary)
    }

    suspend fun getAllChargingSummaries(): List<TbChargingHistory> {
        return appDatabase.appDao().getAllChargingSummaries()
    }

    suspend fun getGunsChargingHistory(gunNumber: Int): List<TbChargingHistory> {
        return appDatabase.appDao().getGunsChargingHistory(gunNumber)
    }

    suspend fun insertAcMeterInfo(acMeterInfo: TbAcMeterInfo){
        appDatabase.appDao().insertAcMeterInfo(acMeterInfo)
    }

    fun getLatestAcMeterInfo(): LiveData<TbAcMeterInfo> {
        return appDatabase.appDao().getLatestAcMeterInfo()
    }

    suspend fun insertMiscInfo(tbMiscInfo: TbMiscInfo){
        appDatabase.appDao().insertMiscInfo(tbMiscInfo)
    }

    fun getLatestMiscInfo(): LiveData<TbMiscInfo> {
        return appDatabase.appDao().getLatestMiscInfo()
    }

    suspend fun insertGunsChargingInfo(tbGunsChargingInfo: TbGunsChargingInfo){
        appDatabase.appDao().insertGunChargingInfo(tbGunsChargingInfo)
    }

    fun getGunsChargingInfoByGunNumber(gunNumber: Int): LiveData<TbGunsChargingInfo>{
        return appDatabase.appDao().getGunsChargingInfo(gunNumber)
    }
    suspend fun insertGunsDCMeterInfo(tbGunsDcMeterInfo: TbGunsDcMeterInfo){
        appDatabase.appDao().insertGunsDCMeterInfo(tbGunsDcMeterInfo)
    }

    fun getGunsDCMeterInfoByGunNumber(gunNumber: Int): Flow<TbGunsDcMeterInfo?> {
        return appDatabase.appDao().getGunsDCMeterInfo(gunNumber)
    }

    suspend fun insertGunsLastChargingSummary(tbGunsLastChargingSummary: TbGunsLastChargingSummary){
        appDatabase.appDao().insertGunsLastChargingSummary(tbGunsLastChargingSummary)
    }

    fun getGunsLastChargingSummary(gunNumber: Int): LiveData<TbGunsLastChargingSummary>{
        return appDatabase.appDao().getGunsLastChargingSummary(gunNumber)
    }


}