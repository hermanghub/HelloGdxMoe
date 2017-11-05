package com.hermantseproduction.hellogdxmoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;

import org.moe.binding.googlemobileads.GADBannerView;
import org.moe.binding.googlemobileads.GADRequest;
import org.moe.natj.general.Pointer;

import apple.coregraphics.struct.CGPoint;
import apple.coregraphics.struct.CGRect;
import apple.coregraphics.struct.CGSize;
import apple.foundation.NSMutableArray;
import apple.uikit.UIApplication;
import apple.uikit.UIScreen;
import apple.uikit.c.UIKit;
import org.moe.binding.googlemobileads.c.GoogleMobileAds;
import org.moe.binding.googlemobileads.protocol.GADBannerViewDelegate;

//public class IOSMoeLauncher extends IOSApplication.Delegate {
//
//    protected IOSMoeLauncher(Pointer peer) {
//        super(peer);
//    }
//
//    @Override
//    protected IOSApplication createApplication() {
//        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
//        config.useAccelerometer = false;
//        return new IOSApplication(new HelloGame(), config);
//    }
//
//    public static void main(String[] argv) {
//        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
//    }
//}

public class IOSMoeLauncher extends IOSApplication.Delegate implements adDelegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }
    private static final boolean USE_TEST_DEVICES = true;
    private GADBannerView adview;
    private boolean adsInitialized = false;
    private IOSApplication application;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false;
        config.statusBarVisible = true;
//        application = new IOSApplication(new MyGdxGame(this), config);
        application = new IOSApplication(new HelloGame(this), config);
        return application;
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }


    @Override
    public void hide() {
        initializeAds();

        final CGSize screenSize = UIScreen.mainScreen().bounds().size();
        double screenWidth = screenSize.width();

        final CGSize adSize = adview.bounds().size();
        double adWidth = adSize.width();
        double adHeight = adSize.height();

        Gdx.app.log("iOS-MOE","Hidding ad. size[" + adWidth + "," + adHeight+ "]");

        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        adview.setFrame(new CGRect(new CGPoint(0, -bannerHeight), new CGSize(bannerWidth, bannerHeight)));
    }

    @Override
    public void show() {
        initializeAds();

        final CGSize screenSize = UIScreen.mainScreen().bounds().size();
        double screenWidth = screenSize.width();

        final CGSize adSize = adview.bounds().size();
        double adWidth = adSize.width();
        double adHeight = adSize.height();

        Gdx.app.log("iOS-MOE","Showing ad. size[" + adWidth + "," + adHeight+ "]");

        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

//        adview.setFrame(new CGRect(new CGPoint((screenWidth / 2) - adWidth / 2, 0), new CGSize(bannerWidth, bannerHeight)));
        adview.setFrame(new CGRect(new CGPoint((screenWidth / 2) - adWidth / 2, 300), new CGSize(adWidth, adHeight)));
        Gdx.app.log("iOS-MOE","Showing ad. frame size[" + adWidth + "," + adHeight + "] @ [" + ((screenWidth / 2) - adWidth / 2) + ", 300]");
    }

    //from the libgdx admob tutorial, changed the roboVM specific parts to MOE
    public void initializeAds() {
        if (!adsInitialized) {
            Gdx.app.log("iOS-MOE","Initalizing ads...");

            adsInitialized = true;

            adview = GADBannerView.alloc().initWithAdSize(GoogleMobileAds.kGADAdSizeBanner());
            adview.setAdUnitID(AdUnitIds.AD_BANNER_ID); //put your secret key here
            adview.setRootViewController(application.getUIViewController());
            application.getUIViewController().view().addSubview(adview);
            application.getUIViewController().view().bringSubviewToFront(adview);
//            application.getUIViewController().setCanDisplayBannerAds(true);
//            Gdx.app.log("Banner", "canDisplayBannerAds:" + application.getUIViewController().canDisplayBannerAds());

            final GADRequest request = GADRequest.request();
            if (USE_TEST_DEVICES) {
                final NSMutableArray<String> testDevices = (NSMutableArray<String>) NSMutableArray.alloc().init();
                testDevices.add(AdUnitIds.AD_TEST_ID);
                request.setTestDevices(testDevices);
                Gdx.app.log("iOS-MOE","Test devices: " + request.testDevices());
            }

            adview.setDelegate(new GADBannerViewDelegate() {
                @Override
                public void adViewDidReceiveAd(GADBannerView gadBannerView) {
                    adview.setHidden(false);

                }
            });
            adview.loadRequest(request);

            Gdx.app.log("iOS-MOE","Initalizing ads complete.");
        }
    }

    @Override
    public void showAds(boolean show) {
        initializeAds();

        final CGSize screenSize = UIScreen.mainScreen().bounds().size();

        double screenWidth = screenSize.width();

        final CGSize adSize = adview.bounds().size();
        double adWidth = adSize.width();
        double adHeight = adSize.height();

//        Gdx.app.log("iOS-MOE","Hidding ad. size[" + adWidth + "," + adHeight+ "]");

//        float bannerHeight = 20f;
//        float bannerWidth = (float) (bannerHeight / adHeight * adWidth);
        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        if(show) {
            Gdx.app.log("iOS-MOE","Showing ad. size[" + bannerWidth + "," + bannerHeight+ "] @ [" + (screenWidth / 2 - adWidth / 2) + ",500]");
            adview.setFrame(new CGRect(new CGPoint((screenWidth / 2) - adWidth / 2, 500), new CGSize(bannerWidth, bannerHeight)));
        } else {
            Gdx.app.log("iOS-MOE","Hidding ad. size[" + adWidth + "," + adHeight+ "]");
            adview.setFrame(new CGRect(new CGPoint(0, -bannerHeight), new CGSize(bannerWidth, bannerHeight)));
        }
    }

    //simpler way to bring ads to ios-moe. still works, but lacks the show and hide features
    private void init_admob_banner(){

        GADBannerView adview = GADBannerView.alloc().initWithAdSize(GoogleMobileAds.kGADAdSizeBanner());
        adview.setRootViewController(application.getUIViewController());
        adview.setAdUnitID(AdUnitIds.AD_BANNER_ID);
        adview.setDelegate(new GADBannerViewDelegate() {
            @Override
            public void adViewDidReceiveAd(GADBannerView gadBannerView) {
                adview.setHidden(false);

            }
        });
//        adview.setFrame(new CGRect(new CGPoint(0 + (320) / 2f - adview.bounds().size().width() / 2f, 0 + (680) / 2f - adview.bounds().size().height() / 2f), new CGSize(adview.bounds().size().width(), adview.bounds().size().height())));
        final CGSize screenSize = UIScreen.mainScreen().bounds().size();

        double screenWidth = screenSize.width();

        final CGSize adSize = adview.bounds().size();
        double adWidth = adSize.width();
        double adHeight = adSize.height();
        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);
        adview.setFrame(new CGRect(new CGPoint((screenWidth / 2) - adWidth / 2, 0), new CGSize(bannerWidth, 20)));
        GADRequest request = GADRequest.request();
        if (USE_TEST_DEVICES) {
            NSMutableArray<String> devices = (NSMutableArray<String>) NSMutableArray.alloc().init();
            devices.add(AdUnitIds.AD_TEST_ID);
            request.setTestDevices(devices);
        }
        adview.loadRequest(request);
        application.getUIViewController().view().addSubview(adview);
    }

    @Override
    public void applicationDidBecomeActive(UIApplication application) {
        super.applicationDidBecomeActive(application);

//        init_admob_banner();
//        showAds(true);
//        show();
    }
}
