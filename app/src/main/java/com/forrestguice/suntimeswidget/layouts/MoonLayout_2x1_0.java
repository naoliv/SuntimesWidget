/**
   Copyright (C) 2018 Forrest Guice
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

package com.forrestguice.suntimeswidget.layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.SuntimesUtils;
import com.forrestguice.suntimeswidget.calculator.MoonPhase;
import com.forrestguice.suntimeswidget.calculator.SuntimesMoonData;
import com.forrestguice.suntimeswidget.settings.WidgetSettings;
import com.forrestguice.suntimeswidget.themes.SuntimesTheme;

public class MoonLayout_2x1_0 extends MoonLayout
{
    public MoonLayout_2x1_0()
    {
        super();
    }

    public MoonLayout_2x1_0(int layoutID)
    {
        this.layoutID = layoutID;
    }

    @Override
    public void initLayoutID()
    {
        this.layoutID = R.layout.layout_widget_moon_2x1_0;
    }

    @Override
    public void updateViews(Context context, int appWidgetId, RemoteViews views, SuntimesMoonData data)
    {
        super.updateViews(context, appWidgetId, views, data);
        boolean showSeconds = WidgetSettings.loadShowSecondsPref(context, appWidgetId);

        SuntimesUtils.TimeDisplayText riseString = utils.calendarTimeShortDisplayString(context, data.moonriseCalendarToday(), showSeconds);
        views.setTextViewText(R.id.text_time_moonrise, riseString.getValue());
        views.setTextViewText(R.id.text_time_moonrise_suffix, riseString.getSuffix());

        SuntimesUtils.TimeDisplayText setString = utils.calendarTimeShortDisplayString(context, data.moonsetCalendarToday(), showSeconds);
        views.setTextViewText(R.id.text_time_moonset, setString.getValue());
        views.setTextViewText(R.id.text_time_moonset_suffix, setString.getSuffix());

        //double illumination = data.getMoonIlluminationToday();
        //views.setTextViewText(R.id.text_time_set, "" + illumination);   // TODO: format illumination

        MoonPhase phase = data.getMoonPhaseToday();

    }

    private int timeColor = Color.WHITE;

    @Override
    public void themeViews(Context context, RemoteViews views, SuntimesTheme theme)
    {
        super.themeViews(context, views, theme);

        int moonriseColor = theme.getMoonriseTextColor();
        int suffixColor = theme.getTimeSuffixColor();
        views.setTextColor(R.id.text_time_moonrise_suffix, suffixColor);
        views.setTextColor(R.id.text_time_moonrise, moonriseColor);

        int moonsetColor = theme.getMoonsetTextColor();
        views.setTextColor(R.id.text_time_moonset_suffix, suffixColor);
        views.setTextColor(R.id.text_time_moonset, moonsetColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            float timeSize = theme.getTimeSizeSp();
            float suffSize = theme.getTimeSuffixSizeSp();

            views.setTextViewTextSize(R.id.text_time_moonrise_suffix, TypedValue.COMPLEX_UNIT_SP, suffSize);
            views.setTextViewTextSize(R.id.text_time_moonrise, TypedValue.COMPLEX_UNIT_SP, timeSize);

            views.setTextViewTextSize(R.id.text_time_moonset, TypedValue.COMPLEX_UNIT_SP, timeSize);
            views.setTextViewTextSize(R.id.text_time_moonset_suffix, TypedValue.COMPLEX_UNIT_SP, suffSize);
        }

        Bitmap moonriseIcon = SuntimesUtils.insetDrawableToBitmap(context, R.drawable.ic_moonrise, moonriseColor, moonriseColor, 0);
        views.setImageViewBitmap(R.id.icon_time_moonrise, moonriseIcon);

        Bitmap moonsetIcon = SuntimesUtils.insetDrawableToBitmap(context, R.drawable.ic_moonset, moonsetColor,moonsetColor, 0);
        views.setImageViewBitmap(R.id.icon_time_moonset, moonsetIcon);
    }

    @Override
    public void prepareForUpdate(SuntimesMoonData data)
    {
        // EMPTY
    }
}
