package com.utils.framework.suggestions;

import com.utils.framework.io.Network;
import com.utils.framework.parsers.xml.GoogleSuggestionXMLParser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

public class GoogleSuggestionsProvider implements SuggestionsProvider<String> {
    final static private String SUGGESTIONS_URL = "http://google.com/complete/search?output=toolbar&q=";
    private GoogleSuggestionXMLParser parserXML;

    public GoogleSuggestionsProvider() {
        parserXML = new GoogleSuggestionXMLParser();
    }

    @Override
    public List<String> getSuggestions(String query) {
        if(query.equals("")){
            return Collections.emptyList();
        }

        String xml,url;
        try {
            url = SUGGESTIONS_URL + URLEncoder.encode(query, "UTF-8");
            xml = Network.executeRequestGET(url);
        }
        catch (IOException e){
            return Collections.emptyList();
        }

        return parserXML.parse(xml);
    }
}
