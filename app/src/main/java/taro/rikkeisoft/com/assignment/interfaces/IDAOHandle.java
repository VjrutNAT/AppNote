package taro.rikkeisoft.com.assignment.interfaces;

import java.util.List;

/**
 * Created by VjrutNAT on 10/29/2017.
 */

public interface IDAOHandle<T, idT>{

    List<T> getAllElement();

    List<T> getListById(idT id);

    boolean insert(T obj, idT id);

    boolean update(T obj, idT id);

    boolean delete(idT id);

}
