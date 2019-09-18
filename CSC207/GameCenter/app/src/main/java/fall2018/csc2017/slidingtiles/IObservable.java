package fall2018.csc2017.slidingtiles;

public interface IObservable<T> {

    void addObserver(IObserver o);

    void notifyObservers();

    boolean hasChanged();

    void setChanged();

}