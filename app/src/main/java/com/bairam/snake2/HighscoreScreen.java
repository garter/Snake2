package com.bairam.snake2;

import java.util.List;

public class HighscoreScreen extends Screen {
    String lines[] = new String[5];

    public HighscoreScreen(Game game){
        super(game);

        for (int i = 0; i < 5; i++){
            lines[i] = " " + (i + 1) + ". " + Settings.highscores[i];
        }
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++){
            Input.TouchEvent event = touchEvents.get(i);
            if (event.x < 64 && event.y > 416){
                if (Settings.soundEnabled)
                    Assets.click.play(1);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.mainMenu, 64, 20, 0, 42, 196, 42);

        int y = 100;
        for(int i = 0; i < 5; i++){
            drawText(g, lines[i], 20, y);
            y += 50;
        }

        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
    }

    public void drawText(Graphics g, String line, int x, int y){
        int len = line.length();
        for (int i = 0; i < len; i++){
            char charecter = line.charAt(i);
            if (charecter == ' '){
                x += 20;
                continue;
            }

            int scrX = 0;
            int scrWidth = 0;
            if (charecter == '.'){
                scrX = 200;
                scrWidth = 10;
            }else {
                scrX = (charecter - '0') * 20;
                scrWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, scrX, 0, scrWidth, 32);
            x += scrWidth;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
