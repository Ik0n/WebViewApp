package ru.ikon.webviewapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import ru.ikon.webviewapp.databinding.FragmentNewsBinding
import ru.ikon.webviewapp.databinding.FragmentWebViewBinding

class WebViewFragment: Fragment(), OnBackPressed {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setOnBackPressedListener(this)

        webView = binding.webview;
        webView.webViewClient = WebViewClient();
        var webSettings = webView.settings;
        webSettings.javaScriptEnabled = true;
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(context?.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)?.getString(
                APP_PREFERENCES_SITE, "")!!)
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val mWebSettings = webSettings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.databaseEnabled = true
        mWebSettings.setSupportZoom(false)
        mWebSettings.allowFileAccess = true
        mWebSettings.allowContentAccess = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true

    }

    override fun onClick() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }
}