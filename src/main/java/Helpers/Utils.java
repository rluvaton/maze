package Helpers;

import Helpers.ThrowableAssertions.ObjectAssertion;
import Maze.ELocation;
import Maze.MazeBuilder.IMazeBuilder;

import java.util.*;
import java.util.stream.Stream;

public class Utils {
    /**
     * Helpers.Helpers Instance
     * TODO - Instead of creating this a singleton make all functions static
     */
    public static final Utils Instance = new Utils();

    public static <T> Optional<T> getOptional(T value) {
        return Optional.ofNullable(value);
    }

    public final Coordinate moveCoordinatesToDirection(Coordinate pos, Direction dir) {
        assert pos != null && dir != null;
        return this.moveCoordinatesToDirection(pos.getRow(), pos.getColumn(), dir);
    }

    public final Coordinate moveCoordinatesToDirection(int row, int col, Direction dir) {
        assert dir != null;

        Coordinate directionMove = dir.getValue();

        return new Coordinate(row + directionMove.getRow(), col + directionMove.getColumn());
    }

    public Direction getDirectionOfMove(Coordinate from, Coordinate to) {
        assert from != null && to != null;

        Direction[] allDirections = Direction.values();
        Coordinate directionCoordinates = new Coordinate(to.getRow() - from.getRow(), to.getColumn() - from.getColumn());

        for (Direction direction : allDirections) {
            if (directionCoordinates.equals(direction.getValue())) {
                return direction;
            }
        }

        throw new IllegalArgumentException("from and not near each other");
    }

    /**
     * Check if index is in the boundaries of the vector
     *
     * @param index Index to check
     * @param arr   Array to check
     *              <typeparam name="T">Array Type</typeparam>
     * @return Returns if the index is in the vector boundaries
     */
    public final <T> boolean inBounds(int index, T[] arr) {
        return (index >= 0) && (index < arr.length);
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param row Row to check
     * @param col Col to check
     * @param arr Array to check
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(int row, int col, T[][] arr) {
        return col >= 0 && col < arr.length && row >= 0 && row < arr[0].length;
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param row    Row to check
     * @param col    Col to check
     * @param height Height of Matrix
     * @param width  Width of Matrix
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(int row, int col, int height, int width) {
        return col >= 0 && col < height && row >= 0 && row < width;
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param loc    Tuple of the row and the col in the matrix
     * @param height Height of Matrix
     * @param width  Width of Matrix
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(Coordinate loc, int height, int width) {
        return loc.getRow() >= 0 && loc.getRow() < height && loc.getColumn() >= 0 && loc.getColumn() < width;
    }

    /**
     * Check if row and column in limit
     *
     * @param row       row to check
     * @param col       column to check
     * @param direction direction to check
     * @param width     width of mat
     * @param height    height of mat
     * @return Returns if in limit
     */
    public final boolean inLimits(int row, int col, Direction direction, int width, int height) {
        return (row == 0 && direction == Direction.UP) || (row == height - 1 && direction == Direction.DOWN) ||
                (col == 0 && direction == Direction.LEFT) || (col == width - 1 && direction == Direction.RIGHT);
    }

    public final boolean inLimits(Coordinate coordinate, Direction direction, int width, int height) {
        assert coordinate != null;
        return this.inLimits(coordinate.getRow(), coordinate.getColumn(), direction, width, height);
    }

    public final boolean inLimits(ELocation eLocation, int width, int height) {
        assert eLocation != null;
        Coordinate coordinate = eLocation.getLocation();

        assert coordinate != null;
        return this.inLimits(coordinate.getRow(), coordinate.getColumn(), eLocation.getDirection(), width, height);
    }

    public final boolean inLimits(IMazeBuilder.ELocationBaseData eLocation, int width, int height) {
        assert eLocation != null;
        Coordinate coordinate = eLocation.getPos();

        assert coordinate != null;
        return this.inLimits(coordinate.getRow(), coordinate.getColumn(), eLocation.getDirection(), width, height);
    }


    /**
     * Convert Array to stream
     *
     * @param arr Array to convert
     * @param <T> Type of the array
     * @return Returns the stream of the array
     */
    public <T> Stream<T> convertArrayToStream(T[] arr) {
        return Arrays.stream(arr);
    }

    /**
     * Convert Matrix to stream of stream
     *
     * @param mat Matrix to convert
     * @param <T> type of the matrix
     * @return Returns the stream of stream of the matrix
     */
    public <T> Stream<Stream<T>> convertMatrixToStream(T[][] mat) {
        return Arrays.stream(mat).map(Arrays::stream);
    }

    public <T> List<T> reverseList(List<T> list) {
        LinkedList<T> reversed = new LinkedList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static  <K, V> AbstractMap<K, V> deepCloneMap(AbstractMap<K, V> source, CallbackFns.ArgsCallbackFunction<K> cloneKeyFn, CallbackFns.ArgsCallbackFunction<V> cloneValueFn) {
        if(source == null) {
            return null;
        }

        ObjectAssertion.requireNonNull(cloneKeyFn, "Clone Key Function can't be null");
        ObjectAssertion.requireNonNull(cloneValueFn, "Clone Value Function can't be null");

        AbstractMap<K, V> cloneTo = new HashMap<>();

        for (Map.Entry<K, V> entry : source.entrySet()) {
            K key = cloneKeyFn.run(entry.getKey());
            V value = cloneValueFn.run(entry.getValue());

            cloneTo.put(key, value);
        }
        return cloneTo;
    }

    public static <T> T[][] cloneMatrix(T[][] matrixToClone) {
        if (matrixToClone == null) {
            return null;
        }

        T[][] clonedMatrix = matrixToClone.clone();

        for (int i = 0; i < matrixToClone.length; i++) {
            if (clonedMatrix[i] == null) {
                continue;
            }

            clonedMatrix[i] = clonedMatrix[i].clone();

            for (int j = 0; j < clonedMatrix[i].length; j++) {
                clonedMatrix[i][j] = clone(clonedMatrix[i][j]);
            }
        }

        return clonedMatrix;
    }

    private static <T> T clone(T itemToClone) {
        if (itemToClone instanceof SuccessCloneable) {
            return ((SuccessCloneable<T>) itemToClone).clone();
        }

        return itemToClone;
    }

    public static <T extends SuccessCloneable<T>> T clone(T cloneable) {
        return cloneable == null ? null : cloneable.clone();
    }
}

