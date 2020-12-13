package tpr.antonius.weatherapp.service.cache;

import java.util.Optional;

public interface Cache<T> {

    void putValue(T value);

    Optional<T> getValue();

    T getValueNull();

}
