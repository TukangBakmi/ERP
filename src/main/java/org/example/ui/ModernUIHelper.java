package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class ModernUIHelper {

    // Button Styles
    public static final String BTN_PRIMARY = """
            -fx-background-color: #2563EB;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 12 20;
            -fx-cursor: hand;
            -fx-background-radius: 8;
            -fx-font-size: 13px;
            """;

    public static final String BTN_PRIMARY_HOVER = """
            -fx-background-color: #1D4ED8;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 12 20;
            -fx-cursor: hand;
            -fx-background-radius: 8;
            -fx-font-size: 13px;
            """;

    public static final String BTN_SUCCESS = """
            -fx-background-color: #10B981;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 12 20;
            -fx-cursor: hand;
            -fx-background-radius: 8;
            -fx-font-size: 13px;
            """;

    public static final String BTN_DANGER = """
            -fx-background-color: #DC2626;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 12 20;
            -fx-cursor: hand;
            -fx-background-radius: 8;
            -fx-font-size: 13px;
            """;

    public static final String BTN_EDIT = """
            -fx-padding: 6 12;
            -fx-font-size: 12;
            -fx-background-color: #0EA5A3;
            -fx-text-fill: white;
            -fx-background-radius: 6;
            -fx-font-weight: 600;
            """;

    public static final String BTN_EDIT_HOVER = """
            -fx-padding: 6 12;
            -fx-font-size: 12;
            -fx-background-color: #0D9488;
            -fx-text-fill: white;
            -fx-background-radius: 6;
            -fx-font-weight: 600;
            """;

    public static final String BTN_DELETE = """
            -fx-padding: 6 12;
            -fx-font-size: 12;
            -fx-text-fill: white;
            -fx-background-color: #DC2626;
            -fx-background-radius: 6;
            -fx-font-weight: 600;
            """;

    public static final String BTN_DELETE_HOVER = """
            -fx-padding: 6 12;
            -fx-font-size: 12;
            -fx-text-fill: white;
            -fx-background-color: #B91C1C;
            -fx-background-radius: 6;
            -fx-font-weight: 600;
            """;

    // Text Field & Input Styles
    public static final String INPUT_FIELD = """
            -fx-padding: 10;
            -fx-font-size: 13px;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            """;

    // Container Styles
    public static final String CARD_CONTAINER = """
            -fx-background-color: white;
            -fx-border-color: #E2E8F0;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 12, 0.0, 0, 2);
            """;

    public static final String PAGE_BACKGROUND = "-fx-background-color: #F8FAFC;";

    // Label Styles
    public static final String LABEL_TITLE = """
            -fx-font-size: 28px;
            -fx-font-weight: 900;
            -fx-text-fill: #0F172A;
            -fx-letter-spacing: 0.5;
            """;

    public static final String LABEL_SUBTITLE = """
            -fx-font-size: 13px;
            -fx-text-fill: #64748B;
            """;

    public static final String LABEL_FORM = """
            -fx-text-fill: #334155;
            -fx-font-weight: 600;
            -fx-font-size: 13px;
            """;

    // Apply hover effects to buttons
    public static void applyPrimaryButtonHover(Button button) {
        button.setStyle(BTN_PRIMARY);
        button.setOnMouseEntered(e -> button.setStyle(BTN_PRIMARY_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_PRIMARY));
    }

    public static void applyEditButtonHover(Button button) {
        button.setStyle(BTN_EDIT);
        button.setOnMouseEntered(e -> button.setStyle(BTN_EDIT_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_EDIT));
    }

    public static void applyDeleteButtonHover(Button button) {
        button.setStyle(BTN_DELETE);
        button.setOnMouseEntered(e -> button.setStyle(BTN_DELETE_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_DELETE));
    }

    // Style dialog buttons
    public static void styleDialogButtons(Dialog<?> dialog) {
        dialog.getDialogPane().lookupButton(ButtonType.OK).setStyle("""
            -fx-padding: 10 24;
            -fx-background-color: #2563EB;
            -fx-text-fill: white;
            -fx-font-weight: 600;
            -fx-background-radius: 6;
            """);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("""
            -fx-padding: 10 24;
            -fx-background-color: #E2E8F0;
            -fx-text-fill: #334155;
            -fx-font-weight: 600;
            -fx-background-radius: 6;
            """);
    }

    // Create modern title labels
    public static Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_TITLE);
        return label;
    }

    // Create modern subtitle labels
    public static Label createSubtitleLabel(String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_SUBTITLE);
        return label;
    }

    // Create form label
    public static Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_FORM);
        return label;
    }

    // Format currency display
    public static String formatCurrency(double value) {
        return String.format("Rp %,.0f", value);
    }

    // Format percentage
    public static String formatPercentage(double value) {
        return String.format("%.2f%%", value);
    }
}
