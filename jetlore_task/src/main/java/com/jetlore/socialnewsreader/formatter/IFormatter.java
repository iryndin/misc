package com.jetlore.socialnewsreader.formatter;

import com.jetlore.socialnewsreader.formatter.model.FormatRequest;
import com.jetlore.socialnewsreader.formatter.model.FormatResult;

/**
 * @author iryndin
 * @since 27/10/16
 */
public interface IFormatter {
    FormatResult format(FormatRequest request) throws FormatterException;
}
