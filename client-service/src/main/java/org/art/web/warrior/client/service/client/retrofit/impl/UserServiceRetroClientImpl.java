package org.art.web.warrior.client.service.client.retrofit.impl;

import org.art.web.warrior.client.exception.ExternalServiceInvocationException;
import org.art.web.warrior.client.service.client.api.UserServiceClient;
import org.art.web.warrior.client.service.client.retrofit.UserServiceRetroClient;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.art.web.warrior.client.CommonServiceConstants.OBJECT_MAPPER;
import static org.art.web.warrior.commons.CommonConstants.PROFILE_RETROFIT;

@Service
@Profile(PROFILE_RETROFIT)
public class UserServiceRetroClientImpl implements UserServiceClient {

    private final UserServiceRetroClient userServiceRetroClient;

    @Autowired
    public UserServiceRetroClientImpl(UserServiceRetroClient userServiceRetroClient) {
        this.userServiceRetroClient = userServiceRetroClient;
    }

    @Override
    public UserDto registerNewUserAccount(UserDto userDto) {
        Response<UserDto> serviceResponse;
        try {
            Call<UserDto> serviceCall = userServiceRetroClient.registerNewUserAccount(userDto);
            serviceResponse = serviceCall.execute();
            if (!serviceResponse.isSuccessful()) {
                rethrowAdaptedException(serviceResponse);
            }
            return serviceResponse.body();
        } catch (ExternalServiceInvocationException e) {
            throw e;
        } catch (Exception e) {
            CommonApiError commonApiError = buildInternalErrorDetails(e);
            throw new ExternalServiceInvocationException(e.getMessage(), commonApiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        Response<UserDto> serviceResponse;
        try {
            Call<UserDto> serviceCall = userServiceRetroClient.findUserByEmail(email);
            serviceResponse = serviceCall.execute();
            if (!serviceResponse.isSuccessful()) {
                rethrowAdaptedException(serviceResponse);
            }
            return serviceResponse.body();
        } catch (ExternalServiceInvocationException e) {
            throw e;
        } catch (Exception e) {
            CommonApiError commonApiError = buildInternalErrorDetails(e);
            throw new ExternalServiceInvocationException(e.getMessage(), commonApiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDto userDto) {
        Response<Void> serviceResponse;
        try {
            Call<Void> serviceCall = userServiceRetroClient.updateUser(userDto);
            serviceResponse = serviceCall.execute();
            if (!serviceResponse.isSuccessful()) {
                rethrowAdaptedException(serviceResponse);
            }
        } catch (ExternalServiceInvocationException e) {
            throw e;
        } catch (Exception e) {
            CommonApiError commonApiError = buildInternalErrorDetails(e);
            throw new ExternalServiceInvocationException(e.getMessage(), commonApiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void addTaskOrder(String email, TaskOrderDto taskOrderDto) {
        Response<Void> serviceResponse;
        try {
            Call<Void> serviceCall = userServiceRetroClient.addTaskOrder(email, taskOrderDto);
            serviceResponse = serviceCall.execute();
            if (!serviceResponse.isSuccessful()) {
                rethrowAdaptedException(serviceResponse);
            }
        } catch (ExternalServiceInvocationException e) {
            throw e;
        } catch (Exception e) {
            CommonApiError commonApiError = buildInternalErrorDetails(e);
            throw new ExternalServiceInvocationException(e.getMessage(), commonApiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUserByEmail(String email) {
        Response<Void> serviceResponse;
        try {
            Call<Void> serviceCall = userServiceRetroClient.deleteUserByEmail(email);
            serviceResponse = serviceCall.execute();
            if (!serviceResponse.isSuccessful()) {
                rethrowAdaptedException(serviceResponse);
            }
        } catch (ExternalServiceInvocationException e) {
            throw e;
        } catch (Exception e) {
            CommonApiError commonApiError = buildInternalErrorDetails(e);
            throw new ExternalServiceInvocationException(e.getMessage(), commonApiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void rethrowAdaptedException(Response<?> serviceResponse) throws IOException {
        String message = serviceResponse.message();
        int statusCode = serviceResponse.code();
        String errorDetailsStr = serviceResponse.errorBody().string();
        CommonApiError commonApiError = OBJECT_MAPPER.readValue(errorDetailsStr, CommonApiError.class);
        throw new ExternalServiceInvocationException(message, commonApiError, HttpStatus.resolve(statusCode));
    }

    private CommonApiError buildInternalErrorDetails(Exception e) {
        return CommonApiError.builder()
                .message(e.getMessage())
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .respStatusCode(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusCode())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
