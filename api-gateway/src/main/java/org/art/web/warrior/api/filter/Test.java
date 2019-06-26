package org.art.web.warrior.api.filter;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private static final Pattern FOOTNOTE_PATTERN = Pattern.compile("([^ ]+)(<note>(.|\\r|\\n)*?</note>)([。.,])?");

    public static void main(String[] args) {

        String text = "RegExr was created by gskinner.com, and is proudly hosted by Media Temple.\n" +
                "\n" +
                "Edit the Expression<note>Super Footnote 1 <a href=\"/content\">Link</a></note>. Text to see matches. Roll over matches or the expression for details. PCRE & Javascript flavors of RegEx are supported.\n" +
                "\n" +
                "The side and Help. You can also Save & Share with the Community, and view patterns you create or favorite<note>Super Footnote 2 <a href=\"/content\">Link</a></note> in My Patterns.\n" +
                "\n" +
                "Explore results with the Tools below<note>Super Footnote 2 <a href=\"/content\">Link</a></note>. Details lists capture groups. Explain your expression in plain English<note>Super Footnote 4 <a href=\"/content\">Link</a></note>.";

        Matcher noteMatcher = FOOTNOTE_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        int noteIndex = 0;
        while (noteMatcher.find()) {
            String delimiter = " ";
            String notePlaceholder = "@" + noteIndex + "@";
            String matchedText = noteMatcher.group(0);
            String noteContentRaw = noteMatcher.group(2);
            String period = StringUtils.EMPTY;
            if (noteMatcher.groupCount() > 3) {
                period = noteMatcher.group(4);
            }
            String replacement = "<span class=\"tooltip-dot-wrapper\">" + matchedText + period + delimiter + "</span>";
            noteMatcher.appendReplacement(sb, replacement);
            System.out.println(noteContentRaw);
            noteIndex++;
        }
        noteMatcher.appendTail(sb);
        String fieldTextTemplate = sb.toString();
        System.out.println(fieldTextTemplate);
    }
}
