package com.techsoft.api.common;

import com.techsoft.api.common.error.HttpResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AbstractService<T, D> {
    protected final JpaRepository<T, Long> repository;
    protected final Class<T> domainClass;

    public AbstractService(JpaRepository<T, Long> repository, Class<T> domainClass) {
        this.repository = repository;
        this.domainClass = domainClass;
    }

    public T saveDto(D domainDto) {
        try {
            T domain = domainClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(domainDto, domain);
            return repository.save(domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpResponseException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Error create %s", domainClass.getSimpleName())
            );
        }
    }

    public T saveDomain(T domain) {
        try {
            return repository.save(domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpResponseException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Error create %s", domainClass.getSimpleName())
            );
        }
    }

    public T findById(Long id) {
        Optional<T> domain = repository.findById(id);

        if(domain.isPresent()) {
            return domain.get();
        }

        throw notFound();
    }

    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<T> list() {
        return repository.findAll();
    }

    public void delete(Long id) {
        Optional<T> domain = repository.findById(id);

        if(domain.isPresent()) {
            repository.delete(domain.get());
            return;
        }

        throw notFound();
    }

    private HttpResponseException notFound() {
        return new HttpResponseException(
                HttpStatus.NOT_FOUND,
                String.format("%s not found!", domainClass.getSimpleName())
        );
    }
}
