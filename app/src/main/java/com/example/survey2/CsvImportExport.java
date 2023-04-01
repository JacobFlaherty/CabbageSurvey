package com.example.survey2;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class CsvImportExport {



    public void writeToCsv(List<String[]> lines, Path path, String filename) throws Exception {
        System.out.println(path.toString());
        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString()+"/"+filename+".csv"))){
            for (String[] line : lines){
                writer.writeNext(line);
            }



        }




    }

    public void writeLineByLine(String filename, Context context) throws Exception {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            java.nio.file.Path path = context.getExternalFilesDir(null).toPath();
            writeToCsv(PointOperationsKt.pointsListToStringList(), path, filename);
        }

    }

    public List<String[]> readCsv(Path filePath) throws Exception {
        List<String[]> list = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (Reader reader = Files.newBufferedReader(filePath)){
                try (CSVReader csvReader = new CSVReader(reader)){
                    String[] line;
                    while ((line = csvReader.readNext()) != null){
                        list.add(line);
                    }
                }
            }
        }
        return list;
    }

    public List<String[]> readLineByLine(String filename, Context context) throws Exception {
        List<String[]> list = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            java.nio.file.Path path = context.getExternalFilesDir(null).toPath();
            String stringPath = path.toString();
            stringPath = stringPath+"/"+filename;
            path = Paths.get(stringPath);
            return readCsv(path);
        }
        else{
            return list;
        }

    }




}
