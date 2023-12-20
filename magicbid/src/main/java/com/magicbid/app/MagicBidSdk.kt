package com.magicbid.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.Date

@Suppress("DEPRECATION")
class MagicBidSdk(private var context: Context) {
    private lateinit var sortedAdsList: MutableList<Adscode>
    private val result = Prefs.getResponseAll(context)
    private var currentAddPosition = 0
    private var isOpen = false
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var adView: AdView? = null
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    private val date = Date()
    private val currentdate = formatter.format(date)
    private var adidinterstital: Int = 0
    private var magic :Boolean = false
    var adFailedCount = 0


    private var ipAddress ="0.0.0.0"
    private lateinit var listnerInterface:AdListnerInterface
    var mInterstitialAd: InterstitialAd? = null

    fun adaptiveBannerAD(activity: Activity, linearLayout: LinearLayout) {
        if (result != null) {
            try {
                val adsList = result.filter { it.ads_type == 1 }
                sortedAdsList = adsList.sortedByDescending { it.cpm }.toMutableList()
                if (sortedAdsList.isNotEmpty()) {
                    loadadaptiveBannerAdd(activity, linearLayout, sortedAdsList[currentAddPosition].adscode,sortedAdsList[currentAddPosition].ads_id)
                }
            } catch (e: Exception) {
                Log.d("magick bidSDK",e.toString())
            }
        }
    }

   private fun loadadaptiveBannerAdd(activity: Activity, linearLayout: LinearLayout, adId: String, adsId: Int) {
        adView = AdView(activity)
        adView!!.adUnitId = adId
        linearLayout.removeAllViews()
        linearLayout.addView(adView)
        val adSize = getAdSizeaptiveBannerAdd(activity, linearLayout)
        adView!!.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {

                postData(adsId)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    // Refresh the ad after 5 seconds

                      adaptiveBannerAD(activity, linearLayout)

                }, 15000) // 5000 milliseconds = 5 seconds

            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (adError.code == 3) {
                    adFailedCount++
                    Log.d("adFailedCount","Ad loading failed $adFailedCount times")
                    if (sortedAdsList.size-1 > currentAddPosition){
                        currentAddPosition++
                        loadadaptiveBannerAdd(
                            activity,
                            linearLayout,
                            sortedAdsList[currentAddPosition].adscode,
                            sortedAdsList[currentAddPosition].ads_id
                        )
                    }

                }
            }

            override fun onAdClicked() {

            }
        }
    }



    private fun getAdSizeaptiveBannerAdd(activity: Activity, linearLayout: LinearLayout): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels = linearLayout.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }


    fun inlineBannerAD(activity: Activity, linearLayout: LinearLayout) {
        if (result != null) {
            try {
                val adsList = result.filter { it.ads_type == 1 }
                sortedAdsList = adsList.sortedByDescending { it.cpm }.toMutableList()
                if (sortedAdsList.isNotEmpty()) {
                    inlineloadAdd(activity, linearLayout, sortedAdsList[currentAddPosition].adscode,sortedAdsList[currentAddPosition].ads_id)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun inlineloadAdd(
        activity: Activity,
        linearLayout: LinearLayout,
        adId: String,
        adsId: Int
    ) {
        adView = AdView(activity)
        adView!!.adUnitId = adId
        linearLayout.removeAllViews()
        linearLayout.addView(adView)
        val adSize = inlinegetAdSize(activity, linearLayout)
        adView!!.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {



                postData(adsId)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    // Refresh the ad after 5 seconds

                    inlineBannerAD(activity, linearLayout)

                }, 15000)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (adError.code == 3) {
                    adFailedCount++
                    Log.d("adFailedCount","Ad loading failed $adFailedCount times")
                    if (sortedAdsList.size-1 > currentAddPosition){
                        currentAddPosition++
                        inlineloadAdd(activity, linearLayout, sortedAdsList[currentAddPosition].adscode,sortedAdsList[currentAddPosition].ads_id)

                    }
                }
            }

            override fun onAdClicked() {

            }
        }
    }

    fun inlinegetAdSize(activity: Activity, linearLayout: LinearLayout): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels = linearLayout.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(activity, adWidth)
    }


    fun showinterStitalad(listnerInterface1: AdListnerInterface) {
        listnerInterface = listnerInterface1
        if (result != null) {
            val adsList = result.filter { it.ads_type == 3 }
             sortedAdsList = adsList.sortedByDescending { it.cpm }.toMutableList()

            if (sortedAdsList.isNotEmpty()) {

                loadinterstitalad(sortedAdsList[currentAddPosition].adscode, listnerInterface,sortedAdsList[currentAddPosition].ads_id)
            }
        }
    }


    private fun loadinterstitalad(adscode: String, listnerInterface: AdListnerInterface, adsId: Int) {


        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adscode, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                listnerInterface.onAdFailedToLoad(adError)
                if (adError.code == 3) {
                    // currentAddPosition++
                    adFailedCount++
                    Log.d("adFailedCount","Ad loading failed $adFailedCount times")
                    if (sortedAdsList.size-1 > currentAddPosition){
                        currentAddPosition++
                        loadinterstitalad(
                            sortedAdsList[currentAddPosition].adscode,
                            listnerInterface,
                            sortedAdsList[currentAddPosition].ads_id
                        )
                    } else{

                        if (!magic){
                            magic = true
                            currentAddPosition = 0
                            loadinterstitalad(
                                sortedAdsList[currentAddPosition].adscode,
                                listnerInterface,
                                sortedAdsList[currentAddPosition].ads_id
                            )

                        }


                    }


                }
            }


            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd

               // if (mInterstitialAd != null) {



//                      mInterstitialAd?.show(context as Activity)

               // }
                adidinterstital = adsId

                mInterstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            listnerInterface.onAdClicked()
                        }

                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            listnerInterface.onAdDismissedFullScreenContent()

                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            mInterstitialAd = null
                            listnerInterface.onAdFailedToShowFullScreenContent(adError)
                        }

                        override fun onAdImpression() {
                            listnerInterface.onAdImpression()
                        }

                        override fun onAdShowedFullScreenContent() {
                            listnerInterface.onAdShowedFullScreenContent()
                        }
                    }


                listnerInterface.onAdLoaded(boolean = true)


            }

        })


    }

    fun setAutoAdCacheing(): Boolean {
        return mInterstitialAd != null
    }

    fun showInterstitialAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(context as Activity)
            postData(adidinterstital)
        }
    }

    fun showNativeAds(context: Context, view: TemplateView) {
        if (result != null) {
            val adsList = result.filter { it.ads_type == 4 }
            sortedAdsList = adsList.sortedByDescending { it.cpm }.toMutableList()

            if (sortedAdsList.isNotEmpty()) {
                loadnativead(context, view, sortedAdsList[currentAddPosition].adscode,sortedAdsList[currentAddPosition].ads_id)
            }
        }
    }

    private fun loadnativead(context: Context, view: TemplateView, adscode: String, adsId: Int) {
        val adLoader: AdLoader = AdLoader.Builder(this.context, adscode).forNativeAd {
//                val styles =
//                    NativeTemplateStyle.Builder().withMainBackgroundColor(context.resources.getColor(R.color.white)).build()
//                val template: TemplateView = findViewById(R.id.my_template)
//                view.setStyles(styles)
            view.setNativeAd(it)
            view.visibility = View.VISIBLE
            postData(adsId)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Refresh the ad after 5 seconds

                showNativeAds(context, view)

            }, 15000)
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                if (loadAdError.code == 3) {
                    adFailedCount++
                    Log.d("adFailedCount","Ad loading failed $adFailedCount times")

                    if (sortedAdsList.size-1 > currentAddPosition){
                        currentAddPosition++
                        loadnativead(
                            context,
                            view,
                            sortedAdsList[currentAddPosition].adscode,
                            sortedAdsList[currentAddPosition].ads_id
                        )
                    }


                    //loadnativead(context, view, adscode)


                }
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun sowAdrewarded() {
        if (result != null) {
            val adsList = result.filter { it.ads_type == 5 }
            sortedAdsList = adsList.sortedByDescending { it.cpm }.toMutableList()
            if (sortedAdsList.isNotEmpty()) {
                loadrewarded(sortedAdsList[currentAddPosition].adscode,sortedAdsList[currentAddPosition].ads_id)
            }
        }
    }

    private fun loadrewarded(adscode: String, adsId: Int) {
        RewardedInterstitialAd.load(context,
            adscode,
            AdManagerAdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    showRewardedAds(adsId)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedInterstitialAd = null
                    if (adError.code == 3) {
                        adFailedCount++
                        Log.d("adFailedCount","Ad loading failed $adFailedCount times")
                        //currentAddPosition++
                        if (sortedAdsList.size-1 > currentAddPosition){
                            currentAddPosition++
                            loadrewarded(
                                sortedAdsList[currentAddPosition].adscode,
                                sortedAdsList[currentAddPosition].ads_id
                            )
                        }
                    }
                }
            })
    }

    private fun showRewardedAds(adsId: Int) {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd?.show(context as Activity) {
                isOpen = true
            }
            postData(adsId)
            rewardedInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {

                    }
                }
        }
    }


    private fun postData(adsId: Int) {



        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        ipAddress =  inetAddress.hostAddress!!

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (checkForInternet(context)) {
            val appId = Prefs.getAppId(context)


            ApiUtilities.getApiInterface()
                .postData(ipAddress, appId, adsId , currentdate)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                        response.body().toString()

                    }

                    override fun onFailure(
                        call: Call<JsonObject>,
                        t: Throwable
                    ) {

                    }

                })


        }

    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}