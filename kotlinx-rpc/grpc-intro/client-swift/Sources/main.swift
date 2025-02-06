// The Swift Programming Language
// https://docs.swift.org/swift-book

import GRPC
import NIO
import SwiftProtobuf

typealias OrderServiceNIOClient = Coffeehouse_Contract_Order_OrderServiceNIOClient
typealias PlaceOrderRequest = Coffeehouse_Contract_Order_PlaceOrderRequest
typealias OrderLine = Coffeehouse_Contract_Order_OrderLine

let group = MultiThreadedEventLoopGroup(numberOfThreads: 1)
defer {
    try? group.syncShutdownGracefully()
}

let channel = ClientConnection
    .insecure(group: group)
    .connect(host: "localhost", port: 50051)

let client = OrderServiceNIOClient(channel: channel)

let request = PlaceOrderRequest.with {
    $0.customerID = "swift-user-5678"
    $0.orderLines = [
        OrderLine.with { $0.productCode = "CAPPUCCINO"; $0.quantity = 2 },
        OrderLine.with { $0.productCode = "AMERICANO"; $0.quantity = 1 }
    ]
}

do {
    let response = try client.placeOrder(request).response.wait()
    print("✅ Swift 클라이언트 - 주문 완료, 주문 ID: \(response.orderID)")
} catch {
    print("❌ Swift 클라이언트 - 주문 오류: \(error)")
}
