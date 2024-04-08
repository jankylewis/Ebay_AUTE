package se.utility.fileUtil.fileReaderUtil;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileReader {

    private CSVFormat csvFormat;
    private final String relativeFilePath = "./src/test/java/se/spo/testDataFiles/";

    //region Handling instance

    public static final CsvFileReader INSTANCE = getInstance();

    private CsvFileReader(){}

    private static final class CsvFileReaderHelper{
        private static final CsvFileReader _INSTANCE = new CsvFileReader();
    }

    private static CsvFileReader getInstance() {
        return CsvFileReaderHelper._INSTANCE;
    }

    //endregion

    //The destinationPath started from testDataFiles folder
    public List<String> getValuesWithHeader(String destinationPath, String header)
            throws IOException {

        List<String> values = new ArrayList<>();

        Reader reader = new FileReader(relativeFilePath + destinationPath);

        csvFormat = CSVFormat
                .DEFAULT
                .builder()
                .setHeader(header)
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVRecord> csvRecords = csvFormat.parse(reader);

        for (CSVRecord csvRecord : csvRecords) {
            values.add(csvRecord.get(header));
        }

        return values;
    }

    public List<Pair<String, String>> getValuesWithHeaders(String destinationPath, String[] headers) throws IOException {

        List<Pair<String, String>> values = new ArrayList<>();

        Reader reader = new FileReader(relativeFilePath + destinationPath);

        csvFormat = CSVFormat
                .DEFAULT
                .builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVRecord> csvRecords = csvFormat.parse(reader);

        for (CSVRecord csvRecord : csvRecords) {
            values.add(Pair.with(csvRecord.get(headers[0]), csvRecord.get(headers[1])));
        }

        return values;
    }

    public List<String> getAllValuesFromCsvFile(String destinationPath) throws IOException {

        List<String> values = new ArrayList<>();

        Reader reader = new FileReader("./src/test/java/se/spo/testDataFiles/" + destinationPath);

        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

        for (CSVRecord csvRecord : csvParser) {
            values.add(csvRecord.get(0));
        }

        return values;
    }

    public int getNumberOfRows(String destinationPath) throws IOException{

        int rowCount = 0;

        try(BufferedReader reader = new BufferedReader(new FileReader(destinationPath))) {
            while(reader.readLine() != null) {
                rowCount++;
            }
        }

        return rowCount;
    }
}
