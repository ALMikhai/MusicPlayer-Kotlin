package Pages.Settings

import src.MusicDBController

class SettingsController(){
    private lateinit var _stage : SettingsStage

    fun setMainModel(settingsStage : SettingsStage){
        _stage = settingsStage
    }

    public fun onApplyClick(){
        if (_stage.settings.mainMusicDirectory.get() == _stage.directoryPathLabel.text)
            return

        _stage.settings.mainMusicDirectory.set(_stage.directoryPathLabel.text)
        val dbController = MusicDBController()
        dbController.deleteAll()
        _stage.settings.updateDBForNewFolder(dbController)
    }
}
