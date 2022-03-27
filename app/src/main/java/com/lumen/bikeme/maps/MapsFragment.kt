package com.lumen.bikeme.maps

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lumen.bikeme.R
import com.lumen.bikeme.databinding.MapsFragmentBinding
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.expressions.dsl.generated.switchCase
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.eq
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.literal
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ASSETS_GEOJSON = "asset://trips.geojson"
        const val GEOJSON_SOURCE_ID = "Trips"
        const val RED_LAYER_ID = "RedDexter"
        const val BLUE_LAYER_ID = "BlueDexter"
        const val BLUE_MARKER = "blue"
        const val RED_MARKER = "red"
        const val VISITED_IMG_MARKER = "VISITED"
        const val NOT_VISITED_IMG_MARKER = "NOT_VISITED"
        const val COLOR_PARAM = "color"
        const val START_MAP_LONG_PARAM = 19.4040232
        const val START_MAP_LAT_PARAM = 54.1696862
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MapsFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        setStartCameraPosition()
        loadAndShowGeoJsonMarkers()

        binding.mapLegend.btnVisited.setOnClickListener { btn ->
            toggleLayersVisibility(btn, BLUE_LAYER_ID)
        }
        binding.mapLegend.btnToVisit.setOnClickListener { btn ->
            toggleLayersVisibility(btn, RED_LAYER_ID)
        }
    }

    private fun loadAndShowGeoJsonMarkers() {

        binding.mapView.getMapboxMap().loadStyle(
            style(Style.OUTDOORS) {
                +image(VISITED_IMG_MARKER) {
                    bitmap(
                        loadBitmapAs888(R.drawable.blue_marker)
                    )
                }
                +image(NOT_VISITED_IMG_MARKER) {
                    bitmap(
                        loadBitmapAs888(R.drawable.red_marker)
                    )
                }
                +geoJsonSource(GEOJSON_SOURCE_ID) {
                    url(ASSETS_GEOJSON)
                }
                +symbolLayer(RED_LAYER_ID, GEOJSON_SOURCE_ID) {
                    iconImage(
                        switchCase {
                            eq {
                                get {
                                    literal(COLOR_PARAM)
                                }
                                literal(RED_MARKER)
                            }
                            literal(NOT_VISITED_IMG_MARKER)
                            literal("")
                        }
                    )
                    iconAllowOverlap(true)
                    iconAnchor(IconAnchor.BOTTOM)
                }
                +symbolLayer(BLUE_LAYER_ID, GEOJSON_SOURCE_ID) {
                    iconImage(VISITED_IMG_MARKER)
                    filter(
                        Expression.all(
                            eq(
                                get(COLOR_PARAM),
                                literal(BLUE_MARKER)
                            )
                        )
                    )
                }
            }
        )
    }

    private fun loadBitmapAs888(markerRes: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, markerRes)
            .copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun setStartCameraPosition() {
        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    START_MAP_LONG_PARAM,
                    START_MAP_LAT_PARAM
                )
            ).zoom(7.0).build()
        )
    }

    private fun toggleLayersVisibility(btn: View, layerId: String) {
        binding.mapView.getMapboxMap().getStyle {
            val blueLayerState = it.getLayer(layerId)?.visibility
            if (blueLayerState == Visibility.NONE) {
                it.getLayer(layerId)?.visibility(Visibility.VISIBLE)
                btn.alpha = 1f
            } else {
                it.getLayer(layerId)?.visibility(Visibility.NONE)
                btn.alpha = 0.5f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
