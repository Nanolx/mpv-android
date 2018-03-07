package is.xyz.mpv;

import android.content.Context;
import android.os.Build;
import android.preference.DialogPreference;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class VersionInfoDialog extends DialogPreference implements LogObserver {
    public VersionInfoDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    public VersionInfoDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public VersionInfoDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private String versionText;

    private void init(Context context) {
        setPersistent(false);
        setDialogLayoutResource(R.layout.version_dialog);

        versionText = "mpv-android " + BuildConfig.VERSION_NAME + " / " + BuildConfig.VERSION_CODE + " (" + BuildConfig.BUILD_TYPE + ")\n";
        MPVLib.create(context);
        MPVLib.init();
        MPVLib.addLogObserver(this);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        MPVLib.removeLogObserver(this);
        MPVLib.destroy();
        TextView field = (TextView) view.findViewById(R.id.info);
        field.setText(versionText);
        field.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void logMessage(@NotNull String prefix, int level, @NotNull String text) {
        if (prefix.equals("cplayer") && level == MPVLib.mpvLogLevel.MPV_LOG_LEVEL_V)
            versionText += text;
    }
}
