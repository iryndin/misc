package com.jetlore.socialnewsreader.formatter.model;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class FormatResult {
    private final String formattedText;

    public FormatResult(String formattedText) {
        this.formattedText = formattedText;
    }

    public String getFormattedText() {
        return formattedText;
    }
}
