package ru.practicum.afisha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.dto.StatDto;
import ru.practicum.afisha.model.StatMapper;
import ru.practicum.afisha.repositories.StatRepository;

import java.util.List;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final StatMapper mapper;

    @Autowired
    public StatServiceImpl(StatRepository repository, StatMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StatDto createStat(StatDto statDto) {
        repository.save(mapper.toStat(statDto));
        return statDto;
    }

    public List<StatCountDto> getStat(GetStatDto getStatDto) {
        if (getStatDto.isUnique() && getStatDto.getUris() != null) {
            return repository.getStatUniqueIP(getStatDto.getStart(), getStatDto.getEnd(),
                    getStatDto.getUris());
        }
        if (!getStatDto.isUnique() && getStatDto.getUris() != null) {
            return repository.getStatNonUniqueIP(getStatDto.getStart(), getStatDto.getEnd(),
                    getStatDto.getUris());
        }
        if (getStatDto.isUnique() && getStatDto.getUris() == null) {
            return repository.getStatUniqueIPNoUris(getStatDto.getStart(), getStatDto.getEnd());
        }
        if (!getStatDto.isUnique() && getStatDto.getUris() == null) {
            return repository.getStatNonUniqueIPNoUris(getStatDto.getStart(), getStatDto.getEnd());
        }
        return null;
    }
}
