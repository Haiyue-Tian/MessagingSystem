package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.dao.UserValidationCodeDAO;
import com.haiyue.messaging.enums.Gender;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.model.UserValidationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserDAO mockUserDAO;
    @Mock
    UserValidationCodeDAO mockUserValidationCodeDAO;
    @Mock
    JavaMailEmailService mockJavaMailEmailService;
    @InjectMocks
    UserService userService;

    @Test
    public void testRegisterUser_happyCase() throws Exception{
        when(this.mockUserDAO.selectByUsername("user")).thenReturn(List.of());
        when(this.mockUserDAO.selectByEmail("email")).thenReturn(List.of());
        this.userService.registerUser(
                "user",
                "user",
                "password",
                "password",
                "address",
                Gender.MALE,
                "email");
        verify(this.mockUserDAO).selectByUsername("user");
        verify(this.mockUserDAO).selectByEmail("email");
        verify(this.mockUserDAO).insertUser(any(User.class));
        verify(this.mockUserValidationCodeDAO).insert(any(UserValidationCode.class));
        verify(this.mockJavaMailEmailService).sendValidationEmail(eq("email"), anyString());
    }

    @Test
    public void testRegisterUser_passwordsNotFound_throwsMessageServiceException(){
        MessageServiceException messageServiceException = assertThrows(MessageServiceException.class,
                () -> this.userService.registerUser(
                        "user",
                        "user",
                        "password1",
                        "password2",
                        "address",
                        Gender.MALE,
                        "email"));
        assertEquals(Status.PASSWORD_NOT_MATCHED, messageServiceException.getStatus());
    }

    @Test
    public void testRegisterUser_passwordsTooShort_throwsMessageServiceException(){
        MessageServiceException messageServiceException = assertThrows(MessageServiceException.class,
                () -> this.userService.registerUser(
                        "user",
                        "user",
                        "pass",
                        "pass",
                        "address",
                        Gender.MALE,
                        "email"));
        assertEquals(Status.PASSWORD_TOO_SHORT, messageServiceException.getStatus());
    }

    @Test
    public void testRegisterUser_emailExists_throwsMessageServiceException(){
        when(this.mockUserDAO.selectByEmail("email")).thenReturn(List.of(new User()));
        MessageServiceException messageServiceException = assertThrows(MessageServiceException.class,
                () -> this.userService.registerUser(
                        "user",
                        "user",
                        "password",
                        "password",
                        "address",
                        Gender.MALE,
                        "email"));
        assertEquals(Status.EMAIL_EXISTS, messageServiceException.getStatus());
    }

    @Test
    public void testRegisterUser_usernameExists_throwsMessageServiceException(){
        when(this.mockUserDAO.selectByUsername("user")).thenReturn(List.of(new User()));
        MessageServiceException messageServiceException = assertThrows(MessageServiceException.class,
                () -> this.userService.registerUser(
                        "user",
                        "user",
                        "password",
                        "password",
                        "address",
                        Gender.MALE,
                        "email"));
        assertEquals(Status.USERNAME_EXISTS, messageServiceException.getStatus());
    }

    @Test
    public void testActivate_happyCase() throws Exception{
        var user = new User();
        user.setId(1);
        when(this.mockUserDAO.selectByUsername("username")).thenReturn(List.of(user));
        when(this.mockUserValidationCodeDAO.selectValidationCodeByUserId(1)).thenReturn("123456");
        this.userService.activate("username", "123456");
        verify(this.mockUserDAO).selectByUsername("username");
        verify(this.mockUserDAO).updateIsValid(1);
        verify(this.mockUserValidationCodeDAO).deleteByUserId(1);
    }

    @Test
    public void testActivate_validationCodeNotMatched_throwsMessageServiceException() throws Exception{
        var user = new User();
        user.setId(1);
        when(this.mockUserDAO.selectByUsername("username")).thenReturn(List.of(user));
        when(this.mockUserValidationCodeDAO.selectValidationCodeByUserId(1)).thenReturn("123567");
        MessageServiceException messageServiceException = assertThrows(MessageServiceException.class,
                () -> this.userService.activate("username", "123456"));
        assertEquals(Status.VALIDATION_CODE_NOT_MATCHED, messageServiceException.getStatus());
        verify(this.mockUserDAO).selectByUsername("username");
    }

    @Test
    public void testActivate_nonExistingUser_throwsMessageServiceException() throws Exception {
        when(this.mockUserDAO.selectByUsername("user")).thenReturn(List.of());
        assertThrows(MessageServiceException.class,
                () -> this.userService.activate("user", "123456"));
        verify(this.mockUserDAO).selectByUsername("user");
    }

}
