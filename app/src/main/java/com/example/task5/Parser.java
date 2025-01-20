package com.example.task5;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class Parser extends DefaultHandler {

    private List<String> currencies;
    private StringBuilder currentText;
    private boolean isRate = false;
    private String currencyName = "";

    public Parser() {
        currencies = new ArrayList<>();
        currentText = new StringBuilder();
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("rate")) {
            currencyName = attributes.getValue("currency");
        } else if (qName.equalsIgnoreCase("rate")) {
            isRate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("rate")) {
            if (isRate) {
                String rate = currentText.toString().trim();
                currencies.add(currencyName + " - " + rate);
                isRate = false;
                currentText.setLength(0);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isRate) {
            currentText.append(ch, start, length);
        }
    }
}