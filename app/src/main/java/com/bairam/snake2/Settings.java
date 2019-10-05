package com.bairam.snake2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings { //хранит, загружает и сохраняет наши настройки
    public static boolean soundEnabled = true;
    public static int[] highscores = new int[] { 100, 80, 50, 30, 10 };

    public static void load(FileIO files){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(".mrnom")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++){
                highscores[i] = Integer.parseInt(in.readLine());
            }
        }catch (IOException e){
            // Хорошо. Стандартные значения у нас есть
        }catch (NumberFormatException e){
            // Нет, ну как же прекрасны значения по умолчанию.
        }finally {
            try {
                if (in != null){
                    in.close();
                }
            }catch (IOException e){

            }
        }
    }

    public static void save(FileIO files){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mrnom")));
            out.write(Boolean.toString(soundEnabled));
            for (int i = 0; i < 5; i++){
                out.write(Integer.toString(highscores[i]));
            }
        }catch (IOException e){
            //
        }finally {
            try {
                if (out != null){
                    out.close();
                }
            }catch (IOException e){
                //
            }
        }
    }

    // добавить новый рекорд в таблицу рекордов, автоматически сортируя их
    public static void addScore(int score){
        for (int i = 0; i < 5; i++){
            if (highscores[i] < score){
                for (int j = 4; j > i; j--){
                    highscores[j] = highscores[j - 1];
                    highscores[i] = score;
                    break;
                }
            }
        }
    }
}
