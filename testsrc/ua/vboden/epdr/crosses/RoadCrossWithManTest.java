package ua.vboden.epdr.crosses;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.vboden.epdr.enums.Direction.E;
import static ua.vboden.epdr.enums.Direction.N;
import static ua.vboden.epdr.enums.Direction.S;
import static ua.vboden.epdr.enums.Direction.W;
import static ua.vboden.epdr.enums.StickPosition.BEFORE;
import static ua.vboden.epdr.enums.StickPosition.DOWN;
import static ua.vboden.epdr.enums.StickPosition.FORWARD;
import static ua.vboden.epdr.enums.StickPosition.SIDE;
import static ua.vboden.epdr.enums.StickPosition.UP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jme3.math.Vector3f;

import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.enums.Direction;
import ua.vboden.epdr.enums.StickPosition;

public class RoadCrossWithManTest {

	private RoadCrossWithMan cross;
	private TrafficMan trafficMan;
	Map<Direction, Vector3f> startPoints = new HashMap<Direction, Vector3f>() {
		{
			put(N, new Vector3f(20, 0, 15));
			put(S, new Vector3f(20, 0, 35));
			put(E, new Vector3f(35, 0, 20));
			put(W, new Vector3f(15, 0, 20));
		}
	};
	Map<Direction, Vector3f> middlePoints = new HashMap<Direction, Vector3f>() {
		{
			put(N, new Vector3f(20, 0, 19));
			put(S, new Vector3f(20, 0, 21));
			put(E, new Vector3f(21, 0, 20));
			put(W, new Vector3f(19, 0, 20));
		}
	};
	Map<Direction, Vector3f> checkPoints = new HashMap<Direction, Vector3f>() {
		{
			put(N, new Vector3f(20, 0, 37));
			put(S, new Vector3f(20, 0, 13));
			put(E, new Vector3f(13, 0, 20));
			put(W, new Vector3f(37, 0, 20));
		}
	};

	@BeforeEach
	public void setUp() {
		cross = new RoadCrossWithMan(new Vector3f(20, 0, 20), new AppContext());
		trafficMan = new TrafficMan(null, N, BEFORE);
		cross.setTrafficMan(trafficMan);
		cross.setupReturnPoints();
	}

	@ParameterizedTest
	@MethodSource("anyManDirAndAnyMoveDir")
	public void sholdReturnNullWhenNotMovedCross(Direction manDir, Direction moveDir) {
		trafficMan.setDirection(manDir);
		Vector3f startPoint = startPoints.get(moveDir);

		Boolean result = cross.passedCross(moveDir, null, (int) startPoint.x, (int) startPoint.z);

		assertNull(result);
	}

	static List<Arguments> anyManDirAndAnyMoveDir() {
		List<Arguments> params = new ArrayList<>();
		for (Direction manDir : Direction.values())
			for (Direction moveDir : Direction.values())
				params.add(Arguments.arguments(manDir, moveDir));
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromBackSomeoneStick")
	public void sholdReturnFalseWhenStickSomeoneAndFromBack(StickPosition stickPosition, Direction manDirection) {
		trafficMan.setStickPosition(stickPosition);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(manDirection);
		Vector3f middlePoint = middlePoints.get(manDirection);
		Vector3f checkPoint = checkPoints.get(manDirection);

		cross.passedCross(manDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(manDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(manDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> fromBackSomeoneStick() {
		List<Arguments> params = new ArrayList<>();
		for (Direction direction : Direction.values())
			for (StickPosition position : StickPosition.values())
				params.add(Arguments.arguments(position, direction));
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromBackOrFrontAndStick_A")
	public void sholdReturnFalseWhenStick_A_AndFromBackOrFront(StickPosition stickPosition, Direction manDirection,
			Direction moveDirection, Direction turnDirection) {
		trafficMan.setStickPosition(stickPosition);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> fromBackOrFrontAndStick_A() {
		List<Arguments> params = new ArrayList<>();
		List<StickPosition> positions = Arrays.asList(BEFORE, DOWN, SIDE);
		for (Direction turnDirection : Direction.values())
			for (Direction direction : Direction.values())
				for (StickPosition position : positions)
					params.add(Arguments.arguments(position, direction, direction, turnDirection));
		for (Direction turnDirection : Direction.values())
			for (StickPosition position : positions) {
				params.add(Arguments.arguments(position, N, S, turnDirection));
				params.add(Arguments.arguments(position, S, N, turnDirection));
				params.add(Arguments.arguments(position, E, W, turnDirection));
				params.add(Arguments.arguments(position, W, E, turnDirection));
			}
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromLeftOrRightAndStick_A_goForwardOrRight")
	public void sholdReturnTrueWhenStick_A_AndFromLeftOrRight_goForwardOrRight(StickPosition stickPosition,
			Direction manDirection, Direction moveDirection, Direction turnDirection) {
		trafficMan.setStickPosition(stickPosition);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertTrue(result);
	}

	static List<Arguments> fromLeftOrRightAndStick_A_goForwardOrRight() {
		List<Arguments> params = new ArrayList<>();
		List<StickPosition> positions = Arrays.asList(BEFORE, DOWN, SIDE);
		for (StickPosition position : positions) {
			params.add(Arguments.arguments(position, N, W, W));
			params.add(Arguments.arguments(position, N, W, N));
			params.add(Arguments.arguments(position, N, E, E));
			params.add(Arguments.arguments(position, N, E, S));

			params.add(Arguments.arguments(position, S, W, W));
			params.add(Arguments.arguments(position, S, W, N));
			params.add(Arguments.arguments(position, S, E, E));
			params.add(Arguments.arguments(position, S, E, S));

			params.add(Arguments.arguments(position, W, N, N));
			params.add(Arguments.arguments(position, W, N, E));
			params.add(Arguments.arguments(position, W, S, S));
			params.add(Arguments.arguments(position, W, S, W));

			params.add(Arguments.arguments(position, E, N, N));
			params.add(Arguments.arguments(position, E, N, E));
			params.add(Arguments.arguments(position, E, S, S));
			params.add(Arguments.arguments(position, E, S, W));
		}
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromLeftOrRightAndStick_A_goLeftOrReturn")
	public void sholdReturnFalseWhenStick_A_AndFromLeftOrRight_goLeftOrReturn(StickPosition stickPosition,
			Direction manDirection, Direction moveDirection, Direction turnDirection) {
		trafficMan.setStickPosition(stickPosition);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> fromLeftOrRightAndStick_A_goLeftOrReturn() {
		List<Arguments> params = new ArrayList<>();
		List<StickPosition> positions = Arrays.asList(BEFORE, DOWN, SIDE);
		for (StickPosition position : positions) {
			params.add(Arguments.arguments(position, N, W, E));
			params.add(Arguments.arguments(position, N, W, S));
			params.add(Arguments.arguments(position, N, E, W));
			params.add(Arguments.arguments(position, N, E, N));

			params.add(Arguments.arguments(position, S, W, E));
			params.add(Arguments.arguments(position, S, W, S));
			params.add(Arguments.arguments(position, S, E, W));
			params.add(Arguments.arguments(position, S, E, N));

			params.add(Arguments.arguments(position, W, N, S));
			params.add(Arguments.arguments(position, W, N, W));
			params.add(Arguments.arguments(position, W, S, N));
			params.add(Arguments.arguments(position, W, S, E));

			params.add(Arguments.arguments(position, E, N, S));
			params.add(Arguments.arguments(position, E, N, W));
			params.add(Arguments.arguments(position, E, S, N));
			params.add(Arguments.arguments(position, E, S, E));
		}
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromLeftAndStick_B")
	public void sholdReturnTrueWhenStick_B_AndFromLeft(Direction manDirection, Direction moveDirection,
			Direction turnDirection) {
		trafficMan.setStickPosition(FORWARD);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertTrue(result);
	}

	static List<Arguments> fromLeftAndStick_B() {
		List<Arguments> params = new ArrayList<>();
		for (Direction turnDirection : Direction.values()) {
			params.add(Arguments.arguments(N, E, turnDirection));
			params.add(Arguments.arguments(E, S, turnDirection));
			params.add(Arguments.arguments(S, W, turnDirection));
			params.add(Arguments.arguments(W, N, turnDirection));
		}
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromFrontAndStick_B_goRight")
	public void sholdReturnTrueWhenStick_B_AndFromFront_goRight(Direction manDirection, Direction moveDirection,
			Direction turnDirection) {
		trafficMan.setStickPosition(FORWARD);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertTrue(result);
	}

	static List<Arguments> fromFrontAndStick_B_goRight() {
		List<Arguments> params = new ArrayList<>();
		params.add(Arguments.arguments(N, S, W));
		params.add(Arguments.arguments(E, W, N));
		params.add(Arguments.arguments(S, N, E));
		params.add(Arguments.arguments(W, E, S));
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromFrontAndStick_B_goNotRight")
	public void sholdReturnFalseWhenStick_B_AndFromFront_goNotRight(Direction manDirection, Direction moveDirection,
			Direction turnDirection) {
		trafficMan.setStickPosition(FORWARD);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> fromFrontAndStick_B_goNotRight() {
		List<Arguments> params = new ArrayList<>();
		for (Direction turnDirection : Direction.values()) {
			if (!W.equals(turnDirection))
				params.add(Arguments.arguments(N, S, turnDirection));
			if (!N.equals(turnDirection))
				params.add(Arguments.arguments(E, W, turnDirection));
			if (!E.equals(turnDirection))
				params.add(Arguments.arguments(S, N, turnDirection));
			if (!S.equals(turnDirection))
				params.add(Arguments.arguments(W, E, turnDirection));
		}
		return params;
	}

	@ParameterizedTest
	@MethodSource("fromRightOrBackAndStick_B")
	public void sholdReturnFalseWhenStick_B_AndFromRightOrBack(Direction manDirection, Direction moveDirection,
			Direction turnDirection) {
		trafficMan.setStickPosition(FORWARD);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> fromRightOrBackAndStick_B() {
		List<Arguments> params = new ArrayList<>();
		for (Direction turnDirection : Direction.values()) {
			params.add(Arguments.arguments(N, N, turnDirection));
			params.add(Arguments.arguments(E, E, turnDirection));
			params.add(Arguments.arguments(S, S, turnDirection));
			params.add(Arguments.arguments(W, W, turnDirection));
			params.add(Arguments.arguments(N, W, turnDirection));
			params.add(Arguments.arguments(E, N, turnDirection));
			params.add(Arguments.arguments(S, E, turnDirection));
			params.add(Arguments.arguments(W, S, turnDirection));
		}
		return params;
	}

	@ParameterizedTest
	@MethodSource("stick_C")
	public void sholdReturnFalseWhenStick_C(Direction manDirection, Direction moveDirection, Direction turnDirection) {
		trafficMan.setStickPosition(UP);
		trafficMan.setDirection(manDirection);

		Vector3f startPoint = startPoints.get(moveDirection);
		Vector3f middlePoint = middlePoints.get(moveDirection);
		Vector3f checkPoint = checkPoints.get(turnDirection);

		cross.passedCross(moveDirection, null, (int) startPoint.x, (int) startPoint.z);
		Boolean result = cross.passedCross(moveDirection, null, (int) middlePoint.x, (int) middlePoint.z);
		if (result == null)
			result = cross.passedCross(turnDirection, null, (int) checkPoint.x, (int) checkPoint.z);

		assertFalse(result);
	}

	static List<Arguments> stick_C() {
		List<Arguments> params = new ArrayList<>();
		for (Direction manDirection : Direction.values())
			for (Direction moveDirection : Direction.values())
				for (Direction turnDirection : Direction.values())
					params.add(Arguments.arguments(manDirection, moveDirection, turnDirection));
		return params;
	}
}
