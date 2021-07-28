/**
   Copyright (C) 2014-2021 Forrest Guice
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
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.SuntimesUtils;
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetData;
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetData2;
import com.forrestguice.suntimeswidget.settings.WidgetSettings;
import com.forrestguice.suntimeswidget.themes.SuntimesTheme;

import java.util.Calendar;

/**
 * A 1x1 layout that displays both the sunrise and sunset time.
 */
public class SunLayout_1x1_0 extends SunLayout
{
    protected int sunriseIconColor = Color.YELLOW;
    protected int sunriseIconStrokeColor = Color.YELLOW;
    protected int sunriseIconStrokePixels = 0;

    protected int sunsetIconColor = Color.YELLOW;
    protected int sunsetIconStrokeColor = Color.YELLOW;
    protected int sunsetIconStrokePixels = 0;

    public SunLayout_1x1_0()
    {
        super();
    }

    public SunLayout_1x1_0(int layoutID )
    {
        this.layoutID = layoutID;
    }

    @Override
    public void initLayoutID()
    {
        this.layoutID = R.layout.layout_widget_1x1_0;
    }

    private WidgetSettings.RiseSetOrder order = WidgetSettings.RiseSetOrder.TODAY;

    @Override
    public void prepareForUpdate(Context context, int appWidgetID, SuntimesRiseSetData data)
    {
        order = WidgetSettings.loadRiseSetOrderPref(context, appWidgetID);
        this.layoutID = chooseSunLayout(R.layout.layout_widget_1x1_0, R.layout.layout_widget_1x1_01, data, order);
    }

    @Override
    public void updateViews(Context context, int appWidgetId, RemoteViews views, SuntimesRiseSetData data)
    {
        super.updateViews(context, appWidgetId, views, data);
        boolean showSeconds = WidgetSettings.loadShowSecondsPref(context, appWidgetId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            if (WidgetSettings.loadScaleTextPref(context, appWidgetId))
            {
                int[] maxDp = new int[] {(maxDimensionsDp[0] - (2 * paddingDp[0]) - (int)Math.ceil(iconSizeDp)), (maxDimensionsDp[1] - (2 * paddingDp[1]))};
                float[] adjustedSizeSp = adjustTextSize(context, maxDp, paddingDp, "sans-serif", boldTime, (showSeconds ? "00:00:00" : "00:00"), timeSizeSp, ClockLayout.CLOCKFACE_MAX_SP, "MM", suffixSizeSp, iconSizeDp);
                if (adjustedSizeSp[0] > timeSizeSp)
                {
                    float textScale = Math.max(adjustedSizeSp[0] / timeSizeSp, 1);
                    float scaledPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textScale * 2, context.getResources().getDisplayMetrics());

                    views.setTextViewTextSize(R.id.text_time_rise, TypedValue.COMPLEX_UNIT_DIP, adjustedSizeSp[0]);
                    views.setTextViewTextSize(R.id.text_time_rise_suffix, TypedValue.COMPLEX_UNIT_DIP, adjustedSizeSp[1]);

                    views.setTextViewTextSize(R.id.text_time_set, TypedValue.COMPLEX_UNIT_DIP, adjustedSizeSp[0]);
                    views.setTextViewTextSize(R.id.text_time_set_suffix, TypedValue.COMPLEX_UNIT_DIP, adjustedSizeSp[1]);

                    views.setViewPadding(R.id.text_time_set, 0, 0, (int)scaledPadding/2, 0);
                    views.setViewPadding(R.id.text_time_set_suffix, 0, 0, (int)scaledPadding, 0);
                    views.setViewPadding(R.id.icon_time_sunset, (int)(scaledPadding), 0, 0, 0);

                    views.setViewPadding(R.id.text_time_rise, 0, 0, (int)scaledPadding/2, 0);
                    views.setViewPadding(R.id.text_time_rise_suffix, 0, 0, (int)scaledPadding, 0);
                    views.setViewPadding(R.id.icon_time_sunrise, (int)(scaledPadding), 0, 0, 0);

                    Drawable sunriseIcon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.svg_sunrise1, null);
                    SuntimesUtils.tintDrawable(sunriseIcon, sunriseIconColor);
                    views.setImageViewBitmap(R.id.icon_time_sunrise, SuntimesUtils.drawableToBitmap(context, sunriseIcon, (int)(iconSizeDp * textScale), (int)(iconSizeDp * textScale) / 2, false));

                    Drawable sunsetIcon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.svg_sunset1, null);
                    SuntimesUtils.tintDrawable(sunsetIcon, sunsetIconColor);
                    views.setImageViewBitmap(R.id.icon_time_sunset, SuntimesUtils.drawableToBitmap(context, sunsetIcon, (int)(iconSizeDp * textScale), (int)(iconSizeDp * textScale) / 2, false));
                }
            }
        }

        updateViewsSunRiseSetText(context, views, data, showSeconds, order);
    }

    @Override
    public void themeViews(Context context, RemoteViews views, SuntimesTheme theme)
    {
        super.themeViews(context, views, theme);

        iconSizeDp = 18;   // override 32
        int suffixColor = theme.getTimeSuffixColor();
        views.setTextColor(R.id.text_time_rise_suffix, suffixColor);
        views.setTextColor(R.id.text_time_rise, theme.getSunriseTextColor());
        views.setTextColor(R.id.text_time_set_suffix, suffixColor);
        views.setTextColor(R.id.text_time_set, theme.getSunsetTextColor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            timeSizeSp = theme.getTimeSizeSp();
            suffixSizeSp = theme.getTimeSuffixSizeSp();

            views.setTextViewTextSize(R.id.text_time_rise_suffix, TypedValue.COMPLEX_UNIT_DIP, suffixSizeSp);
            views.setTextViewTextSize(R.id.text_time_rise, TypedValue.COMPLEX_UNIT_DIP, timeSizeSp);

            views.setTextViewTextSize(R.id.text_time_set, TypedValue.COMPLEX_UNIT_DIP, timeSizeSp);
            views.setTextViewTextSize(R.id.text_time_set_suffix, TypedValue.COMPLEX_UNIT_DIP, suffixSizeSp);
        }

        sunriseIconColor = theme.getSunriseIconColor();
        sunriseIconStrokeColor = theme.getSunriseIconStrokeColor();
        sunriseIconStrokePixels = theme.getSunriseIconStrokePixels(context);

        sunsetIconColor = theme.getSunsetIconColor();
        sunsetIconStrokeColor = theme.getSunsetIconStrokeColor();
        sunsetIconStrokePixels = theme.getSunsetIconStrokePixels(context);

        Bitmap sunriseIcon = SuntimesUtils.layerDrawableToBitmap(context, R.drawable.ic_sunrise0, sunriseIconColor, sunriseIconStrokeColor, sunriseIconStrokePixels);
        views.setImageViewBitmap(R.id.icon_time_sunrise, sunriseIcon);

        Bitmap sunsetIcon = SuntimesUtils.layerDrawableToBitmap(context, R.drawable.ic_sunset0, sunsetIconColor, sunsetIconStrokeColor, sunsetIconStrokePixels);
        views.setImageViewBitmap(R.id.icon_time_sunset, sunsetIcon);
    }
}
