package com.demo.recipelist

import android.app.Application
import com.demo.recipelist.data.AppContainer
import com.demo.recipelist.data.AppDataContainer

class RecipeListApplication: Application() {

    /*
    The Application class is the base class for maintaining global application state.
    It contains all other components such as activities and services.
    The Application class, or any subclass of the Application class, is instantiated
    before any other class when the process for your application/package is created.
    This class is primarily used for initialization of global state before the first
    Activity is displayed.
    Application Class 屬於 singleton pattern，保證 class 僅有一個實例存在，並且可以全局使用，
    所以在不同的 Activity 、 Service 中獲得的都是同一個實例。
    Application class 以及他的子類會在其他 class 之前先被實體化，主要用來初始化全域狀態、保存靜態
    變數，進行如資料傳遞、資料共享等操作。
    */

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}