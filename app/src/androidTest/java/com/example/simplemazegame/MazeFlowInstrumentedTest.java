package com.example.simplemazegame;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MazeFlowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void startScreenShowsStartAction() {
        onView(withId(R.id.startButton)).check(matches(isDisplayed()));
        onView(withText("Simple Maze Game")).check(matches(isDisplayed()));
    }

    @Test
    public void roomScreenShowsAllowedAndBlockedDirections() {
        onView(withId(R.id.startButton)).perform(click());

        onView(withText("Room: row 2, column 1")).check(matches(isDisplayed()));
        onView(withText("Room value: 28")).check(matches(isDisplayed()));
        onView(withId(R.id.upButton)).check(matches(isEnabled()));
        onView(withId(R.id.downButton)).check(matches(isEnabled()));
        onView(withId(R.id.leftButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.rightButton)).check(matches(not(isEnabled())));
    }

    @Test
    public void sampleMazePathCanReachResultScreen() {
        onView(withId(R.id.startButton)).perform(click());

        onView(withId(R.id.downButton)).perform(click());
        onView(withId(R.id.downButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.upButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.downButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.upButton)).perform(click());
        onView(withId(R.id.upButton)).perform(click());
        onView(withId(R.id.upButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.downButton)).perform(click());

        onView(withText("You win!")).check(matches(isDisplayed()));
        onView(withText("Finished at row 2, column 3.")).check(matches(isDisplayed()));
        onView(withId(R.id.restartButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backToMenuButton)).check(matches(isDisplayed()));
    }
}
