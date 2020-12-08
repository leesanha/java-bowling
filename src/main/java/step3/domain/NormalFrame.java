package step3.domain;

import step3.exception.InvalidPitchesException;
import step3.type.PitchesOrderType;
import step3.type.ResultPitchesType;

import java.util.Arrays;

import static java.util.Objects.nonNull;
import static step3.type.PitchesOrderType.FIRST;
import static step3.type.PitchesOrderType.SECOND;
import static step3.type.ResultPitchesType.SPARE;
import static step3.type.ResultPitchesType.STRIKE;

public class NormalFrame extends Frame {
    public static final int MAX_PITCHES = 2;
    public static final String ERROR_INVALID_PITCHES = "유효하지 않은 투구수입니다.";

    private final Frame next;

    public NormalFrame(int frameNo) {
        this(frameNo, makeNext(frameNo));
    }

    public NormalFrame(int frameNo, Frame next) {
        super(frameNo, BowlingSymbols.of(MAX_PITCHES));
        this.next = next;
    }

    @Override
    public Frame pitches(int pitchesCount) throws InvalidPitchesException {
        if (!isFinished()) {
            isValidPitchesCount(pitchesCount);
            super.bowlingSymbols.push(pitchesCount);

            return this;
        }

        return next.pitches(pitchesCount);
    }

    private void isValidPitchesCount(int pitchesCount) {
        if (bowlingSymbols.getScore() + pitchesCount > 10) {
            throw new IllegalArgumentException(ERROR_INVALID_PITCHES);
        }
    }

    @Override
    public int getScore() {
        int score = getCurrentScore();
        ResultPitchesType type = bowlingSymbols.getType();
        if (isStrikeOrSpare(type)) {
            score += next.getScore(type);
        }
        return score;
    }

    @Override
    public int getScore(ResultPitchesType prevType) {
        ResultPitchesType currentType = bowlingSymbols.getType();

        if (isDouble(prevType, currentType) && !next.hasNext()) {
            return getCurrentScore() + next.getScoreByOrderType(FIRST);
        }

        if (isDouble(prevType, currentType)) {
            return getCurrentScore() + next.getScoreByOrderType(FIRST);
        }

        if (STRIKE.equals(prevType)) {
            return getCurrentScore();
        }

        if (SPARE.equals(prevType)) {
            return getScoreByOrderType(FIRST);
        }
        return 0;
    }

    private boolean isDouble(ResultPitchesType prevType, ResultPitchesType currentType) {
        return STRIKE.equals(prevType) && STRIKE.equals(currentType);
    }


    @Override
    public Frame next() {
        return next;
    }

    @Override
    public boolean hasNext() {
        return nonNull(next);
    }

    public static Frame makeNext(int frameNo) {
        if (frameNo == BowlingGame.FRAME_LAST_NO) {
            return new FinalFrame(frameNo + 1);
        }
        return new NormalFrame(frameNo + 1);
    }


    @Override
    public boolean isFinished() {
        return bowlingSymbols.size() == 2 || bowlingSymbols.getType(FIRST).equals(STRIKE);
    }

    @Override
    public boolean isAllowAggregate() {
        ResultPitchesType type = bowlingSymbols.getType();
        if (isStrikeOrSpare(type)) {
            return next.isAllowAggregate(type);
        }
        return isFinished();
    }

    @Override
    public boolean isAllowAggregate(ResultPitchesType prevType) {
        ResultPitchesType currentType = bowlingSymbols.getType();
        if (isDouble(prevType, currentType)) {
            return next.existsSymbol(FIRST);
        }
        if (STRIKE.equals(prevType)) {
            return existsSymbol(SECOND);
        }
        return existsSymbol(FIRST);
    }

    private boolean isStrikeOrSpare(ResultPitchesType type) {
        return Arrays.asList(STRIKE, SPARE).contains(type);
    }

}