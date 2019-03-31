package alejandro.lajusticia.mastermind.game.domain.enumeration.utils;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColorConverter {

    public static List<GuessBall> convertStringToGuessBalls(String input) {
        String[] guessSlitted = input.split(":");
        List<GuessBall> guessBalls = new ArrayList<>();
        for (String guessValue : guessSlitted) {
            buildGuessBallFromString(guessValue)
                    .ifPresent(guessBalls::add);
        }

        return guessBalls;
    }

    private static Optional<GuessBall> buildGuessBallFromString(String guessValue) {
        for (GuessColor guessColor : GuessColor.values()) {
            if (guessColor.name().equals(guessValue)) {
                return Optional.of(new GuessBall(guessColor));
            }
        }

        return Optional.empty();
    }

    public static List<FeedbackBall> convertStringToFeedbackBalls(String feedback) {
        String[] feedbackSlitted = feedback.split(":");
        List<FeedbackBall> feedbackBalls = new ArrayList<>();
        for (String feedbackValue : feedbackSlitted) {
            buildFeedBackBallFromString(feedbackValue)
                    .ifPresent(feedbackBalls::add);
        }

        return feedbackBalls;
    }

    private static Optional<FeedbackBall> buildFeedBackBallFromString(String feedbackValue) {
        for (FeedbackColor feedbackColor : FeedbackColor.values()) {
            if (feedbackColor.name().equals(feedbackValue)) {
                return Optional.of(new FeedbackBall(feedbackColor));
            }
        }

        return Optional.empty();
    }

    public static String convertGuessBallsToString(List<GuessBall> guessBalls) {
        StringBuilder guessBallsAsString = new StringBuilder();
        for (GuessBall guessBall : guessBalls) {
            if (guessBallsAsString.length() > 0) {
                guessBallsAsString.append(":");
            }
            guessBallsAsString.append(guessBall.getColor());
        }
        return guessBallsAsString.toString();
    }

    public static String convertFeedbackBallsToString(List<FeedbackBall> feedbackBalls) {
        StringBuilder feedbackBallsAsString = new StringBuilder();
        for (FeedbackBall feedbackBall : feedbackBalls) {
            if (feedbackBallsAsString.length() > 0) {
                feedbackBallsAsString.append(":");
            }
            feedbackBallsAsString.append(feedbackBall.getColor());
        }
        return feedbackBallsAsString.toString();
    }

    public static List<GuessBall> convertGuessStringToGuessBall(List<String> guessAsString) {
        List<GuessBall> guessBalls = new ArrayList<>();
        guessAsString.forEach(
                elementAsString -> guessBalls.add(
                        new GuessBall(elementAsString)
                )
        );
        return guessBalls;
    }

    public static String getAllColorAvailable() {
        StringBuilder colors = new StringBuilder();
        for (GuessColor color : GuessColor.values()) {
            if (colors.length() > 0) {
                colors.append(", ");
            }
            colors.append(color.name());
        }
        return colors.toString();
    }

    private ColorConverter() {

    }

}
