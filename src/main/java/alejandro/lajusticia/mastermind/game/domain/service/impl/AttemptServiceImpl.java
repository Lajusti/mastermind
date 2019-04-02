package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Ball;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.NullFeedbackException;
import alejandro.lajusticia.mastermind.game.domain.service.AttemptService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttemptServiceImpl implements AttemptService {

    @Override
    public Attempt createAttemptFromSecretAndAttemptInput(List<GuessBall> secret, List<GuessBall> attemptInput)
            throws EmptyInputException, NullFeedbackException
    {
        List<Ball> secretCopy = Lists.newArrayList(secret);
        List<Ball> attemptCopy = Lists.newArrayList(attemptInput);

        List<FeedbackBall> feedback = buildBlackFeedbackBalls(secretCopy, attemptCopy);
        feedback.addAll(buildWhiteFeedbackBalls(secretCopy, attemptCopy));

        return new Attempt(attemptInput, feedback);
    }

    private List<FeedbackBall> buildBlackFeedbackBalls(List<Ball> secret, List<Ball> attempt) {
        List<FeedbackBall> blackFeedbackBalls = new ArrayList<>();
        for (int position = 0; position < secret.size(); position++) {
            if (secret.get(position).equals(attempt.get(position))) {
                FeedbackBall blackBall = new FeedbackBall(FeedbackColor.BLACK);
                blackFeedbackBalls.add(blackBall);
                secret.set(position, blackBall);
                attempt.set(position, blackBall);
            }
        }

        return blackFeedbackBalls;
    }

    private List<FeedbackBall> buildWhiteFeedbackBalls(List<Ball> secret, List<Ball> attempt) {
        List<FeedbackBall> whiteFeedbackBalls = new ArrayList<>();
        for (int position = 0; position < attempt.size(); position++) {
            Ball currentBall = attempt.get(position);
            if (currentBall instanceof GuessBall && secret.contains(currentBall)) {
                FeedbackBall whiteBall = new FeedbackBall(FeedbackColor.WHITE);
                whiteFeedbackBalls.add(whiteBall);
                attempt.set(position, whiteBall);
                secret.set(secret.indexOf(currentBall), whiteBall);
            }
        }

        return whiteFeedbackBalls;
    }


}
