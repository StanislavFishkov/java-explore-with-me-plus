package ru.practicum.ewm.event.service;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.core.error.exception.ConflictDataException;
import ru.practicum.ewm.core.error.exception.NotFoundException;
import ru.practicum.ewm.core.error.exception.ValidationException;
import ru.practicum.ewm.core.util.DateTimeUtil;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.ewm.participationrequest.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.participationrequest.model.ParticipationRequest;
import ru.practicum.ewm.participationrequest.model.ParticipationRequestStatus;
import ru.practicum.ewm.participationrequest.model.QParticipationRequest;
import ru.practicum.ewm.participationrequest.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    private Event checkAndGetEventByIdAndInitiatorId(Long eventId, Long initiatorId) {
        return eventRepository.findByIdAndInitiator_Id(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException(String.format("On event operations - " +
                        "Event doesn't exist with id %s or not available for User with id %s: ", eventId, initiatorId)));
    }

    @Override
    public EventFullDto addEvent(Long id, NewEventDto newEventDto) {
        checkEventTime(newEventDto.getEventDate());
        Category category = categoriesRepository.findById(newEventDto.getCategory()).get();
        User user = userRepository.findById(id).get();
        Location location = getOrCreateLocation(newEventDto.getLocation());

        Event event = eventRepository.save(eventMapper.toEvent(newEventDto, category, user, location));
        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long id) {
        List<EventShortDto> eventShortDtos = eventRepository.findAllByInitiator_Id(id)
                .stream()
                .map(eventMapper::toShortDto)
                .toList();

        BooleanExpression byStatusAndEventId = QParticipationRequest
                .participationRequest
                .status
                .eq(ParticipationRequestStatus.CONFIRMED)
                .and(QParticipationRequest.participationRequest
                        .event.id.in(eventShortDtos.stream().map(EventShortDto::getId).toList()));

        List<ParticipationRequest> participationRequestsList = (List<ParticipationRequest>)
                participationRequestRepository.findAll(byStatusAndEventId);

        for (EventShortDto eventShortDto : eventShortDtos) {
            eventShortDto.setConfirmedRequests((int) participationRequestsList.stream()
                    .filter(participationRequest -> participationRequest.getEvent().getId().equals(eventShortDto.getId()))
                    .count());
        }
        return eventShortDtos;
    }


    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        EventFullDto eventFullDto = eventMapper.toFullDto(checkAndGetEventByIdAndInitiatorId(eventId, userId));
        eventFullDto.setConfirmedRequests(participationRequestRepository.findAllByEvent_IdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED).size());
        log.info("Количество подтвержденных запросов: {}", participationRequestRepository.findAllByEvent_IdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED).size());
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventUpdateDto) {
        Event event = checkAndGetEventByIdAndInitiatorId(eventId, userId);
        eventMapper.update(event, eventUpdateDto, getOrCreateLocation(eventUpdateDto.getLocation()));
        if (eventUpdateDto.getStateAction() != null) {
            setStateToEvent(eventUpdateDto, event);
        }

        event.setId(eventId);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("On Event admin update - Event doesn't exist with id: " + eventId));

        Category category = null;
        if (updateEventAdminRequestDto.getCategory() != null)
            category = categoriesRepository.findById(updateEventAdminRequestDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("On Event admin update - Category doesn't exist with id: " +
                            updateEventAdminRequestDto.getCategory()));

        event = eventMapper.update(event, updateEventAdminRequestDto, category,
                getOrCreateLocation(updateEventAdminRequestDto.getLocation()));
        calculateNewEventState(event, updateEventAdminRequestDto.getStateAction());

        event = eventRepository.save(event);
        log.info("Event is updated by admin: {}", event);
        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> get(EventsFilterParamsDto filters) {
        QEvent event = QEvent.event;

        BooleanBuilder builder = new BooleanBuilder();

        if (filters.getUsers() != null && !filters.getUsers().isEmpty())
            builder.and(event.initiator.id.in(filters.getUsers()));

        if (filters.getStates() != null && !filters.getStates().isEmpty())
            builder.and(event.state.in(filters.getStates()));

        if (filters.getCategories() != null && !filters.getCategories().isEmpty())
            builder.and(event.category.id.in(filters.getCategories()));

        if (filters.getRangeStart() != null)
            builder.and(event.eventDate.goe(filters.getRangeStart()));

        if (filters.getRangeEnd() != null)
            builder.and(event.eventDate.loe(filters.getRangeEnd()));

        int from = filters.getFrom();
        int size = filters.getSize();
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, new QSort(event.createdOn.desc()));

        var result = eventMapper.toFullDto(eventRepository.findAll(builder, page));

        BooleanExpression byStatusAndEventId = QParticipationRequest
                .participationRequest
                .status
                .eq(ParticipationRequestStatus.CONFIRMED)
                .and(QParticipationRequest.participationRequest
                        .event.id.in(result.stream().map(EventFullDto::getId).toList()));

        List<ParticipationRequest> participationRequestsList = (List<ParticipationRequest>)
                participationRequestRepository.findAll(byStatusAndEventId);

        for (EventFullDto eventFullDto : result) {
            eventFullDto.setConfirmedRequests((int) participationRequestsList.stream()
                    .filter(participationRequest -> participationRequest.getEvent().getId().equals(eventFullDto.getId()))
                    .count());
        }
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getEventAllParticipationRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Такого события не существует: " + eventId));
        checkEventOwner(event, userId);
        return participationRequestRepository.findAllByEvent_IdAndStatus(eventId, ParticipationRequestStatus.PENDING)
                .stream()
                .map(participationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto changeEventState(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequestDto statusUpdateRequest) {
        log.info("Change event state by user: {} and event id: {}", userId, eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Такого события не существует: " + eventId));
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Такого пользователя не существует: " + userId));
        checkEventOwner(event, userId);
        int participantsLimit = event.getParticipantLimit();

        List<ParticipationRequest> confirmedRequests = participationRequestRepository.findAllByEvent_IdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED);
        List<ParticipationRequest> requestToChangeStatus = statusUpdateRequest.getRequestIds()
                .stream()
                .map(participationRequestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        //Не очень понял, как обрабатывать это условие:
        // "если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется"
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            log.info("Заявки подтверждать не требуется");
            return null;
        }

        log.info("Заявки:  Лимит: {}, а заявок {}, разница между ними: {}", participantsLimit,
                statusUpdateRequest.getRequestIds().size(), (participantsLimit
                        - statusUpdateRequest.getRequestIds().size()));

        if (statusUpdateRequest.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
            log.info("меняем статус заявок для статуса: {}", ParticipationRequestStatus.CONFIRMED);
            if ((participantsLimit - (confirmedRequests.size()) - statusUpdateRequest.getRequestIds().size()) >= 0) {
                for (ParticipationRequest request : requestToChangeStatus) {
                    request.setStatus(ParticipationRequestStatus.CONFIRMED);
                    participationRequestRepository.save(request);
                }
                return new EventRequestStatusUpdateResultDto(requestToChangeStatus
                        .stream().map(participationRequestMapper::toDto)
                        .toList()
                        , null);
            } else {
                throw new ConflictDataException("слишком много участников. Лимит: " + participantsLimit +
                        ", уже подтвержденных заявок: " + confirmedRequests.size() + ", а заявок на одобрение: " +
                        statusUpdateRequest.getRequestIds().size() +
                        ". Разница между ними: " + (participantsLimit - confirmedRequests.size() -
                        statusUpdateRequest.getRequestIds().size()));
            }
        } else if (statusUpdateRequest.getStatus().equals(ParticipationRequestStatus.REJECTED)) {
            log.info("меняем статус заявок для статуса: {}", ParticipationRequestStatus.REJECTED);
            for (ParticipationRequest request : requestToChangeStatus) {
                if (request.getStatus() == ParticipationRequestStatus.CONFIRMED) {
                    throw new ConflictDataException("Заявка" + request.getStatus() + "уже подтверждена.");
                }
                request.setStatus(ParticipationRequestStatus.REJECTED);
                participationRequestRepository.save(request);
            }
            return new EventRequestStatusUpdateResultDto(null, requestToChangeStatus
                    .stream().map(participationRequestMapper::toDto)
                    .toList());
        }
        return null;
    }

    private Location getOrCreateLocation(LocationDto locationDto) {
        return locationDto == null ? null : locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon())
                .orElseGet(() -> locationRepository.save(locationMapper.toLocation(locationDto)));
    }

    private void calculateNewEventState(Event event, EventStateActionAdmin stateAction) {
        if (stateAction == EventStateActionAdmin.PUBLISH_EVENT) {
            if (event.getState() != EventStates.PENDING) {
                throw new ConflictDataException(
                        String.format("On Event admin update - " +
                                        "Event with id %s can't be published from the state %s: ",
                                event.getId(), event.getState()));
            }

            LocalDateTime currentDateTime = DateTimeUtil.currentDateTime();
            if (currentDateTime.plusHours(1).isAfter(event.getEventDate()))
                throw new ConflictDataException(
                        String.format("On Event admin update - " +
                                        "Event with id %s can't be published because the event date is to close %s: ",
                                event.getId(), event.getEventDate()));

            event.setPublishedOn(currentDateTime);
            event.setState(EventStates.PUBLISHED);
        } else if (stateAction == EventStateActionAdmin.REJECT_EVENT) {
            if (event.getState() == EventStates.PUBLISHED) {
                throw new ConflictDataException(
                        String.format("On Event admin update - " +
                                        "Event with id %s can't be canceled because it is already published: ",
                                event.getState()));
            }

            event.setState(EventStates.CANCELED);
        }
    }

    private void setStateToEvent(UpdateEventUserRequestDto eventUpdateDto, Event event) {
        if (eventUpdateDto.getStateAction().toString()
                .equalsIgnoreCase(EventStateActionPrivate.CANCEL_REVIEW.toString())) {
            event.setState(EventStates.CANCELED);
        } else if (eventUpdateDto.getStateAction().toString()
                .equalsIgnoreCase(EventStateActionPrivate.SEND_TO_REVIEW.toString())) {
            event.setState(EventStates.PENDING);
        }
    }

    private void checkEventTime(LocalDateTime eventDate) {
        log.info("Проверяем дату события на корректность: {}", eventDate);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime correctEventTime = eventDate.plusHours(2);
        if (correctEventTime.isBefore(now)) {
            log.info("дата не корректна");
            throw new ValidationException("Дата события должна быть +2 часа вперед");
        }

    }

    private void checkEventOwner(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ValidationException("Событие создал другой пользователь");
        }
    }
}
