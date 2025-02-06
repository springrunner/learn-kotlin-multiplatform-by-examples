import UIKit
import SwiftUI
import CoffeehouseClient

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // Compose has own keyboard handler
        ComposeView().ignoresSafeArea(.keyboard)
    }
}
