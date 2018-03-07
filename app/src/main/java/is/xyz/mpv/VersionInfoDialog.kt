package `is`.xyz.mpv

import android.content.Context
import android.util.AttributeSet
import android.preference.DialogPreference
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView

class VersionInfoDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
): DialogPreference(context, attrs, defStyleAttr, defStyleRes), LogObserver {
    private var versionText: String

    init {
        isPersistent = false
        dialogLayoutResource = R.layout.version_dialog

        versionText = "mpv-android ${BuildConfig.VERSION_NAME} / ${BuildConfig.VERSION_CODE} (${BuildConfig.BUILD_TYPE})\n"
        /* create mpv context to capture versions from log */
        MPVLib.create(context)
        MPVLib.init()
        MPVLib.addLogObserver(this)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        MPVLib.removeLogObserver(this)
        MPVLib.destroy()
        val field = view.findViewById<TextView>(R.id.info)
        field.text = versionText
        field.movementMethod = ScrollingMovementMethod()
    }

    override fun logMessage(prefix: String, level: Int, text: String) {
        if (prefix == "cplayer" && level == MPVLib.mpvLogLevel.MPV_LOG_LEVEL_V)
            versionText += text
    }
}