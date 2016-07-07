/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g2inc.scap.editor.gui.resources;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.Style;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author justin.raabe
 */
public class HTMLEditorScrubber {

    /**
     * Puts in an html attribute to force an html box to use the default system
     * font instead of Times New Roman, but only if the text has no Font-Family
     * attributes already
     *
     * @param toScrub
     * @return
     */
    public static JEditorPane scrubJEditorPane(JEditorPane toScrub) {
        boolean declaredFont = false;
        StyleSheet styles = ((HTMLDocument) toScrub.getDocument()).getStyleSheet();
        Enumeration rules = styles.getStyleNames();
        while (rules.hasMoreElements()) {
            String name = (String) rules.nextElement();
            Style rule = styles.getStyle(name);
            System.out.println(rule.toString());
            if (rule.toString().contains("font-family")) {
                declaredFont = true;
            }
        }

        if (!declaredFont) {
            // add a CSS rule to force body tags to use the default label font
            // instead of the value in javax.swing.text.html.default.csss
            Font font = UIManager.getFont("Label.font");
            String bodyRule = "body { font-family: " + font.getFamily() + "; "
                    + "font-size: " + font.getSize() + "pt; }";
            ((HTMLDocument) toScrub.getDocument()).getStyleSheet().addRule(bodyRule);
        }
        return toScrub;
    }
}
