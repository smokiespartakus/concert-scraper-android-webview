package dk.bluesteel.concerts.kbh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

	private lateinit var webView: WebView
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout
	private var doubleBackToExitPressedOnce = false
	private val splashTimeOut = 3000L // 3 seconds

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		createWebView()
		handleSwipeRefresh()
		handleBackPressed()

	}

	private fun createWebView() {
		webView = findViewById(R.id.webview)
		webView.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
				val url = request?.url.toString()
				if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
					view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
					return true
				}
				return false
			}
			override fun onPageFinished(view: WebView?, url: String?) {
				super.onPageFinished(view, url)
				swipeRefreshLayout.isRefreshing = false // stop the refresh animation
			}
		}
		webView.getSettings().setDomStorageEnabled(true)
		webView.getSettings().setJavaScriptEnabled(true)
		webView.loadUrl("https://koncerterkbh.dk?platform=android");
		webView.getSettings().setAllowContentAccess(true)
		webView.getSettings().setAllowFileAccess(true)

		//		webView.setFocusableInTouchMode(true)
//		webView.setFocusable(true)
//		webView.setHapticFeedbackEnabled(true)
//		webView.setClickable(true)
//		webView.getSettings().setUseWideViewPort(true)
//		webView.getSettings().setLoadWithOverviewMode(true)
//		webView.requestFocus()

		//webView.addJavascriptInterface(new WebAppInterface(), "Android");

		//webView.getSettings().setAllowUniversalAccessFromFileURLs(true)
		//webView.getSettings().setPluginState(WebSettings.PluginState.ON)

		/*
webView.webChromeClient = object : WebChromeClient() {
	override fun onConsoleMessage(message: ConsoleMessage): Boolean {
		Log.d("MyApplication", "${message.message()} -- From line " +
				"${message.lineNumber()} of ${message.sourceId()}")
		Toast.makeText(applicationContext, message.message(), Toast.LENGTH_SHORT).show()
		return true
	}
}*/
	}

	private fun handleSwipeRefresh() {
		swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
		swipeRefreshLayout.setOnRefreshListener {
			webView.reload()
		}
	}

	private fun handleBackPressed() {
		val callback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (webView.canGoBack()) {
					webView.goBack()
				} else {
					if (doubleBackToExitPressedOnce) {
						finish()
					} else {
						doubleBackToExitPressedOnce = true
						Toast.makeText(applicationContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
						Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000) // reset flag after 2 seconds
					}
				}
			}
		}
		onBackPressedDispatcher.addCallback(this, callback)
	}
}