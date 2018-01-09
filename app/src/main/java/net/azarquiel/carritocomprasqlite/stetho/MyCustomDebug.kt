package net.azarquiel.carritocomprasqlite.stetho

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by alberto on 06/11/2017.
 */
class MyCustomDebug : Application() {
    override fun onCreate() {
        super.onCreate()
        // depura almacenamiento local BD, share, etc
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build())
    }
}
