package com.jetlore.socialnewsreader.formatter.html;

import com.jetlore.socialnewsreader.formatter.AbstractFormatter;
import com.jetlore.socialnewsreader.formatter.model.FormatRequest;
import com.jetlore.socialnewsreader.formatter.model.FormatResult;
import com.jetlore.socialnewsreader.formatter.FormatterException;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntity;

import java.util.List;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class HtmlFormatter extends AbstractFormatter {

    @Override
    public FormatResult format(FormatRequest request) throws FormatterException {
        List<ObjectEntity> entities = sortObjectEntitiesByStartPos(request.getEntities());
        checkOverlappingEntitiesInSortedList(entities);
        checkEntityPositionsAreWithinText(request.getText().length(), entities);

        if (entities.isEmpty()) {
            return new FormatResult(request.getText());
        }

        String text = request.getText();
        StringBuilder formattedText = new StringBuilder();
        int pos = 0;
        for (ObjectEntity e : entities) {
            if (e.getStartPosition() > pos) {
                String s = text.substring(pos, e.getStartPosition());
                formattedText.append(s);
            }
            formattedText.append(format(text, e));
            pos = e.getEndPosition();
        }
        if (pos < text.length()) {
            String s = text.substring(pos);
            formattedText.append(s);
        }
        return new FormatResult(formattedText.toString());
    }

    private String format(String text, ObjectEntity e) {
        switch(e.getType()) {
            case ENTITY: return formatEntity(text, e);
            case USERNAME_TWITTER: return formatTwitterUsername(text, e);
            case LINK: return formatLink(text, e);
            default: return noFormat(text, e);
        }
    }

    private String noFormat(String text, ObjectEntity e) {
        return text.substring(e.getStartPosition(), e.getEndPosition());
    }

    private String formatLink(String text, ObjectEntity e) {
        String s = text.substring(e.getStartPosition(), e.getEndPosition());
        return String.format("<a href=\"%s\">%s</a>", s,s);
    }

    private String formatTwitterUsername(String text, ObjectEntity e) {
        String s = text.substring(e.getStartPosition(), e.getEndPosition());
        return String.format("<a href=\"http://twitter.com/%s\">%s</a>", s,s);
    }

    private String formatEntity(String text, ObjectEntity e) {
        String s = text.substring(e.getStartPosition(), e.getEndPosition());
        return "<strong>" + s + "</strong>";
    }
}
