/**
    Copyright (C) 2023 Forrest Guice
    This file is part of SuntimesWidget.

    SuntimesWidget is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SuntimesWidget is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SuntimesWidget.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package com.forrestguice.suntimeswidget.alarmclock.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.forrestguice.suntimeswidget.HelpDialog;
import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.AboutActivity;
import com.forrestguice.suntimeswidget.SuntimesSettingsActivity;
import com.forrestguice.suntimeswidget.SuntimesUtils;
import com.forrestguice.suntimeswidget.alarmclock.AlarmClockItem;
import com.forrestguice.suntimeswidget.alarmclock.AlarmNotifications;
import com.forrestguice.suntimeswidget.settings.AppSettings;
import com.forrestguice.suntimeswidget.settings.SettingsActivityInterface;
import com.forrestguice.suntimeswidget.settings.SolarEvents;
import com.forrestguice.suntimeswidget.settings.WidgetSettings;
import com.forrestguice.suntimeswidget.settings.WidgetThemes;
import com.forrestguice.suntimeswidget.themes.SuntimesTheme;

/**
 * AlarmBedtimeActivity
 */
public class AlarmBedtimeActivity extends AppCompatActivity
{
    public static final String TAG = "BedtimeActivity";

    private static final String EXTRA_SHOWBACK = AlarmClockActivity.EXTRA_SHOWBACK;

    private static final int REQUEST_SETTINGS = 20;

    private static final String DIALOGTAG_HELP = "helpDialog";

    private AppSettings.LocaleInfo localeInfo;

    public AlarmBedtimeActivity() {
        super();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        Context context = AppSettings.initLocale(newBase, localeInfo = new AppSettings.LocaleInfo());
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate(Bundle savedState)
    {
        initTheme();
        super.onCreate(savedState);
        initLocale(this);
        setContentView(R.layout.layout_activity_bedtime);
        initViews(this);
        initWarnings(this, savedState);
        handleIntent(getIntent());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        registerReceiver(updateBroadcastReceiver, AlarmNotifications.getUpdateBroadcastIntentFilter());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        restoreDialogs();
        checkWarnings();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        unregisterReceiver(updateBroadcastReceiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_SETTINGS:
                onSettingsResult(resultCode, data);
                break;
        }
    }

    protected void initWarnings(Context context, Bundle savedState) {
        // TODO
    }
    protected void checkWarnings() {
        // TODO
    }
    protected void saveWarnings(Bundle outState) {
        // TODO
    }
    protected void restoreWarnings(@NonNull Bundle savedState) {
        // TODO
    }

    private String appTheme;
    private int appThemeResID;
    private SuntimesTheme appThemeOverride = null;

    private void initTheme()
    {
        appTheme = AppSettings.loadThemePref(this);
        appThemeResID = AppSettings.setTheme(this, appTheme);

        String themeName = AppSettings.getThemeOverride(this, appTheme);
        if (themeName != null && WidgetThemes.hasValue(themeName)) {
            Log.i("initTheme", "Overriding \"" + appTheme + "\" using: " + themeName);
            appThemeOverride = WidgetThemes.loadTheme(this, themeName);
        }
    }

    private final BroadcastReceiver updateBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Uri data = intent.getData();
            Log.d(TAG, "updateReceiver.onReceive: " + data + " :: " + action);

            if (action != null)
            {
                if (action.equals(AlarmNotifications.ACTION_UPDATE_UI)) {
                    if (data != null) {
                        onAlarmItemUpdated(ContentUris.parseId(data));

                    } else Log.e(TAG, "updateReceiver.onReceive: null data!");
                } else Log.e(TAG, "updateReceiver.onReceive: unrecognized action: " + action);
            } else Log.e(TAG, "updateReceiver.onReceive: null action!");
        }
    };

    protected void onAlarmItemUpdated(long alarmID)
    {
        // TODO
        //list.reloadAdapter((alarmID != -1 ? alarmID : null));
        //Log.d("DEBUG", "adapter reloaded: " + alarmID);
    }

    @Override
    public void onNewIntent( Intent intent )
    {
        super.onNewIntent(intent);
        Log.d("DEBUG", "new intent: " + intent);
        handleIntent(intent);
    }

    protected void handleIntent(Intent intent)
    {
        Context context = this;

        String param_action = intent.getAction();
        intent.setAction(null);

        Uri param_data = intent.getData();
        intent.setData(null);

        if (param_action != null)
        {
            // TODO
            /*if (param_action.equals(ACTION_SET_ALARM)) {
                handleIntent_setAlarm(context, intent);
            }*/

        } else {
            if (param_data != null) {
                // TODO
                //list.notifyAlarmUpdated(ContentUris.parseId(param_data));
            }
        }
    }

    @SuppressLint("ResourceType")
    private void initLocale(Context context)
    {
        WidgetSettings.initDefaults(context);
        WidgetSettings.initDisplayStrings(context);
        SuntimesUtils.initDisplayStrings(context);
        SolarEvents.initDisplayStrings(context);
        AlarmClockItem.AlarmType.initDisplayStrings(context);
        AlarmClockItem.AlarmTimeZone.initDisplayStrings(context);

        // TODO
        /*int[] attrs = { R.attr.alarmColorEnabled, android.R.attr.textColorPrimary, R.attr.text_disabledColor, R.attr.buttonPressColor, android.R.attr.textColor, R.attr.icActionNew, R.attr.icActionClose };
        TypedArray a = context.obtainStyledAttributes(attrs);
        colorAlarmEnabled = colorOn = ContextCompat.getColor(context, a.getResourceId(0, R.color.alarm_enabled_dark));
        a.recycle();*/

        if (appThemeOverride != null) {
            // TODO
            /*colorAlarmEnabled = colorOn = appThemeOverride.getAccentColor();
            colorPressed = appThemeOverride.getActionColor();*/
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState(outState);
        saveWarnings(outState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedState)
    {
        super.onRestoreInstanceState(savedState);
        restoreWarnings(savedState);
    }

    /**
     * initialize ui/views
     * @param context a context used to access resources
     */
    protected void initViews(Context context)
    {
        SuntimesUtils.initDisplayStrings(context);

        Toolbar menuBar = (Toolbar) findViewById(R.id.app_menubar);
        setSupportActionBar(menuBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            boolean showBack = getIntent().getBooleanExtra(EXTRA_SHOWBACK, false);
            if (!showBack) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_suntimes);   // TODO: "suntimes alarms" icon
            }
        }

        // TODO
    }

    protected void updateViews(Context context) {
    }

    protected void restoreDialogs()
    {
        /*FragmentManager fragments = getSupportFragmentManager();
        AlarmCreateDialog alarmCreateDialog = (AlarmCreateDialog) fragments.findFragmentById(R.id.createAlarmFragment);
        if (alarmCreateDialog != null) {
            alarmCreateDialog.setOnAcceptedListener(onAddAlarmAccepted);
            alarmCreateDialog.setOnCanceledListener(onAddAlarmCanceled);
            alarmCreateDialog.setOnNeutralListener(onAddAlarmNeutral);
        }*/
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bedtime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                showSettings();
                return true;

            case R.id.action_help:
                showHelp();
                return true;

            case R.id.action_about:
                showAbout();
                return true;

            case android.R.id.home:
                if (getIntent().getBooleanExtra(EXTRA_SHOWBACK, false))
                    onBackPressed();
                else onHomePressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onHomePressed()
    {
        Intent intent = new Intent(this, AlarmClockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_swap_in, R.anim.transition_swap_out);
    }

    @SuppressWarnings("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu)
    {
        SuntimesUtils.forceActionBarIcons(menu);
        return super.onPrepareOptionsPanel(view, menu);
    }

    protected void showSettings()
    {
        Intent settingsIntent = new Intent(this, SuntimesSettingsActivity.class);
        startActivityForResult(settingsIntent, REQUEST_SETTINGS);
        overridePendingTransition(R.anim.transition_next_in, R.anim.transition_next_out);
    }

    protected void showHelp()
    {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.setContent(getString(R.string.help_alarms_bedtime));
        helpDialog.show(getSupportFragmentManager(), DIALOGTAG_HELP);
    }

    protected void showAbout()
    {
        Intent about = new Intent(this, AboutActivity.class);
        about.putExtra(AboutActivity.EXTRA_ICONID, R.drawable.ic_suntimesalarms);
        startActivity(about);
        overridePendingTransition(R.anim.transition_next_in, R.anim.transition_next_out);
    }

    protected void onSettingsResult(int resultCode, Intent data)
    {
        boolean recreateActivity = (data != null && data.getBooleanExtra(SettingsActivityInterface.RECREATE_ACTIVITY, false));
        if (recreateActivity) {
            Handler handler = new Handler();
            handler.postDelayed(recreateRunnable, 0);    // post to end of execution queue (onResume must be allowed to finish before calling recreate)
        }
    }
    private final Runnable recreateRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                recreate();

            } else {
                finish();
                startActivity(getIntent());
            }
        }
    };

}
