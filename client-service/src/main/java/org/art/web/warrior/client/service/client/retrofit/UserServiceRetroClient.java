package org.art.web.warrior.client.service.client.retrofit;

import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserServiceRetroClient {

    @POST("/users")
    Call<UserDto> registerNewUserAccount(@Body UserDto userDto);

    @GET("/users/{email}")
    Call<UserDto> findUserByEmail(@Path("email") String email);

    @PUT("/users")
    Call<Void> updateUser(@Body UserDto userDto);

    @PUT("/users/{email}")
    Call<Void> addTaskOrder(@Path("email") String email, @Body TaskOrderDto taskOrderDto);

    @DELETE("/users/{email}")
    Call<Void> deleteUserByEmail(@Path("email") String email);
}
