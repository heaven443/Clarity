package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class ClarityHelpController extends BaseController {

    @FXML
    private HBox settingsNavItem;

    @Override
    public void initialize() {
        System.out.println("Clarity Help & Support screen initialized.");

        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        System.out.println("Welcome to Clarity Help & Support!");
        System.out.println("Available features:");
        System.out.println("- Getting Started Guide");
        System.out.println("- FAQ Section");
        System.out.println("- Troubleshooting Tips");
        System.out.println("- Contact Support");
    }

    @FXML
    private void handleContactSupport() {
        showInfo("Contact Support",
                "Need help? Reach out to us!\n\n" +
                        "📧 Email: support@clarity.com\n" +
                        "💬 Live Chat: Available 9 AM - 5 PM\n" +
                        "📞 Phone: 1-800-CLARITY\n\n" +
                        "We typically respond within 24 hours.");
    }

    @FXML
    private void handleFAQ() {
        showInfo("Frequently Asked Questions",
                "Common Questions:\n\n" +
                        "Q: How do I create a new task?\n" +
                        "A: Go to My Task > Click 'Add Task' button\n\n" +
                        "Q: How do I reset my password?\n" +
                        "A: Settings > Account > Change Password\n\n" +
                        "Q: How do I export my notes?\n" +
                        "A: Notes > Select note > Options > Export\n\n" +
                        "For more help, contact support.");
    }

    @FXML
    private void handleTroubleshooting() {
        showInfo("Troubleshooting",
                "Common Issues & Solutions:\n\n" +
                        "🔧 App not loading?\n" +
                        "   → Check your internet connection\n" +
                        "   → Clear cache and restart\n\n" +
                        "🔧 Data not syncing?\n" +
                        "   → Verify you're logged in\n" +
                        "   → Check sync settings\n\n" +
                        "🔧 Can't find a feature?\n" +
                        "   → Use the search bar\n" +
                        "   → Check the user guide\n\n" +
                        "Still having issues? Contact support!");
    }

    @FXML
    private void handleUserGuide() {
        showInfo("User Guide",
                "📖 Clarity User Guide\n\n" +
                        "Dashboard:\n" +
                        "  • View your productivity overview\n" +
                        "  • See upcoming tasks and deadlines\n\n" +
                        "My Task:\n" +
                        "  • Create and manage tasks\n" +
                        "  • Set priorities and deadlines\n" +
                        "  • Track completion status\n\n" +
                        "Notes:\n" +
                        "  • Write and organize notes\n" +
                        "  • Search and filter\n\n" +
                        "Schedule:\n" +
                        "  • View calendar\n" +
                        "  • Add events and reminders\n\n" +
                        "For detailed guides, visit our website.");
    }
}