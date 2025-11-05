package com.sibtex.weather_android_test_app.presentation.shared

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.sibtex.weather_android_test_app.utils.extensions.dpToPx

class LocationPickerDialog(
    context: Context,
    private val initialLat: Double = 55.7569,
    private val initialLon: Double = 37.6151,
    private val onLocationSelected: (lat: Double, lon: Double) -> Unit
) : Dialog(context) {

    private var selectedLat: Double = initialLat
    private var selectedLon: Double = initialLon
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        
        setupDialog()
    }

    private fun setupDialog() {
        val container = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }

        val mainLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(context).apply {
            text = "Выберите местоположение"
            textSize = 20f
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        mainLayout.addView(title)

        webView = WebView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            ).apply {
                bottomMargin = 16
            }
            
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
            }
            
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    initializeMap()
                }
            }
            
            addJavascriptInterface(MapInterface(), "AndroidMap")
            loadDataWithBaseURL(
                null,
                getMapHtml(),
                "text/html",
                "UTF-8",
                null
            )
        }
        mainLayout.addView(webView)

        val buttonContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val greenColor = Color.parseColor("#4CAF50")
        val greenColorStateList = ColorStateList.valueOf(greenColor)
        
        val cancelButton = MaterialButton(context).apply {
            text = "Отмена"
            setTextColor(greenColor)
            backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            setStrokeColor(ColorStateList.valueOf(greenColor))
            strokeWidth = 2.dpToPx
            setAllCaps(false)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 8
            }
            setOnClickListener {
                dismiss()
            }
        }
        buttonContainer.addView(cancelButton)

        val confirmButton = MaterialButton(context).apply {
            text = "Выбрать"
            backgroundTintList = greenColorStateList // Зеленый фон
            setTextColor(Color.WHITE)
            setPadding(32, 24, 32, 24)
            setAllCaps(false)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 8
            }
            setOnClickListener {
                onLocationSelected(selectedLat, selectedLon)
                dismiss()
            }
        }
        buttonContainer.addView(confirmButton)

        mainLayout.addView(buttonContainer)
        container.addView(mainLayout)
        setContentView(container)

        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun initializeMap() {
        val js = """
            initializeMap($initialLat, $initialLon);
        """.trimIndent()
        webView?.evaluateJavascript(js, null)
    }

    private fun getMapHtml(): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body, html { margin: 0; padding: 0; height: 100%; }
                    #map { height: 100%; width: 100%; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map;
                    var marker;
                    var selectedLat = $initialLat;
                    var selectedLon = $initialLon;
                    
                    function initializeMap(lat, lon) {
                        selectedLat = lat;
                        selectedLon = lon;
                        
                        var map = L.map('map').setView([lat, lon], 10);
                        
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: '© OpenStreetMap contributors',
                            maxZoom: 19
                        }).addTo(map);
                        
                        marker = L.marker([lat, lon], { draggable: true }).addTo(map);
                        
                        marker.on('dragend', function(e) {
                            var position = marker.getLatLng();
                            selectedLat = position.lat;
                            selectedLon = position.lng;
                            if (window.AndroidMap) {
                                window.AndroidMap.onLocationChanged(selectedLat, selectedLon);
                            }
                        });
                        
                        map.on('click', function(e) {
                            selectedLat = e.latlng.lat;
                            selectedLon = e.latlng.lng;
                            marker.setLatLng([selectedLat, selectedLon]);
                            if (window.AndroidMap) {
                                window.AndroidMap.onLocationChanged(selectedLat, selectedLon);
                            }
                        });
                    }
                    
                    var link = document.createElement('link');
                    link.rel = 'stylesheet';
                    link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';
                    document.head.appendChild(link);
                    
                    var script = document.createElement('script');
                    script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
                    script.onload = function() {
                        initializeMap($initialLat, $initialLon);
                    };
                    document.head.appendChild(script);
                </script>
            </body>
            </html>
        """.trimIndent()
    }

    inner class MapInterface {
        @JavascriptInterface
        fun onLocationChanged(lat: Double, lon: Double) {
            selectedLat = lat
            selectedLon = lon
        }
    }

    override fun dismiss() {
        webView?.destroy()
        super.dismiss()
    }
}

