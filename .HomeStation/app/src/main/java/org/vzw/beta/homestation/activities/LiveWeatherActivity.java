package org.vzw.beta.homestation.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.tools.PreferencesHelper;
import org.vzw.beta.homestation.tools.RootActivity;

/**
 * Created by user109 on 22/03/2016.
 */
public class LiveWeatherActivity extends RootActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_weather_layout);
        webView=(WebView)findViewById(R.id.webView_weather);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header')[0].style.display='none'; " +
                        "})()");
            }
        });
        webView.loadUrl("http://www.accuweather.com/en/" + PreferencesHelper.COUNTRY_CODE + "/national/satellite");
//        webView.loadUrl("http://weerdata.weerslag.nl/image/1.0/?size=nl-Motregen-550x550&type=Freecontent&v=54098902");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }


}
