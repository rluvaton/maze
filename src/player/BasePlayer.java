package player;

import Helpers.Direction;
import Helpers.Tuple;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static Helpers.Utils.Instance;

/**
 * Base Player
 * Abstract class for player
 */
public abstract class BasePlayer {
    /**
     * Subject for where the player move
     */
    private BehaviorSubject<Direction> playerMoveSub = BehaviorSubject.create();

    /**
     * Subject for where the player move
     */
    private BehaviorSubject<LocationChanged> playerLocationChangedSub = BehaviorSubject.create();

    /**
     * Current Location
     */
    private Tuple<Integer, Integer> location;

    /**
     * Previous Location
     */
    private Tuple<Integer, Integer> prevLocation;

    /**
     * Constructor
     *
     * @param location Starting location of the player
     */
    public BasePlayer(Tuple<Integer, Integer> location) {
        this.location = location;
    }

    /**
     * Move at direction
     *
     * @param direction direction to move
     */
    public void move(Direction direction) {
        switch (direction) {
            case TOP:
                this.top();
                break;
            case RIGHT:
                this.right();
                break;
            case BOTTOM:
                this.bottom();
                break;
            case LEFT:
                this.left();
                break;
            default:
                System.out.println("Direction not recognized: " + direction);
                break;
        }
    }

    /**
     * Notify that the player moved
     *
     * @param direction Moving Direction
     */
    protected void notifyMoved(Direction direction) {
        this.playerMoveSub.onNext(direction);
    }

    /**
     * Move Top
     */
    public abstract void top();

    /**
     * Move Right
     */
    public abstract void right();

    /**
     * Move Bottom
     */
    public abstract void bottom();

    /**
     * Move Left
     */
    public abstract void left();

    // region Getter & Setter

    /**
     * Get the observables of the player moves
     *
     * @return Observable of the moving player
     */
    public Observable<Direction> getPlayerMoveObs() {
        return this.playerMoveSub;
    }

    /**
     * Get the observable of the player location
     *
     * @return Observable of the location player
     */
    public Observable<LocationChanged> getPlayerLocationChangedObs() {
        return this.playerLocationChangedSub;
    }

    public Tuple<Integer, Integer> getLocation() {
        return location;
    }

    public void setLocation(Tuple<Integer, Integer> location) {
        prevLocation = this.location;

        // Notify of the location change
        this.playerLocationChangedSub.onNext(new LocationChanged(this.location, location));

        this.location = location;
    }

    public void setLocation(Direction direction) {
        Tuple<Integer, Integer> nextLocation = Instance.getNextCell(this.location, direction);

        prevLocation = this.location;

        // Notify of the location change
        this.playerLocationChangedSub.onNext(new LocationChanged(this.location, nextLocation, direction));

        this.location = nextLocation;
    }

    public Tuple<Integer, Integer> getPrevLocation() {
        return prevLocation;
    }

    // endregion
}