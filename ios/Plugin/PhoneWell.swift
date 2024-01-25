import Foundation

@objc public class PhoneWell: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func start(_ value: String) -> String {
        return value
    }
}
