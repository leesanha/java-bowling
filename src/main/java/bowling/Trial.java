package bowling;

public class Trial {
    private static final int NON_MISS_NUMBER = 10;
    private static final int DOUBLE_STRIKE_NUMBER = 20;
    private static final int NORMAL_MAX_SCORE_THRESHOLD = 11;

    private final Scores scores = new Scores();
    private ScoreString scoreString = new ScoreString();

    public void add(Score score) {
        scores.add(score);
        scoreString = addScoreString(score);
    }

    public FrameResult getFrameResult() {
        if (scores.isTotalScoreHigherOrEqualThan(NON_MISS_NUMBER)) {
            return getSpareOrStrike();
        }

        return FrameResult.MISS;
    }

    public boolean isNormalEnd() {
        if (scores.isTotalScoreHigherOrEqualThan(NORMAL_MAX_SCORE_THRESHOLD)) {
            throw new IllegalStateException("프레임 최대 점수를 초과하였습니다.");
        }

        return scores.isTotalScoreHigherOrEqualThan(NON_MISS_NUMBER) || scores.isSecondScore();
    }

    public boolean isFinalEnd() {
        if (scores.isSecondScore() &&
                scores.isTotalScoreHigherOrEqualThan(NORMAL_MAX_SCORE_THRESHOLD) &&
                !isDoubleStrike()) {
            throw new IllegalStateException("프레임 최대 점수를 초과하였습니다.");
        }

        return scores.isAfterSecondScore() || isFullMiss();
    }

    public String getScoreString() {
        return scoreString.getOutputString();
    }

    private FrameResult getSpareOrStrike() {
        if (!scores.isSecondScore() || isDoubleStrike()) {
            return FrameResult.STRIKE;
        }

        return FrameResult.SPARE;
    }

    private boolean isDoubleStrike() {
        return scores.isSecondScore() && scores.isTotalScoreHigherOrEqualThan(DOUBLE_STRIKE_NUMBER);
    }

    private boolean isFullMiss() {
        return scores.isSecondScore() && getFrameResult().equals(FrameResult.MISS);
    }

    private ScoreString addScoreString(Score score) {
        if (scores.isTotalScoreHigherOrEqualThan(NON_MISS_NUMBER)) {
            return scoreString.append(score, getFrameResult());
        }

        return scoreString.append(score);
    }
}