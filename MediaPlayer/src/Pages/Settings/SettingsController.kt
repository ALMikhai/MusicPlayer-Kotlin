package Pages.Settings

class SettingsController(){
    private lateinit var _stage : SettingsStage

    fun setMainModel(settingsStage : SettingsStage){
        _stage = settingsStage
    }

    public fun onApplyClick(){
        _stage.settings.mainMusicDirectory.set(_stage.directoryPathTextBox.text)
    }
}
