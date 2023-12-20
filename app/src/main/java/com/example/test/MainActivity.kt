package com.example.test
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.magicbid.app.AdListnerInterface
import com.magicbid.app.MagicBidSdk
import com.magicbid.app.TemplateView
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var magicBidSdk: MagicBidSdk
    private lateinit var bannerad: LinearLayout
    private lateinit var inline: LinearLayout
    private lateinit var templateView: TemplateView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bannerad = findViewById(R.id.bannerad)
        templateView = findViewById(R.id.template)
        inline = findViewById(R.id.inbannerad)
        magicBidSdk = MagicBidSdk(this)
        magicBidSdk.sowAdrewarded()
//        magicBidSdk.showinterStitalad(object : AdListnerInterface {
//            override fun onAdClicked() {
//                Log.d("magicBid","onAdClicked")
//
//            }
//
//            override fun onAdClosed() {
//                Log.d("magicBid","onAdClosed")
//            }
//
//            override fun onAdFailedToLoad(var1: LoadAdError) {
//                Log.d("magicBid","onAdFailedToLoad")
//            }
//
//            override fun onAdImpression() {
//                Log.d("magicBid","onAdImpression")
//            }
//
//            override fun onAdLoaded(boolean: Boolean) {
//                Log.d("magicBid","onAdLoaded")
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                Log.d("magicBid","onAdDismissedFullScreenContent")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(var1: AdError) {
//                Log.d("magicBid","onAdFailedToShowFullScreenContent")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Log.d("magicBid","onAdShowedFullScreenContent")
//            }
//
//
//
//        })
////        if (magicBidSdk.setAutoAdCACHEING()) {
////            magicBidSdk.showInterstitialAds()
////        }else{
////            Log.d("magicBid","***custom text here")
////        }


    }
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        if (magicBidSdk.setAutoAdCacheing()) {
//            magicBidSdk.showInterstitialAds()
//            super.onBackPressed()
//        }else{
//            Log.d("magicBid","magicBid")
//        }
//    }




}