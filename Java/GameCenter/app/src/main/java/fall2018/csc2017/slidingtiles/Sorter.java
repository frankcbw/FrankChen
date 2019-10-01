package fall2018.csc2017.slidingtiles;

import java.util.List;

public interface Sorter<T> {
    /**
     * sorting algorithm
     *
     * @param nameAndScore list of score info, sort in-place
     */
    void sort(List<T> nameAndScore);

}
