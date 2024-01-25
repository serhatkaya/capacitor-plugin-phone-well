import CallKit
import Capacitor
import Foundation

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(PhoneWellPlugin)
public class PhoneWellPlugin: CAPPlugin, CXCallObserverDelegate {
    private let implementation = PhoneWell()
    private let callObserver = CXCallObserver()
    private var enabled = false
    private var activeListener = false

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value),
        ])
    }

    @objc func detectCallState(_ call: CAPPluginCall) {
        call.keepAlive = true
        // 'ACTIVATE' | 'DEACTIVATE'
        let value = call.getString("action") ?? ""
        if value == "ACTIVATE" {
            activeListener = true
        } else if value == "DEACTIVATE" {
            activeListener = false
        } else {
            call.reject("must provide an action: 'ACTIVATE' | 'DEACTIVATE'")
        }

        if !enabled {
            callObserver.setDelegate(self, queue: nil) // Set delegate to self to call delegate method.
            enabled = true
        }

        call.resolve(["success": true])
    }

    @objc public func callObserver(_: CXCallObserver, callChanged call: CXCall) {
        // https://developer.apple.com/documentation/callkit/cxcall
        var callState = "IDLE"
        var callActive = true

        // 'RINGING' | 'OUTGOING' | 'IDLE' | 'ON_CALL' | 'ON_HOLD'
        if call.hasConnected {
            callState = "ON_CALL"
            callActive = true
        } else if call.isOutgoing {
            callState = "OUTGOING"
            callActive = false
        } else if call.hasEnded {
            callState = "IDLE"
            callActive = false
        } else if call.isOnHold {
            callState = "ON_HOLD"
            callActive = true
        } else {
            callState = "RINGING"
            callActive = false
        }

        if activeListener {
            notifyListeners("callStateChange", data: [
                "callActive": callActive,
                "callState": callState,
            ])
        }
    }

    @objc func start(_ call: CAPPluginCall) {
        let phoneNumber = call.getString("phone") ?? ""
        let phoneUrlStr = "tel://" + phoneNumber
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(URL(string: phoneUrlStr)!, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.openURL(URL(string: phoneUrlStr)!)
        }
        call.resolve([
            "msg": "成功",
        ])
    }
}
