package Maze.MazeBuilder;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Candy.Candy;
import Maze.Maze;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Cell;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;

import static Helpers.Utils.Instance;

public class RectangleMazeBuilder extends BaseMazeBuilder {
    private Cell[][] mazeData;

    private int height;
    private int width;

    private int totalBuiltCell = 0;

    @Override
    public IMazeBuilder buildMazeSkeleton(int height, int width) {
        this.mazeData = new Cell[height][width];

        this.height = height;
        this.width = width;

        return this;
    }

    @Override
    public IMazeBuilder buildAllCellsAsEmpty() {

        // Init the mazeData with empty cubes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.mazeData[i][j] = new Cell(i, j);
                totalBuiltCell++;
            }
        }

        return this;
    }

    @Override
    public IMazeBuilder buildCell(Coordinate position) {
        assert position != null;
        this.mazeData[position.getRow()][position.getColumn()] = new Cell(position);

        totalBuiltCell++;

        return this;
    }

    @Override
    public IMazeBuilder buildCell(Coordinate position, Candy[] candies) {
        assert position != null;
        int row = position.getRow();
        int col = position.getColumn();

        assert row >= 0 && col >= 0;

        this.mazeData[row][col] = new Cell(position);
        this.mazeData[row][col].addCandies(candies);

        totalBuiltCell++;

        return this;
    }

    @Override
    public IMazeBuilder buildDoor(Coordinate cell1Pos, Coordinate cell2Pos) throws Exception {
        buildDoor(cell1Pos, cell2Pos, Instance.getDirectionOfMove(cell1Pos, cell2Pos));
        return this;
    }

    @Override
    public IMazeBuilder buildDoor(Coordinate cell1Pos, Direction doorDirection) throws Exception {
        buildDoor(cell1Pos, Instance.moveCoordinatesToDirection(cell1Pos, doorDirection), doorDirection);

        return this;
    }

    private void buildDoor(Coordinate cell1Pos, Coordinate cell2Pos, Direction direction) throws Exception {
        Cell cell = this.getCellAtPosition(cell1Pos);
        cell.setCellAtDirection(direction, this.getCellAtPosition(cell2Pos));
    }

    @Override
    public IMazeBuilder buildEntrance(Coordinate pos, Direction direction) {
        buildELocation(pos, direction, ELocationType.Entrance);
        return this;
    }

    @Override
    public IMazeBuilder buildExit(Coordinate pos, Direction direction) {
        buildELocation(pos, direction, ELocationType.Exit);
        return this;
    }

    private void buildELocation(Coordinate pos, Direction direction, ELocationType type) {
        assert pos != null && direction != null;
        Cell cell = this.getCellAtPosition(pos);
        ELocation eLocation = new ELocation(pos, direction, type);
        cell.setELocationAsNeighbor(eLocation);
    }

    @Override
    public Maze getMaze() throws MazeBuilderException {
        if(totalBuiltCell < height * width) {
            throw new MazeBuilderException(this, "Not all cells have been built");
        }
        return new Maze(this.mazeData);
    }

    @Override
    public Cell getCellAtPosition(Coordinate pos) {
        assert pos != null;
        return this.mazeData[pos.getRow()][pos.getColumn()];
    }

    @Override
    public int getTotalBuiltCells() {
        return totalBuiltCell;
    }

    @Override
    public IMazeBuilder clone() {
        RectangleMazeBuilder mazeBuilder = new RectangleMazeBuilder();

        mazeBuilder.height = height;
        mazeBuilder.width = width;
        mazeBuilder.totalBuiltCell = totalBuiltCell;
        mazeBuilder.mazeData = mazeData == null ? null : mazeData.clone();

        return mazeBuilder;
    }
}
