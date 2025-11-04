package com.winapp.deliverytrackingapp

import android.app.Application
import android.content.Context
import android.os.Environment
import java.io.File


class MyApplication : Application() {
    private var mInstance: MyApplication? = null

    object FILE {
        val EXTERNAL_FILE_PATH = "/PurchaseOrder"
    }
    object FILEGRA {
        val EXTERNAL_FILE_PATH_GRA = "/GoodsReceipt"
    }
    object FILESyncGRA {
        val EXTERNAL_FILE_PATH_GRASync = "/SyncGoodReceipt"
    }

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        val context: Context = applicationContext()

//        GlobalScope.launch {
//            val database = PDDatabase.getInstance(context = this@MyApplication)
//        }
//
//        EventBus.builder()


        val outputFile = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString() + FILE.EXTERNAL_FILE_PATH
        )
        if (!outputFile.exists())
            outputFile.mkdir()

        val outputFileGRA = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString() + FILEGRA.EXTERNAL_FILE_PATH_GRA
        )
        if (!outputFileGRA.exists())
            outputFileGRA.mkdir()

        val outputFileGRASync = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString() + FILESyncGRA.EXTERNAL_FILE_PATH_GRASync
        )
        if (!outputFileGRASync.exists())
            outputFileGRASync.mkdir()
    }

    @Synchronized
    fun getInstance(): MyApplication? {
        return mInstance
    }


}
