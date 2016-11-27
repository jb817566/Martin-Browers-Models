/*
 * Written by WCUPA Computer Science club members
 * November 2016
 */
package edu.wcupa.cscclub.projects.martinbrower.reportgenerator.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohamed Alie Pussah (mp754927@wcupa.edu)
 * Encapsulates actions for efficiently parsing a CSV file 
 * to extract predefined data items
 */
public class CsvReader
{
    // <editor-fold defaultstate="collapsed" desc="Fields">
    
    /**
     * The pages contained in the parsed CSV file
     */
    public final ArrayList<Page> PAGES;
    
    private final String _wantedColumns;    
    private int _rowCount;
    private final BufferedReader _reader;

    private Pattern SUMMARY_HEADER_PATTERN;
    private Pattern COLUMN_HEADER_PATTERN;
    private Pattern DATA_ROW_PATTERN;
    private Pattern CART_TOTAL_PATTERN;
    private Pattern PAGE_NUMBER_PATTERN;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new reader with the given information
     * @param fileName The full path of the CSV file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public CsvReader(String fileName) 
            throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(fileName);
        _reader = new BufferedReader(reader);
        PAGES = new ArrayList<>();
        
        // In the future, this value could be dynamic
        _wantedColumns = 
                "ROUTE,WRIN,TRAILER POSITION,STOP,CASES,DESCRIPTION,TRAILER POS";

        initializePatterns();
        processFile(_reader);
        _reader.close();
    }

    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Helpers">
    private void initializePatterns() {
        SUMMARY_HEADER_PATTERN = Pattern.compile("\\b(ROUTE|WRIN|TRAILER\\s*(POSITION|POS)|STOP|CASES|DESCRIPTION)\\s*:{1}\\s*\\,+\\w+\\b");
        COLUMN_HEADER_PATTERN = Pattern.compile("^,+(ROUTE|WRIN|TRAILER\\s*(POSITION|POS)|STOP|CASES|DESCRIPTION)+\\,+.*$");
        DATA_ROW_PATTERN = Pattern.compile("^,+(\\b(?:\\d*\\.)?\\d+\\b\\,+)+\\b(\\w+(\\s|[\\/\\-\\_])?)+\\b\\s*\\,+(\\b(?:\\d*\\.)?\\d+\\b\\,+)\\b\\w+\\b\\,+.*\\b(\\b(?:\\d*\\.)?\\d+\\b)\\,*$");
        CART_TOTAL_PATTERN = Pattern.compile("^CART TOTAL\\,*(?:\\d*\\.)?\\d+");
        PAGE_NUMBER_PATTERN = Pattern.compile("^\\bPage\\s*\\d+\\b");
    }

    private Map<String, String> getSummaryHeader(String line) {
        Matcher matcher = SUMMARY_HEADER_PATTERN.matcher(line);
        Map<String, String> summaryHeaders = new HashMap<>();
        while (matcher.find()) {
            String match = matcher.group();
            String header = match.substring(0, match.indexOf(":")).trim();
            String value = match.substring(match.lastIndexOf(",") + 1);
            summaryHeaders.put(header, value);
        }
        return summaryHeaders;
    }

    private String getPageNumber(String line) {
        Matcher matcher = PAGE_NUMBER_PATTERN.matcher(line);
        if (matcher.find()) {
            String match = matcher.group();
            String pageNumber = match.substring(match.indexOf(" ") + 1);
            return pageNumber;
        }
        return null;
    }

    private boolean updateColumnHeaders(Page page, String line) {
        Matcher matcher = COLUMN_HEADER_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return false;
        }

        int column = 0;
        /*
         * I am using String.split() here because it provides
         * /* an accurate count of columns in the file. Normally,
         * /* you will want to use the group() method from the
         * /* matcher
         */
        Queue<String> row
                = new LinkedList<>(Arrays.asList(matcher.group().split(",")));
        while (!row.isEmpty()) {
            String data = row.poll();
            column++;
            if (data == null || "".equals(data)
                    || !_wantedColumns.contains(data)) {
                continue;
            }

            Cell header = new Cell(_rowCount, column, data);
            page.getColumns().add(new Column(header));
        }
        return true;
    }

    private boolean updatePageColumns(String line, Page page) {
        Matcher matcher = DATA_ROW_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        // See getColumnHeaders for reasoning behind String.split()
        Queue<String> row
                = new LinkedList<>(Arrays.asList(matcher.group().split(",")));
        int dataColumn = 0;
        for (Column column : page.getColumns()) {
            while (!row.isEmpty()) {
                String data = row.poll();
                dataColumn++;
                if (data == null || "".equals(data)) {
                    continue;
                }

                int positionDifference
                        = Math.abs(dataColumn - column.HEADER.COLUMN);
                if (positionDifference == 0 || positionDifference == 1) {
                    Cell dataCell
                            = new Cell(_rowCount, column.HEADER.COLUMN, data);
                    column.CELLS.add(dataCell);
                    break;
                }
            }
        }
        return true;
    }

    private void updatePage(Page page, String line) {
        if (updateColumnHeaders(page, line)) {
            return;
        }

        if (updatePageColumns(line, page)) {
            return;
        }

        Matcher matcher = CART_TOTAL_PATTERN.matcher(line);
        if (!matcher.find()) {
            return;
        }
        page.setCartTotal(Integer.parseInt(
                matcher.group().substring(matcher.group()
                       .lastIndexOf(",") + 1)));
    }

    private void processFile(BufferedReader reader) throws IOException {
        Page currentPage = null;
        String line;
        while ((line = _reader.readLine()) != null) {
            _rowCount++;
            if (currentPage == null) {
                currentPage = new Page();
            }

            String pageNumber = getPageNumber(line);
            if (pageNumber != null && pageNumber.length() > 0) {
                currentPage.setNumber(Integer.parseInt(pageNumber.trim()));
                PAGES.add(currentPage);
                currentPage = null;
                continue;
            }

            Map<String, String> currentSummaryHeaders = getSummaryHeader(line);
            if (currentSummaryHeaders.size() > 0) {
                currentPage.SUMMARIES.putAll(currentSummaryHeaders);
                continue;
            }

            updatePage(currentPage, line);
        }
    }

    // </editor-fold>
        
    // </editor-fold>
}
