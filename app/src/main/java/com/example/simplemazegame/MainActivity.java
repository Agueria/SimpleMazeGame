package com.example.simplemazegame;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Student: Cem Cakir, Student ID: 44463, Lab Task 5
public class MainActivity extends AppCompatActivity {
    private static final String KEY_SCREEN = "screen";
    private static final String KEY_ROW = "row";
    private static final String KEY_COLUMN = "column";
    private static final int SCREEN_START = 0;
    private static final int SCREEN_GAME = 1;
    private static final int SCREEN_RESULT = 2;
    private static final int AVAILABLE_COLOR = Color.rgb(46, 125, 50);
    private static final int BLOCKED_COLOR = Color.rgb(117, 117, 117);
    private static final int PRIMARY_TEXT_COLOR = Color.WHITE;

    private View startScreen;
    private View gameScreen;
    private View resultScreen;
    private TextView roomText;
    private TextView roomValueText;
    private TextView moveFeedbackText;
    private TextView resultDetailsText;
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private MazeGameState currentState;
    private int currentScreen = SCREEN_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();
        bindActions();
        restoreState(savedInstanceState);
        renderCurrentScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCREEN, currentScreen);
        outState.putInt(KEY_ROW, currentState.getRow());
        outState.putInt(KEY_COLUMN, currentState.getColumn());
    }

    private void bindViews() {
        startScreen = findViewById(R.id.startScreen);
        gameScreen = findViewById(R.id.gameScreen);
        resultScreen = findViewById(R.id.resultScreen);
        roomText = findViewById(R.id.roomText);
        roomValueText = findViewById(R.id.roomValueText);
        moveFeedbackText = findViewById(R.id.moveFeedbackText);
        resultDetailsText = findViewById(R.id.resultDetailsText);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
    }

    private void bindActions() {
        findViewById(R.id.startButton).setOnClickListener(view -> startNewGame());
        findViewById(R.id.restartButton).setOnClickListener(view -> startNewGame());
        findViewById(R.id.backToMenuButton).setOnClickListener(view -> showStartScreen());
        leftButton.setOnClickListener(view -> move(Direction.LEFT));
        rightButton.setOnClickListener(view -> move(Direction.RIGHT));
        upButton.setOnClickListener(view -> move(Direction.UP));
        downButton.setOnClickListener(view -> move(Direction.DOWN));
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentState = MazeGameState.createDefaultGame();
            currentScreen = SCREEN_START;
            return;
        }

        currentScreen = savedInstanceState.getInt(KEY_SCREEN, SCREEN_START);
        int row = savedInstanceState.getInt(KEY_ROW, 1);
        int column = savedInstanceState.getInt(KEY_COLUMN, 0);
        currentState = new MazeGameState(MazeGameState.DEFAULT_MAZE, row, column);
    }

    private void startNewGame() {
        currentState = MazeGameState.createDefaultGame();
        currentScreen = SCREEN_GAME;
        moveFeedbackText.setText("Choose an available direction.");
        renderCurrentScreen();
    }

    private void showStartScreen() {
        currentScreen = SCREEN_START;
        renderCurrentScreen();
    }

    private void move(Direction direction) {
        if (!currentState.canMove(direction)) {
            moveFeedbackText.setText("That path is blocked.");
            renderGameScreen();
            return;
        }

        currentState = currentState.move(direction);
        if (currentState.isFinished()) {
            currentScreen = SCREEN_RESULT;
        } else {
            currentScreen = SCREEN_GAME;
            moveFeedbackText.setText("Moved to the next room.");
        }
        renderCurrentScreen();
    }

    private void renderCurrentScreen() {
        if (currentScreen == SCREEN_RESULT || currentState.isFinished()) {
            renderResultScreen();
        } else if (currentScreen == SCREEN_GAME) {
            renderGameScreen();
        } else {
            renderStartScreen();
        }
    }

    private void renderStartScreen() {
        startScreen.setVisibility(View.VISIBLE);
        gameScreen.setVisibility(View.GONE);
        resultScreen.setVisibility(View.GONE);
    }

    private void renderGameScreen() {
        startScreen.setVisibility(View.GONE);
        gameScreen.setVisibility(View.VISIBLE);
        resultScreen.setVisibility(View.GONE);

        roomText.setText(String.format("Room: row %d, column %d", currentState.getRow() + 1, currentState.getColumn() + 1));
        roomValueText.setText(String.format("Room value: %d", currentState.getCurrentRoomValue()));
        updateDirectionButton(leftButton, Direction.LEFT);
        updateDirectionButton(rightButton, Direction.RIGHT);
        updateDirectionButton(upButton, Direction.UP);
        updateDirectionButton(downButton, Direction.DOWN);
    }

    private void renderResultScreen() {
        currentScreen = SCREEN_RESULT;
        startScreen.setVisibility(View.GONE);
        gameScreen.setVisibility(View.GONE);
        resultScreen.setVisibility(View.VISIBLE);
        resultDetailsText.setText(String.format("Finished at row %d, column %d.", currentState.getRow() + 1, currentState.getColumn() + 1));
    }

    private void updateDirectionButton(Button button, Direction direction) {
        boolean available = currentState.canMove(direction);
        button.setEnabled(available);
        button.setAlpha(available ? 1.0f : 0.72f);
        button.setTextColor(PRIMARY_TEXT_COLOR);
        button.setBackgroundTintList(ColorStateList.valueOf(available ? AVAILABLE_COLOR : BLOCKED_COLOR));
    }
}
