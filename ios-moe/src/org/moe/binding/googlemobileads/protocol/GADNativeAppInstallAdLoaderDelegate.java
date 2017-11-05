package org.moe.binding.googlemobileads.protocol;


import org.moe.binding.googlemobileads.GADAdLoader;
import org.moe.binding.googlemobileads.GADNativeAppInstallAd;
import org.moe.natj.general.ann.Generated;
import org.moe.natj.general.ann.Library;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.ann.ObjCProtocolName;
import org.moe.natj.objc.ann.Selector;

@Generated
@Library("GoogleMobileAds")
@Runtime(ObjCRuntime.class)
@ObjCProtocolName("GADNativeAppInstallAdLoaderDelegate")
public interface GADNativeAppInstallAdLoaderDelegate extends
		GADAdLoaderDelegate {
	@Generated
	@Selector("adLoader:didReceiveNativeAppInstallAd:")
	void adLoaderDidReceiveNativeAppInstallAd(GADAdLoader adLoader,
			GADNativeAppInstallAd nativeAppInstallAd);
}