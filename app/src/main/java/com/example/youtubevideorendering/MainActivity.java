package com.example.youtubevideorendering;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String Video_ID = "y6N8u4OGgXk";
    private WebView webView;
    private FrameLayout frameLayout;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle window insets for better layout handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find WebView and configure it
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyCustomView());
        webView.setWebViewClient(new WebViewClient());

        // Create an iframe to load the YouTube video
        String iframe = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + Video_ID + "\" frameborder=\"0\" allowfullscreen></iframe>";
        webView.loadData(iframe, "text/html", "utf-8");
    }

    private class MyCustomView extends WebChromeClient {
        @Override
        public void onHideCustomView() {
            // When exiting fullscreen mode
            if (customView != null) {
                FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
                decorView.removeView(customView);
                customView = null;
                customViewCallback.onCustomViewHidden();
                webView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // When entering fullscreen mode
            if (customView != null) {
                onHideCustomView();
                return;
            }

            // Store the incoming custom view and callback
            customView = view;
            customViewCallback = callback;

            FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
            decorView.addView(customView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            webView.setVisibility(View.GONE);  // Hide WebView when fullscreen is active
        }
    }
}
